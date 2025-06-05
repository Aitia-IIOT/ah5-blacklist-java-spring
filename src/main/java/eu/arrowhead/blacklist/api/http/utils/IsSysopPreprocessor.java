package eu.arrowhead.blacklist.api.http.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import eu.arrowhead.common.exception.InvalidParameterException;
import eu.arrowhead.common.http.HttpUtilities;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class IsSysopPreprocessor {

	//=================================================================================================
	// members

	private final Logger logger = LogManager.getLogger(this.getClass());

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public boolean process(final HttpServletRequest request, final String origin) throws InvalidParameterException {
		logger.debug("isSysop process started...");

		return HttpUtilities.isSysop(request, origin);
	}
}