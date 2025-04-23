package eu.arrowhead.blacklist.api.http.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import eu.arrowhead.blacklist.BlacklistConstants;
import eu.arrowhead.blacklist.service.DiscoveryService;
import eu.arrowhead.blacklist.service.normalization.Normalization;
import eu.arrowhead.common.Constants;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.exception.ForbiddenException;
import eu.arrowhead.common.http.HttpUtilities;
import eu.arrowhead.common.http.filter.ArrowheadFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Constants.REQUEST_FILTER_ORDER_AUTHORIZATION_BLACKLIST)
public class InternalBlacklistFilter extends ArrowheadFilter {

	//=================================================================================================
	// members

	@Autowired
	private DiscoveryService discoveryService;

	private static final String SLASH = "/";

	@Autowired
	private Normalization normalizer;

	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
		log.debug("InternalBlacklistFilter is active");
		final String origin = request.getMethod() + " " + request.getRequestURL().substring(request.getRequestURL().indexOf(BlacklistConstants.HTTP_API_BASE_PATH));

		final String systemName = request.getAttribute(Constants.HTTP_ATTR_ARROWHEAD_AUTHENTICATED_SYSTEM).toString();

		if (!HttpUtilities.isSysop(request, origin)
				&& !isSelfCheck(request, systemName)
				&& !isLookup(request)
				&& discoveryService.check(systemName, origin)) {
				throw new ForbiddenException(systemName + " system is blacklisted!");
		}
		chain.doFilter(request, response);
	}

	//-------------------------------------------------------------------------------------------------
	private boolean isLookup(final HttpServletRequest request) {

		final String requestTarget = Utilities.stripEndSlash(request.getRequestURL().toString());
		if (requestTarget.endsWith(BlacklistConstants.HTTP_API_BASE_PATH + BlacklistConstants.HTTP_API_OP_LOOKUP)) {
			return true;
		}
		return false;
	}

	//-------------------------------------------------------------------------------------------------
	private boolean isSelfCheck(final HttpServletRequest request, final String systemName) {

		final String requestTarget = Utilities.stripEndSlash(request.getRequestURL().toString());

		if (!requestTarget.contains("/")) {
			return false;
		}

		// rebuilding the request target so it contains a normalized system name as path parameter
		final String path = requestTarget.substring(0, requestTarget.lastIndexOf('/') + 1);
		final String pathParam = requestTarget.substring(requestTarget.lastIndexOf('/') + 1);
		final String normalizedRequestTarget = path + normalizer.normalizeSystemName(pathParam);

		return normalizedRequestTarget.endsWith(BlacklistConstants.HTTP_API_BASE_PATH + BlacklistConstants.HTTP_API_OP_CHECK + SLASH + normalizer.normalizeSystemName(systemName));
	}
}
