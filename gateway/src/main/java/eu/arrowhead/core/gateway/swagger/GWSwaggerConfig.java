package eu.arrowhead.core.gateway.swagger;

import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.swagger.DefaultSwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class GWSwaggerConfig extends DefaultSwaggerConfig {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public GWSwaggerConfig() {
		super(CommonConstants.CORE_SYSTEM_GATEWAY);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Bean
	public Docket customizeSwagger() {
		return configureSwaggerForCoreSystem(this.getClass().getPackageName());
	}
}