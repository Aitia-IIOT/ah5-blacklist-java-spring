package eu.arrowhead.blacklist.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.arrowhead.blacklist.jpa.entity.Entry;
import eu.arrowhead.blacklist.jpa.service.EntryDbService;
import eu.arrowhead.blacklist.service.dto.DTOConverter;
import eu.arrowhead.blacklist.service.validation.Validation;
import eu.arrowhead.common.exception.InternalServerError;
import eu.arrowhead.dto.BlacklistEntryListResponseDTO;

@Service
public class DiscoveryService {

	//=================================================================================================
	// members

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private EntryDbService dbService;

	@Autowired
	private DTOConverter dtoConverter;

	@Autowired
	private Validation validator;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public boolean check(final String systemName, final String origin) {
		logger.debug("check started...");

		final String normalizedName = validator.validateAndNormalizeSystemName(systemName, origin);

		try {
			return dbService.isActiveEntryForName(normalizedName);
		} catch (final InternalServerError ex) {
			throw new InternalServerError(ex.getMessage(), origin);
		}
	}

	//-------------------------------------------------------------------------------------------------
	public BlacklistEntryListResponseDTO lookup(final String requester, final String origin) {
		logger.debug("lookup started...");

		final String normalizedName = validator.validateAndNormalizeSystemName(requester, origin);

		try {
			final List<Entry> entries = dbService.getActiveEntriesForName(normalizedName);

			return dtoConverter.convertEntriesToBlacklistEntryListResponseDTO(entries, entries.size());
		} catch (final InternalServerError ex) {
			throw new InternalServerError(ex.getMessage(), origin);
		}
	}
}