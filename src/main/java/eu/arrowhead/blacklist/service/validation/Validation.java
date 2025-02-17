package eu.arrowhead.blacklist.service.validation;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.arrowhead.blacklist.BlacklistConstants;
import eu.arrowhead.blacklist.jpa.entity.Entry;
import eu.arrowhead.blacklist.service.dto.NormalizedBlacklistQueryRequestDTO;
import eu.arrowhead.blacklist.service.normalization.Normalization;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.exception.InvalidParameterException;
import eu.arrowhead.common.service.validation.PageValidator;
import eu.arrowhead.common.service.validation.name.NameNormalizer;
import eu.arrowhead.common.service.validation.name.NameValidator;
import eu.arrowhead.dto.BlacklistCreateListRequestDTO;
import eu.arrowhead.dto.BlacklistCreateRequestDTO;
import eu.arrowhead.dto.BlacklistQueryRequestDTO;
import eu.arrowhead.dto.enums.Mode;

@Service
public class Validation {

	//=================================================================================================
	// members

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private NameNormalizer nameNormalizer; // for checking duplications

	@Autowired
	private NameValidator nameValidator;

	@Autowired
	private PageValidator pageValidator;

	@Autowired
	private Normalization normalizer;

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
		for (final BlacklistCreateRequestDTO entity : dto.entities()) {
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
				throw new InvalidParameterException("Duplicated system name: " + entity.systemName(), origin);
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

			if (entity.reason().length() > BlacklistConstants.REASON_LENGTH) {
				throw new InvalidParameterException("Reason is too long", origin);
			}
		}
	}

	//-------------------------------------------------------------------------------------------------
	public void validateBlacklistQueryRequestDTO(final BlacklistQueryRequestDTO dto, final String origin) {
		logger.debug("validateBlacklistQueryRequestDTO started...");

		if (dto != null) {
			// pagination
			pageValidator.validatePageParameter(dto.pagination(), Entry.SORTABLE_FIELDS_BY, origin);
			// system names
			if (!Utilities.isEmpty(dto.systemNames()) && Utilities.containsNullOrEmpty(dto.systemNames())) {
				throw new InvalidParameterException("System name list contains null or empty element", origin);
			}
			// mode
			if (dto.mode() != null) {
				try {
					Mode.valueOf(dto.mode().trim().toUpperCase());
				} catch (final IllegalArgumentException ex) {
					// throwing exception containing the possible values of mode
					final List<String> possibleValues = new ArrayList<>(Mode.values().length);
					for (final Mode mode : Mode.values()) {
						possibleValues.add(mode.toString());
					}
					throw new InvalidParameterException("Mode is invalid. Possible values: " + String.join(", ", possibleValues));
				}
			}
			// issuers
			if (!Utilities.isEmpty(dto.issuers()) && Utilities.containsNullOrEmpty(dto.issuers())) {
				throw new InvalidParameterException("Issuer name list contains null or empty element", origin);
			}
			// revokers
			if (!Utilities.isEmpty(dto.revokers()) && Utilities.containsNullOrEmpty(dto.revokers())) {
				throw new InvalidParameterException("Revoker name list contains null or empty element", origin);
			}
			// reason -> no need to validate
			// alivesAt
			if (!Utilities.isEmpty(dto.alivesAt())) {
				ZonedDateTime alivesAt = null;
				try {
					alivesAt = Utilities.parseUTCStringToZonedDateTime(dto.alivesAt());
				} catch (final DateTimeException ex) {
					throw new InvalidParameterException("Alives at date has an invalid time format, UTC string expected (example: 2024-10-11T14:30:00Z)", origin);
				}
			}
		}
	}

	//-------------------------------------------------------------------------------------------------
	public void validateSystemNameList(final List<String> names, final String origin) {
		logger.debug("validateSystemNameList started...");

		if (Utilities.isEmpty(names)) {
			throw new InvalidParameterException("System name list is missing or empty", origin);
		}
		if (Utilities.containsNullOrEmpty(names)) {
			throw new InvalidParameterException("System name list contains null or empty element!", origin);

		}
	}

	//-------------------------------------------------------------------------------------------------
	public void validateSystemName(final String name, final String origin) {
		logger.debug("validateSystemName started...");

		if (Utilities.isEmpty(name)) {
			throw new InvalidParameterException("System name is empty", origin);
		}
	}

	// VALIDATION AND NORMALIZATION

	//-------------------------------------------------------------------------------------------------
	public BlacklistCreateListRequestDTO validateAndNormalizeBlacklistCreateListRequestDTO(final BlacklistCreateListRequestDTO dto, final String origin) {
		logger.debug("validateAndNormalizeBlacklistCreateListRequestDTO started...");

		validateBlacklistCreateListRequestDTO(dto, origin);

		final BlacklistCreateListRequestDTO normalized = normalizer.normalizeBlacklistCreateListRequestDTO(dto);

		normalized.entities().forEach(e -> nameValidator.validateName(e.systemName()));

		return normalized;
	}

	//-------------------------------------------------------------------------------------------------
	public NormalizedBlacklistQueryRequestDTO validateAndNormalizeBlacklistQueryRequestDTO(final BlacklistQueryRequestDTO dto, final String origin) {
		logger.debug("validateAndNormalizeBlacklistQueryRequestDTO started...");

		validateBlacklistQueryRequestDTO(dto, origin);
		final NormalizedBlacklistQueryRequestDTO normalized = normalizer.normalizeBlacklistQueryRequestDTO(dto);
		return normalized;

	}

	//-------------------------------------------------------------------------------------------------
	public List<String> validateAndNormalizeSystemNameList(final List<String> names, final String origin) {
		logger.debug("validateAndNormalizeSystemNameList started...");

		validateSystemNameList(names, origin);
		final List<String> normalizedNames = normalizer.normalizeSystemNames(names);
		return normalizedNames;
	}

	//-------------------------------------------------------------------------------------------------
	public String validateAndNormalizeSystemName(final String name, final String origin) {
		logger.debug("validateAndNormalizeSystemName");
		final String normalizedName = normalizer.normalizeSystemName(name);
		return normalizedName;
	}

}
