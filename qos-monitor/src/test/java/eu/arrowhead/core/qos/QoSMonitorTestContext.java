package eu.arrowhead.core.qos;

import eu.arrowhead.core.qos.database.service.QoSDBService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class QoSMonitorTestContext {

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Bean
	@Primary // This bean is primary only in test context
	public QoSDBService mockQoSDBService() {
		return Mockito.mock(QoSDBService.class);
	}
}
