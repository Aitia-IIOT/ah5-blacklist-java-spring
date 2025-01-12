package eu.arrowhead.blacklist.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import eu.arrowhead.blacklist.service.dto.BlacklistCreateListRequestDTO;
import eu.arrowhead.blacklist.service.dto.BlacklistEntryListResponseDTO;
import eu.arrowhead.blacklist.service.dto.BlacklistQueryRequestDTO;

@Service
public class ManagementService {
	//=================================================================================================
	// members
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public BlacklistEntryListResponseDTO query(final BlacklistQueryRequestDTO dto, final String origin) {
		logger.debug("ManagementService query started...");
		
		//TODO 
		
		return null;
	}
	
	//-------------------------------------------------------------------------------------------------
	public BlacklistEntryListResponseDTO create(final BlacklistCreateListRequestDTO dto, final String origin) {
		logger.debug("ManagementService create started...");
		
		//TODO
		
		return null;
	}
	
	//-------------------------------------------------------------------------------------------------
	public void remove(final List<String> systemNameList, final String origin) {
		logger.debug("ManagementService remove started...");
		
		//TODO
	}
}
