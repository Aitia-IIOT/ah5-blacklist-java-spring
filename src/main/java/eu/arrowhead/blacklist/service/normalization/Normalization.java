package eu.arrowhead.blacklist.service.normalization;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import eu.arrowhead.blacklist.service.dto.NormalizedBlacklistQueryRequestDTO;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.service.validation.name.NameNormalizer;
import eu.arrowhead.dto.BlacklistCreateListRequestDTO;
import eu.arrowhead.dto.BlacklistCreateRequestDTO;
import eu.arrowhead.dto.BlacklistQueryRequestDTO;
import eu.arrowhead.dto.enums.Mode;

@Service
public class Normalization {
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
	public NormalizedBlacklistQueryRequestDTO normalizeBlacklistQueryRequestDTO(final BlacklistQueryRequestDTO dto) {
		logger.debug("normalizeBlacklistQueryRequestDTO started...");

		if (dto == null) {
			return new NormalizedBlacklistQueryRequestDTO(null, null, null, null, null, null, null);
		}

		return new NormalizedBlacklistQueryRequestDTO(
				// pagination
				dto.pagination(), // no need to normalize, because it will happen in the getPageRequest method
				// system names
				Utilities.isEmpty(dto.systemNames()) ? null
					: dto.systemNames().stream().map(n -> nameNormalizer.normalize(n)).collect(Collectors.toList()),
				// mode
				Mode.valueOf(dto.mode().toUpperCase()),
				// issuers
				Utilities.isEmpty(dto.issuers()) ? null
						: dto.issuers().stream().map(n -> nameNormalizer.normalize(n)).collect(Collectors.toList()),
				// revokers
				Utilities.isEmpty(dto.revokers()) ? null
						: dto.revokers().stream().map(n -> nameNormalizer.normalize(n)).collect(Collectors.toList()),
				// reason
				dto.reason(),
				// alives at
				Utilities.isEmpty(dto.alivesAt()) ? "" : dto.alivesAt().trim());
	}

	//-------------------------------------------------------------------------------------------------
	public List<String> normalizeSystemNames(final List<String> names) {
		logger.debug("normalizeSystemNames started...");
		return names.stream().map(n -> nameNormalizer.normalize(n)).collect(Collectors.toList());
	}

	//-------------------------------------------------------------------------------------------------
	public String normalizeSystemName(final String name) {
		logger.debug("normalizeSystemName started...");
		return nameNormalizer.normalize(name);
	}

	//=================================================================================================
	// assistant methods

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
