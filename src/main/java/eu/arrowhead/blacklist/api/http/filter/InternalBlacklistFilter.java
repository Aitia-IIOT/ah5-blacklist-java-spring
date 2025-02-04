package eu.arrowhead.blacklist.api.http.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import eu.arrowhead.blacklist.BlacklistConstants;
import eu.arrowhead.blacklist.service.DiscoveryService;
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

	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
		log.debug("BlacklistFilter is active");
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

		String requestTarget = Utilities.stripEndSlash(request.getRequestURL().toString());
		if (requestTarget.endsWith(BlacklistConstants.HTTP_API_OP_LOOKUP)) {
			return true;
		}
		return false;
	}

	//-------------------------------------------------------------------------------------------------
	private boolean isSelfCheck(final HttpServletRequest request, final String systemName) {

		String requestTarget = Utilities.stripEndSlash(request.getRequestURL().toString());
		if (requestTarget.endsWith(BlacklistConstants.HTTP_API_OP_CHECK + SLASH + systemName)) {
			return true;
		}
		return false;
	}
}
