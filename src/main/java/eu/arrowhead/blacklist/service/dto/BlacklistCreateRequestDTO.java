package eu.arrowhead.blacklist.service.dto;

public record BlacklistCreateRequestDTO(
		String systemName,
		String expiresAt,
		String reason) {

}
