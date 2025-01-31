package eu.arrowhead.blacklist.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.arrowhead.blacklist.BlacklistConstants;
import eu.arrowhead.blacklist.jpa.service.EntryDbService;
import eu.arrowhead.blacklist.service.normalization.Normalization;
import eu.arrowhead.common.exception.InvalidParameterException;
import jakarta.annotation.PostConstruct;

@Service
public class WhitelistService {

	//=================================================================================================
	// members

	@Value(BlacklistConstants.$WHITELIST)
	private List<String> whitelist;

	private List<String> normalizedWhitelist;

	@Autowired
	private EntryDbService dbService;

	@Autowired
	private Normalization normalizer;

	private final Logger logger = LogManager.getLogger(this.getClass());

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	// Throws exception, if the provided system name list contains element(s) that are on the whitelist
	public void checkWhitelist(final List<String> names, final String origin) {
		logger.debug("checkWhitelist started");

		final List<String> namesOnWhitelist = names.stream().filter(n -> normalizedWhitelist.contains(n)).collect(Collectors.toList());

		if (!namesOnWhitelist.isEmpty()) {
			throw new InvalidParameterException("The following system names cannot be added, because they are on the whitelist: " + String.join(", ", namesOnWhitelist), origin);
		}
	}

	//-------------------------------------------------------------------------------------------------
	// Inactivates entries in the database, that refer to whitelisted systems
	public void cleanDatabase() {
		logger.debug("cleanDatabase started");

		dbService.inactivateNameList(normalizedWhitelist, BlacklistConstants.SYSTEM_NAME);
	}

	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	@PostConstruct
	private void init() {
		normalizedWhitelist = normalizer.normalizeSystemNames(whitelist);
	}
}
