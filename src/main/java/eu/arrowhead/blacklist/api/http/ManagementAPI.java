package eu.arrowhead.blacklist.api.http;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.arrowhead.blacklist.BlacklistConstants;
import eu.arrowhead.blacklist.api.http.utils.IsSysopPreprocessor;
import eu.arrowhead.blacklist.api.http.utils.SystemNamePreprocessor;
import eu.arrowhead.blacklist.service.ManagementService;
import eu.arrowhead.blacklist.service.dto.BlacklistCreateListRequestDTO;
import eu.arrowhead.blacklist.service.dto.BlacklistEntryListResponseDTO;
import eu.arrowhead.blacklist.service.dto.BlacklistQueryRequestDTO;
import eu.arrowhead.common.Constants;
import eu.arrowhead.dto.ErrorMessageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(BlacklistConstants.HTTP_API_MANAGEMENT_PATH)
@SecurityRequirement(name = Constants.SECURITY_REQ_AUTHORIZATION)
public class ManagementAPI {
	
	//=================================================================================================
	// members

	@Autowired
	private ManagementService managementService;
	
	@Autowired
	private SystemNamePreprocessor systemNamepreprocessor;
	
	@Autowired
	private IsSysopPreprocessor isSysopPreprocessor;

	private final Logger logger = LogManager.getLogger(this.getClass());

	//=================================================================================================
	// methods

	// query
	//POST /mgmt/query BlacklistQueryRequestDTO -> BlacklistEntryListResponseDTO
	//-------------------------------------------------------------------------------------------------
	@Operation(summary = "Returns the blacklist entries that match various filters specified by the requester")
	@ApiResponses(value = {
			@ApiResponse(responseCode = Constants.HTTP_STATUS_OK, description = Constants.SWAGGER_HTTP_200_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BlacklistEntryListResponseDTO.class)) }),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_BAD_REQUEST, description = Constants.SWAGGER_HTTP_400_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTO.class)) }),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_UNAUTHORIZED, description = Constants.SWAGGER_HTTP_401_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTO.class)) }),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_FORBIDDEN, description = Constants.SWAGGER_HTTP_403_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTO.class)) }),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_INTERNAL_SERVER_ERROR, description = Constants.SWAGGER_HTTP_500_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTO.class)) })
	})
	@PostMapping(path = BlacklistConstants.HTTP_API_QUERY_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BlacklistEntryListResponseDTO query(@RequestBody final BlacklistQueryRequestDTO dto) {
		logger.debug("query started");

		final String origin = HttpMethod.POST.name() + " " + BlacklistConstants.HTTP_API_MANAGEMENT_PATH + BlacklistConstants.HTTP_API_QUERY_PATH;

		return managementService.query(dto, origin);
	}
	
	// create
	// POST /mgmt/create BlacklistCreateListRequestDTO -> BlacklistEntryListResponseDTO
	//-------------------------------------------------------------------------------------------------
	@Operation(summary = "Adds new entries to the blacklist.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = Constants.HTTP_STATUS_OK, description = Constants.SWAGGER_HTTP_201_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BlacklistEntryListResponseDTO.class)) }),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_BAD_REQUEST, description = Constants.SWAGGER_HTTP_400_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTO.class)) }),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_UNAUTHORIZED, description = Constants.SWAGGER_HTTP_401_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTO.class)) }),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_FORBIDDEN, description = Constants.SWAGGER_HTTP_403_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTO.class)) }),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_INTERNAL_SERVER_ERROR, description = Constants.SWAGGER_HTTP_500_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTO.class)) })
	})
	@PostMapping(path = BlacklistConstants.HTTP_API_CREATE_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BlacklistEntryListResponseDTO create(final HttpServletRequest httpServletRequest, @RequestBody final BlacklistCreateListRequestDTO dto) {
		logger.debug("Create started");

		final String origin = HttpMethod.POST.name() + " " + BlacklistConstants.HTTP_API_MANAGEMENT_PATH + BlacklistConstants.HTTP_API_CREATE_PATH;
		final String identifiedName = systemNamepreprocessor.process(httpServletRequest, origin);
		
		return managementService.create(dto, origin, identifiedName);
	}
	
	// remove
	// DELETE /mgmt/remove String list as query param
	//-------------------------------------------------------------------------------------------------
	@Operation(summary = "Inactivates the entries that apply to the systems with the specified names")
	@ApiResponses(value = {
			@ApiResponse(responseCode = Constants.HTTP_STATUS_OK, description = Constants.SWAGGER_HTTP_200_MESSAGE),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_NO_CONTENT, description = Constants.SWAGGER_HTTP_204_MESSAGE),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_BAD_REQUEST, description = Constants.SWAGGER_HTTP_400_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTO.class)) }),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_UNAUTHORIZED, description = Constants.SWAGGER_HTTP_401_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTO.class)) }),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_FORBIDDEN, description = Constants.SWAGGER_HTTP_403_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTO.class)) }),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_INTERNAL_SERVER_ERROR, description = Constants.SWAGGER_HTTP_500_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTO.class)) })
	})
	@DeleteMapping(path = BlacklistConstants.HTTP_API_REMOVE_PATH)
	public ResponseEntity<Void> remove(final HttpServletRequest httpServletRequest, @PathVariable final List<String> systemNameList) {
		logger.debug("remove started");

		final String origin = HttpMethod.DELETE.name() + " " + BlacklistConstants.HTTP_API_MANAGEMENT_PATH + BlacklistConstants.HTTP_API_REMOVE_PATH;
		final boolean isSysop = isSysopPreprocessor.process(httpServletRequest, origin);
		final String revokerName = systemNamepreprocessor.process(httpServletRequest, origin);
		managementService.remove(systemNameList, isSysop, revokerName, origin);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
}
