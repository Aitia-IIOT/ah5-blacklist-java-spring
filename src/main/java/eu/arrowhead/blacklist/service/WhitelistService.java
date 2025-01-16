package eu.arrowhead.blacklist.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.arrowhead.blacklist.BlacklistConstants;
import eu.arrowhead.blacklist.jpa.service.EntryDbService;
import eu.arrowhead.common.exception.InvalidParameterException;

@Service
public class WhitelistService {
	
	//=================================================================================================
	// members
	
	@Value(BlacklistConstants.$WHITELIST_WD)
	private List<String> whitelist;
	
	@Autowired
	private EntryDbService dbService;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	// Throws exception, if the provided system name list contains element(s) that are on the whitelist
	public void checkWhitelist(final List<String> names, final String origin) {
		
		List<String> namesOnWhitelist = names.stream().filter(n -> whitelist.contains(n)).collect(Collectors.toList());
		
		if (!namesOnWhitelist.isEmpty()) {
			throw new InvalidParameterException("The following system names cannod be added, because they are on the whitelist: " + namesOnWhitelist.stream().collect(Collectors.joining(", ")), origin);
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	// Inactivates entries in the database, that refer to whitelisted systems
	public void cleanDatabase(final String origin) {
		
		dbService.unsetActiveByNameList(whitelist, BlacklistConstants.SYSTEM_NAME, origin);
	}
}
