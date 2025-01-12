package eu.arrowhead.blacklist.service.dto;

import java.util.List;

public record BlacklistCreateListRequestDTO(
		List<BlacklistCreateRequestDTO> entities
		) {

}
