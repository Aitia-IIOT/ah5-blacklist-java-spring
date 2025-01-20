package eu.arrowhead.blacklist.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import eu.arrowhead.blacklist.service.WhitelistService;
import eu.arrowhead.common.init.ApplicationInitListener;

@Component
public class BlacklistApplicationInitListener extends ApplicationInitListener {

	//=================================================================================================
	// members
	 @Autowired
	 private WhitelistService whitelistService;

	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	@Override
	protected void customInit(final ContextRefreshedEvent event) throws InterruptedException {
		logger.debug("customInit started...");

		// removing systems, that are on the whitelist from the database
		whitelistService.cleanDatabase("BlacklistApplicationInitListener.customInit");
	}
}
