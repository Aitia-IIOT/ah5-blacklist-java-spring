package eu.arrowhead.blacklist.jpa.service;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.arrowhead.blacklist.jpa.entity.Entry;
import eu.arrowhead.blacklist.jpa.repository.EntryRepository;
import eu.arrowhead.blacklist.service.dto.BlacklistCreateRequestDTO;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.exception.InternalServerError;

@Service
public class EntryDbService {
	
	//=================================================================================================
	// members
	
	@Autowired
	private EntryRepository entryRepo;
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
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

}
