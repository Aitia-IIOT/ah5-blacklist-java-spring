package eu.arrowhead.blacklist.api.http.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import eu.arrowhead.blacklist.BlacklistConstants;
import eu.arrowhead.blacklist.BlacklistSystemInfo;
import eu.arrowhead.blacklist.service.DiscoveryService;
import eu.arrowhead.common.Constants;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.exception.ForbiddenException;
import eu.arrowhead.common.http.filter.ArrowheadFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@ConditionalOnProperty(name = Constants.ENABLE_BLACKLIST_FILTER, matchIfMissing = true)
@Order(Constants.REQUEST_FILTER_ORDER_AUTHORIZATION_BLACKLIST)
public class BlacklistFilter extends ArrowheadFilter {
	
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
		
		// if requester is sysop, no need for check
		final boolean isSysop = Boolean.valueOf(request.getAttribute(Constants.HTTP_ATTR_ARROWHEAD_SYSOP_REQUEST).toString());
		
		// if request is self check, no need for check
		boolean isSelfCheck = false;
		final String systemName = request.getAttribute(Constants.HTTP_ATTR_ARROWHEAD_AUTHENTICATED_SYSTEM).toString();
		String requestTarget = Utilities.stripEndSlash(request.getRequestURL().toString());
		if (requestTarget.endsWith(BlacklistConstants.HTTP_API_OP_CHECK + SLASH + systemName)) {
			isSelfCheck = true;
		}
		
		if (!isSysop && !isSelfCheck) {
			try {
				if (discoveryService.check(systemName, "BlacklistFilter.java")) {
					throw new ForbiddenException(systemName + " system is blacklisted!");
				}
			} catch (ArrowheadException ex) {
				throw ex;
			}
		}
		chain.doFilter(request, response);
	}
}
