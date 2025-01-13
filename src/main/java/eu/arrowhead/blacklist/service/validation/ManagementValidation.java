package eu.arrowhead.blacklist.service.validation;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.arrowhead.blacklist.BlacklistConstants;
import eu.arrowhead.blacklist.service.dto.BlacklistCreateListRequestDTO;
import eu.arrowhead.blacklist.service.dto.BlacklistCreateRequestDTO;
import eu.arrowhead.blacklist.service.normalization.ManagementNormalization;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.exception.InvalidParameterException;
import eu.arrowhead.common.service.validation.name.NameNormalizer;
import eu.arrowhead.common.service.validation.name.NameValidator;

@Service
public class ManagementValidation {
	
	//=================================================================================================
	// members
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private NameNormalizer nameNormalizer; //for checking duplications

	@Autowired
	private NameValidator nameValidator;
	
	@Autowired
	private ManagementNormalization managementNormalizer;
	
	//=================================================================================================
	// methods
	
	// VALIDATION
	
	//-------------------------------------------------------------------------------------------------
	public void validateBlacklistCreateListRequestDTO(final BlacklistCreateListRequestDTO dto, final String origin) {
		logger.debug("validateBlacklistCreateListRequestDTO started...");
		
		if (dto == null) {
			throw new InvalidParameterException("Request payload is missing", origin);
		}

		if (Utilities.isEmpty(dto.entities())) {
			throw new InvalidParameterException("Request payload is empty", origin);
		}
		
		final Set<String> names = new HashSet<>();
		for (BlacklistCreateRequestDTO entity : dto.entities()) {
			if (entity == null) {
				throw new InvalidParameterException("Entity list contains null element", origin);
			}

			// system name
			if (Utilities.isEmpty(entity.systemName())) {
				throw new InvalidParameterException("System name is empty", origin);
			}

			if (entity.systemName().length() > BlacklistConstants.SYSTEM_NAME_LENGTH) {
				throw new InvalidParameterException("System name is too long", origin);
			}

			if (names.contains(nameNormalizer.normalize(entity.systemName()))) {
				throw new InvalidParameterException("Duplicate system name: " + entity.systemName(), origin);
			}

			names.add(nameNormalizer.normalize(entity.systemName()));
			
			// expires at
			if (!Utilities.isEmpty(entity.expiresAt())) {
				ZonedDateTime expiresAt = null;
				try {
					expiresAt = Utilities.parseUTCStringToZonedDateTime(entity.expiresAt());
				} catch (final DateTimeException ex) {
					throw new InvalidParameterException("Expiration time has an invalid time format, UTC string expected (example: 2024-10-11T14:30:00Z)", origin);
				}
				if (Utilities.utcNow().isAfter(expiresAt)) {
					throw new InvalidParameterException("Expiration time is in the past", origin);
				}
			}
			
			// reason
			if (Utilities.isEmpty(entity.reason())) {
				throw new InvalidParameterException("You cannot blacklist a system without specifying the reason", origin);
			}
			
			if(entity.reason().length() > BlacklistConstants.REASON_LENGTH) {
				throw new InvalidParameterException("Reason is too long", origin);
			}
		}
	}
	
	// VALIDATION AND NORMALIZATION
	
	//-------------------------------------------------------------------------------------------------
	public BlacklistCreateListRequestDTO validateAndNormalizeBlacklistCreateListRequestDTO(final BlacklistCreateListRequestDTO dto, final String origin) {
		logger.debug("validateAndNormalizeBlacklistCreateListRequestDTO started...");
		
		validateBlacklistCreateListRequestDTO(dto, origin);
		
		final BlacklistCreateListRequestDTO normalized = managementNormalizer.normalizeBlacklistCreateListRequestDTO(dto);
		
		normalized.entities().forEach(e -> nameValidator.validateName(e.systemName()));
		
		return normalized;
	}
}
