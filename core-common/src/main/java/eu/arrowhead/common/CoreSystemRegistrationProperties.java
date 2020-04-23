package eu.arrowhead.common;

import eu.arrowhead.common.core.CoreSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ServiceConfigurationError;

@Component
public class CoreSystemRegistrationProperties {

	//=================================================================================================
	// members
	
	private final Logger logger = LogManager.getLogger(CoreSystemRegistrationProperties.class);

	@Value(CoreCommonConstants.$CORE_SYSTEM_NAME)
	private String coreSystemName;
	
	private CoreSystem coreSystem;
	
	@Value(CommonConstants.$SERVICE_REGISTRY_ADDRESS_WD)
	private String serviceRegistryAddress;
	
	@Value(CommonConstants.$SERVICE_REGISTRY_PORT_WD)
	private int serviceRegistryPort;

	@Value(CoreCommonConstants.$SERVER_ADDRESS)
	private String coreSystemAddress;
	
	@Value(CoreCommonConstants.$SERVER_PORT)
	private int coreSystemPort;
	
	@Value(CoreCommonConstants.$DOMAIN_NAME)
	private String coreSystemDomainName;
	
	@Value(CoreCommonConstants.$DOMAIN_PORT)
	private int coreSystemDomainPort;

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@PostConstruct
	public void initCoreSystem() {
		logger.debug("initCoreSystem started...");
		
		if (Utilities.isEmpty(coreSystemName)) {
			throw new ServiceConfigurationError("Core system name is not specified in the application.properties file");
		}
		
		try {
			coreSystem = CoreSystem.valueOf(coreSystemName.trim().toUpperCase());
		} catch (final IllegalArgumentException ex) {
			throw new ServiceConfigurationError("Core system name " + coreSystemName + " is not recognized.", ex);
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public String getCoreSystemName() { return coreSystemName; }
	public CoreSystem getCoreSystem() { return coreSystem; }
	public String getServiceRegistryAddress() { return serviceRegistryAddress; }
	public int getServiceRegistryPort() { return serviceRegistryPort; }
	
	//-------------------------------------------------------------------------------------------------
	public String getCoreSystemDomainName() { 
		if (Utilities.isEmpty(coreSystemDomainName)) {
			return Utilities.isEmpty(coreSystemAddress) ? CommonConstants.LOCALHOST : coreSystemAddress;
		}
		
		return coreSystemDomainName;
	}
	
	//-------------------------------------------------------------------------------------------------
	public int getCoreSystemDomainPort() {
		if (coreSystemDomainPort > 0) {
			return coreSystemDomainPort;
		}
		
		if (coreSystemPort > 0) {
			return coreSystemPort;
		}
		
		throw new ServiceConfigurationError("Please specify a " + CoreCommonConstants.SERVER_PORT + " in the application.properties file.");
	}
}