package eu.arrowhead.core.gatekeeper;

import eu.arrowhead.common.ApplicationInitListener;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.CoreCommonConstants;
import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.core.CoreSystemService;
import eu.arrowhead.core.gatekeeper.quartz.subscriber.RelaySubscriberDataContainer;
import eu.arrowhead.core.gatekeeper.relay.GatekeeperRelayClientFactory;
import eu.arrowhead.core.gatekeeper.relay.GatekeeperRelayClientUsingCachedSessions;
import eu.arrowhead.core.gatekeeper.service.matchmaking.GetRandomAndDedicatedIfAnyGatekeeperMatchmaker;
import eu.arrowhead.core.gatekeeper.service.matchmaking.GetRandomCommonPreferredIfAnyOrRandomCommonPublicGatewayMatchmaker;
import eu.arrowhead.core.gatekeeper.service.matchmaking.ICNProviderMatchmakingAlgorithm;
import eu.arrowhead.core.gatekeeper.service.matchmaking.RandomICNProviderMatchmaker;
import eu.arrowhead.core.gatekeeper.service.matchmaking.RelayMatchmakingAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.jms.Session;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;

@Component
public class GatekeeperApplicationInitListener extends ApplicationInitListener {
	
	//=================================================================================================
	// members

	@Value(CommonConstants.$HTTP_CLIENT_SOCKET_TIMEOUT_WD)
	private long timeout;
	
	@Value(CoreCommonConstants.$GATEKEEPER_IS_GATEWAY_PRESENT_WD)
	private boolean gatewayIsPresent;
		
	@Value(CoreCommonConstants.$GATEKEEPER_IS_GATEWAY_MANDATORY_WD)
	private boolean gatewayIsMandatory;
	
	@Autowired
	private SSLProperties sslProps;
	
	private GatekeeperRelayClientUsingCachedSessions gatekeeperRelayClientWithCache;
	
	private RelaySubscriberDataContainer relaySubscriberDataContainer; // initialization is on demand to avoid circular dependencies 
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@Bean(CoreCommonConstants.GATEKEEPER_MATCHMAKER)
	public RelayMatchmakingAlgorithm getGatekeeperRelayMatchmakingAlgorithm() {
		return new GetRandomAndDedicatedIfAnyGatekeeperMatchmaker();
	}
	
	//-------------------------------------------------------------------------------------------------
	@Bean(CoreCommonConstants.GATEWAY_MATCHMAKER)
	public RelayMatchmakingAlgorithm getGatewayRelayMatchmakingAlgorithm() {
		return new GetRandomCommonPreferredIfAnyOrRandomCommonPublicGatewayMatchmaker();
	}
	
	//-------------------------------------------------------------------------------------------------
	@Bean(CoreCommonConstants.ICN_PROVIDER_MATCHMAKER)
	public ICNProviderMatchmakingAlgorithm getICNProviderMatchmaker() {
		return new RandomICNProviderMatchmaker();
	}

	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	@Override
	protected List<CoreSystemService> getRequiredCoreSystemServiceUris() {
		if (gatewayIsPresent) {
			return List.of(CoreSystemService.AUTH_CONTROL_INTER_SERVICE, CoreSystemService.ORCHESTRATION_SERVICE, CoreSystemService.GATEWAY_CONSUMER_SERVICE,
						   CoreSystemService.GATEWAY_PROVIDER_SERVICE, CoreSystemService.GATEWAY_PUBLIC_KEY_SERVICE); 
		}
		
		return List.of(CoreSystemService.AUTH_CONTROL_INTER_SERVICE, CoreSystemService.ORCHESTRATION_SERVICE); 
	}
		
	//-------------------------------------------------------------------------------------------------
	@Override
	protected void customInit(final ContextRefreshedEvent event) {
		logger.debug("customInit started...");

		if (!sslProperties.isSslEnabled()) {
			throw new ServiceConfigurationError("Gatekeeper can only started in SECURE mode!");
		}
		
		if (gatewayIsMandatory && !gatewayIsPresent) {
			throw new ServiceConfigurationError("Gatekeeper can't start with 'gateway_is_present=false' property when the 'gateway_is_mandatory' property is true!");
		}
		
		initializeGatekeeperRelayClient(event.getApplicationContext());
		relaySubscriberDataContainer = event.getApplicationContext().getBean(RelaySubscriberDataContainer.class);
		relaySubscriberDataContainer.init();
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	protected void customDestroy() {
		// close connections using listening on advertisement topic
		relaySubscriberDataContainer.close();
		
		// close connections used by web services and gatekeeper tasks
		for (final Session session : gatekeeperRelayClientWithCache.getCachedSessions()) {
			gatekeeperRelayClientWithCache.closeConnection(session);
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	private void initializeGatekeeperRelayClient(final ApplicationContext appContext) {
		@SuppressWarnings("unchecked")
		final Map<String,Object> context = appContext.getBean(CommonConstants.ARROWHEAD_CONTEXT, Map.class);
		final String serverCN = (String) context.get(CommonConstants.SERVER_COMMON_NAME);
		final PublicKey publicKey = (PublicKey) context.get(CommonConstants.SERVER_PUBLIC_KEY);
		final PrivateKey privateKey = (PrivateKey) context.get(CommonConstants.SERVER_PRIVATE_KEY);

		this.gatekeeperRelayClientWithCache = (GatekeeperRelayClientUsingCachedSessions) GatekeeperRelayClientFactory.createGatekeeperRelayClient(serverCN, publicKey, privateKey, sslProps, timeout,
																																				  true);
	}
}