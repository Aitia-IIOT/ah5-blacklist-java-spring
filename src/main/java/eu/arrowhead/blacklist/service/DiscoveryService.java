package eu.arrowhead.blacklist.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.arrowhead.blacklist.jpa.service.EntryDbService;
import eu.arrowhead.blacklist.service.validation.Validation;
import eu.arrowhead.common.exception.InternalServerError;

@Service
public class DiscoveryService {
	
	//=================================================================================================
	// members
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private EntryDbService dbService;
	
	@Autowired
	private Validation validator;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public boolean check(final String systemName, final String origin) {
		logger.debug("check started...");
		
		validator.validateAndNormalizeSystemName(systemName, origin);
		
		try {
			return dbService.isActiveEntryForName(systemName, origin);
		} catch (final InternalServerError ex) {
			throw new InternalServerError(ex.getMessage(), origin);
		}
	}

}
