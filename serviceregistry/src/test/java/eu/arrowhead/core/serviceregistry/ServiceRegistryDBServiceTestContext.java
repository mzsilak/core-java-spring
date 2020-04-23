package eu.arrowhead.core.serviceregistry;

import eu.arrowhead.core.serviceregistry.database.service.ServiceRegistryDBService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ServiceRegistryDBServiceTestContext {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Bean
	@Primary // This bean is primary only in test context
	public ServiceRegistryDBService mockServiceRegistryDBService() {
		return Mockito.mock(ServiceRegistryDBService.class);
	}
}