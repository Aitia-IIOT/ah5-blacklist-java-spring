package eu.arrowhead.blacklist.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record BlacklistEntryDTO(
		String systemName,
		String createdBy,
		String revokedBy,
		String createdAt,
		String updatedAt,
		String reason,
		String expiresAt,
		boolean active) {

}
