package eu.arrowhead.blacklist.service.normalization;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import eu.arrowhead.blacklist.service.dto.BlacklistCreateListRequestDTO;
import eu.arrowhead.blacklist.service.dto.BlacklistCreateRequestDTO;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.service.validation.name.NameNormalizer;

@Service
public class ManagementNormalization {
	//=================================================================================================
	// members
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private NameNormalizer nameNormalizer;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public BlacklistCreateListRequestDTO normalizeBlacklistCreateListRequestDTO(final BlacklistCreateListRequestDTO dto) {
		logger.debug("normalizeBlacklistCreateListRequestDTO started...");
		Assert.notNull(dto, "BlacklistCreateListRequestDTO is null");
		
		return new BlacklistCreateListRequestDTO(dto.entities().stream().map(e -> normalizeBlacklistCreateRequestDTO(e)).collect(Collectors.toList()));
	}
	
	//-------------------------------------------------------------------------------------------------
	private BlacklistCreateRequestDTO normalizeBlacklistCreateRequestDTO(final BlacklistCreateRequestDTO candidate) {
		logger.debug("normalizeBlacklistCreateRequestDTO started...");
		Assert.notNull(candidate, "BlacklistCreateRequestDTO is null");
		
		return new BlacklistCreateRequestDTO(
				// system name
				nameNormalizer.normalize(candidate.systemName()),
				// expires at
				Utilities.isEmpty(candidate.expiresAt()) ? "" : candidate.expiresAt().trim(),
				// reason
				candidate.reason());
	}
}
