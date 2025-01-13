package eu.arrowhead.blacklist.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import eu.arrowhead.blacklist.jpa.entity.Entry;
import eu.arrowhead.blacklist.jpa.service.EntryDbService;
import eu.arrowhead.blacklist.service.dto.BlacklistCreateListRequestDTO;
import eu.arrowhead.blacklist.service.dto.BlacklistCreateRequestDTO;
import eu.arrowhead.blacklist.service.dto.BlacklistEntryListResponseDTO;
import eu.arrowhead.blacklist.service.dto.BlacklistQueryRequestDTO;
import eu.arrowhead.blacklist.service.dto.DTOConverter;
import eu.arrowhead.blacklist.service.validation.ManagementValidation;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.exception.InvalidParameterException;

@Service
public class ManagementService {
	
	//=================================================================================================
	// members
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private ManagementValidation managementValidator;
	
	@Autowired
	private DTOConverter dtoConverter;
	
	@Autowired
	private EntryDbService dbService;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public BlacklistEntryListResponseDTO query(final BlacklistQueryRequestDTO dto, final String origin) {
		logger.debug("ManagementService query started...");
		
		//TODO 
		
		return null;
	}
	
	//-------------------------------------------------------------------------------------------------
	public BlacklistEntryListResponseDTO create(final BlacklistCreateListRequestDTO dto, final String origin, final String requesterName) {
		logger.debug("ManagementService create started...");
		
		BlacklistCreateListRequestDTO normalized = managementValidator.validateAndNormalizeBlacklistCreateListRequestDTO(dto, origin);
		checkSelfBlacklisting(dto.entities(), requesterName, origin);
		
		List<Entry> createdEnties = dbService.createBulk(normalized.entities(), requesterName);
		return dtoConverter.convertEntryListToBlacklistEntryListResponseDTO(createdEnties);
	}
	
	//-------------------------------------------------------------------------------------------------
	public void remove(final List<String> systemNameList, final String origin) {
		logger.debug("ManagementService remove started...");
		
		//TODO
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private void checkSelfBlacklisting(final List<BlacklistCreateRequestDTO> candidates, final String requesterName, final String origin) {
		Assert.notNull(candidates, "Candidates is null!");
		Assert.isTrue(!Utilities.containsNull(candidates), "Candidates contains null element!");
		
		for (BlacklistCreateRequestDTO candidate : candidates) {
			if (candidate.systemName().equals(requesterName)) {
				throw new InvalidParameterException("It is not allowed to add yourself to the blacklist!", origin);
			}
		}
		
	}
}
