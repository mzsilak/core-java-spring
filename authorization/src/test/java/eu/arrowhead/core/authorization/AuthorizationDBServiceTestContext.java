package eu.arrowhead.core.authorization;

import eu.arrowhead.core.authorization.database.service.AuthorizationDBService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AuthorizationDBServiceTestContext {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Bean
	@Primary // This bean is primary only in test context
	public AuthorizationDBService mockAuthorizationDBService() {
		return Mockito.mock(AuthorizationDBService.class);
	}	
}