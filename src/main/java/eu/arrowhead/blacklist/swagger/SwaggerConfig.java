package eu.arrowhead.blacklist.swagger;

import org.springframework.context.annotation.Configuration;
import eu.arrowhead.common.swagger.DefaultSwaggerConfig;

@Configuration
public class SwaggerConfig extends DefaultSwaggerConfig {

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public SwaggerConfig() {
		super("Arrowhead Blacklist");
	}
}