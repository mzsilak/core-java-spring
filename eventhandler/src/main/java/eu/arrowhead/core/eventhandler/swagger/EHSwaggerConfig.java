package eu.arrowhead.core.eventhandler.swagger;

import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.swagger.DefaultSwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class EHSwaggerConfig extends DefaultSwaggerConfig {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public EHSwaggerConfig() {
		super(CommonConstants.CORE_SYSTEM_EVENT_HANDLER);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Bean
	public Docket customizeSwagger() {
		return configureSwaggerForCoreSystem(this.getClass().getPackageName());
	}
}