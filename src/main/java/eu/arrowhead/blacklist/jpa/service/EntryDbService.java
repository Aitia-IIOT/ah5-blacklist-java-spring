package eu.arrowhead.blacklist.jpa.service;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import eu.arrowhead.blacklist.jpa.entity.Entry;
import eu.arrowhead.blacklist.jpa.repository.EntryRepository;
import eu.arrowhead.blacklist.service.dto.BlacklistCreateRequestDTO;
import eu.arrowhead.blacklist.service.dto.enums.Mode;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.exception.InternalServerError;

@Service
public class EntryDbService {
	
	//=================================================================================================
	// members
	
	@Autowired
	private EntryRepository entryRepo;
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	private static final Object LOCK = new Object();
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public List<Entry> createBulk(final List<BlacklistCreateRequestDTO> candidates, final String requesterName) {
		logger.debug("createBulk started...");
		
		try {
			return entryRepo.saveAllAndFlush(createEntriesFromDTOs(candidates, requesterName));
		} catch (final Exception ex) {
			logger.error(ex.getMessage());
			logger.debug(ex);
			throw new InternalServerError("Database operation error");
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public Page<Entry> getPageByFilters(final PageRequest pagination, final List<String> systemNames, final Mode mode, 
			final List<String> issuers, final List<String> revokers, final String reason, final ZonedDateTime alivesAt) {
		logger.debug("getByFilters started...");
		Assert.notNull(pagination, "page is null");
		
		try {
			Page<Entry> entries = null;
			
			// Without filters
			if (Utilities.isEmpty(systemNames)
					&& mode == null
					&& Utilities.isEmpty(issuers)
					&& Utilities.isEmpty(revokers)
					&& Utilities.isEmpty(reason)
					&& alivesAt == null){
				entries = entryRepo.findAll(pagination);
			}
			
			// With filters
			if (entries == null) {
				synchronized (LOCK) {
					List<Long> matchingIDs = new ArrayList<>();
					final List<Entry> toFilter = Utilities.isEmpty(systemNames) ? entryRepo.findAll() : entryRepo.findAllBySystemNameIn(systemNames);
					for (final Entry entry : toFilter) {
						if (mode != null && !modeMatch(mode, entry.getActive())) {
							continue;
						}
						if (!Utilities.isEmpty(issuers) && !issuers.contains(entry.getCreatedBy())) {
							continue;
						}
						if (!Utilities.isEmpty(revokers) && (entry.getRevokedBy() == null || !revokers.contains(entry.getRevokedBy()))) {
							continue;
						}
						// reason match is case insensitive
						if (!Utilities.isEmpty(reason) && !entry.getReason().toLowerCase().contains(reason.toLowerCase())) {
							continue;
						}
						if (entry.getExpiresAt() != null && alivesAt != null && !(alivesAt.isBefore(entry.getExpiresAt()) || alivesAt.isEqual(entry.getExpiresAt()))) {
							continue;
						}
						
						matchingIDs.add(entry.getId());
					}
					entries = entryRepo.findAllByIdIn(matchingIDs, pagination);
				}
			}
			return entries;	
		} catch (final Exception ex) {
			logger.error(ex.getMessage());
			logger.debug(ex);
			throw new InternalServerError("Database operation error");
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public void unsetActiveByNameList(final List<String> names, final String revokerName, final String origin) {
		logger.debug("unsetActiveByNameList started...");
		Assert.isTrue(!Utilities.isEmpty(names), "System name list is missing or empty");
		
		try {
			synchronized (LOCK) {
				final List<Entry> toInactivate = entryRepo.findAllBySystemNameIn(names).stream().filter(Entry::getActive).collect(Collectors.toList());
				for (Entry entry : toInactivate) {
					entry.setActive(false);
					entry.setRevokedBy(revokerName);
				}
				entryRepo.saveAllAndFlush(toInactivate);
			}
		} catch (final Exception ex) {
			logger.error(ex.getMessage());
			logger.debug(ex);
			throw new InternalServerError("Database operation error");
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	// Returns true, if there is a record with the given system name, the active flag is set, and the expiration date is in the future.
	public boolean isActiveEntryForName(final String systemName, final String origin) {
		logger.debug("checkSystemName");
		Assert.isTrue(!Utilities.isEmpty(systemName), "System name is missing or empty");
		
		try {
			synchronized (LOCK) {
				
				// finding entries with the matching system name
				List<Entry> entries = entryRepo.findAllBySystemName(systemName);
				if (entries.size() == 0) {
					return false;
				}
				
				for (Entry entry : entries) {
					// the active flag has to be set and the expiration date has to be in the future
					if (entry.getActive() && entry.getExpiresAt().isAfter(ZonedDateTime.now())) {
						return true;
					}
				}
				return false;
			}
		} catch (final Exception ex) {
			logger.error(ex.getMessage());
			logger.debug(ex);
			throw new InternalServerError("Database operation error");
		}
		
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private List<Entry> createEntriesFromDTOs(final List<BlacklistCreateRequestDTO> candidates, final String requesterName) {
		return candidates
				.stream()
				.map(c -> new Entry(
						// system name
						c.systemName(),
						// active
						true,
						// expires at
						Utilities.parseUTCStringToZonedDateTime(c.expiresAt()),
						// created by
						requesterName,
						// revoked by
						null,
						// reason
						c.reason()
						))
				.collect(Collectors.toList());
	}
	
	//-------------------------------------------------------------------------------------------------
	// Returns true, if the record activity state matches the filtering mode
	private boolean modeMatch(final Mode mode, final boolean active) {
		switch (mode) {
			case Mode.ACTIVES:
				return active;
			case Mode.INACTIVES:
				return !active;
			default:
				return true;
		}
	}

}
