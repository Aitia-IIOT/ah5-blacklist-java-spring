package eu.arrowhead.blacklist.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record BlacklistEntryListResponseDTO(
		List<BlacklistEntryDTO> entries,
		long count) {

}
