package eu.arrowhead.blacklist.service.dto;
import java.util.List;

import eu.arrowhead.blacklist.service.dto.enums.Mode;
import eu.arrowhead.dto.PageDTO;

public record BlacklistQueryRequestDTO(
		PageDTO pagination,
		List<String> systemNames,
		Mode mode,
		List<String> issuers,
		List<String> revokers,
		String reason,
		String alivesAt) {

}
