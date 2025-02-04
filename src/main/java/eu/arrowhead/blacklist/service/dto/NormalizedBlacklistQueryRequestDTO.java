package eu.arrowhead.blacklist.service.dto;

import java.util.List;

import eu.arrowhead.dto.PageDTO;
import eu.arrowhead.dto.enums.Mode;

public record NormalizedBlacklistQueryRequestDTO(
		PageDTO pagination,
		List<String> systemNames,
		Mode mode,
		List<String> issuers,
		List<String> revokers,
		String reason,
		String alivesAt) {
}
