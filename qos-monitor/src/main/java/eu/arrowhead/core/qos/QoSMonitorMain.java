package eu.arrowhead.core.qos;

import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.CoreCommonConstants;
import eu.arrowhead.common.database.repository.RefreshableRepositoryImpl;
import eu.arrowhead.core.qos.measurement.properties.PingMeasurementProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableConfigurationProperties(PingMeasurementProperties.class)
@ComponentScan (CommonConstants.BASE_PACKAGE)
@EntityScan (CoreCommonConstants.DATABASE_ENTITY_PACKAGE)
@EnableJpaRepositories (basePackages = CoreCommonConstants.DATABASE_REPOSITORY_PACKAGE, repositoryBaseClass = RefreshableRepositoryImpl.class)
@EnableSwagger2
public class QoSMonitorMain {
	
	//=================================================================================================
	// members

	//-------------------------------------------------------------------------------------------------
	public static void main(final String[] args) {
		SpringApplication.run(QoSMonitorMain.class, args);
	}
}