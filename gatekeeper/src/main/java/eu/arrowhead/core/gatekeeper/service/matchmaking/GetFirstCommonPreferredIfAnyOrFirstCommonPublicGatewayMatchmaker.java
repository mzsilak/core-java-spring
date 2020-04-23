package eu.arrowhead.core.gatekeeper.service.matchmaking;

import eu.arrowhead.common.database.entity.CloudGatewayRelay;
import eu.arrowhead.common.database.entity.Relay;
import eu.arrowhead.common.dto.internal.RelayRequestDTO;
import eu.arrowhead.core.gatekeeper.database.service.GatekeeperDBService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

public class GetFirstCommonPreferredIfAnyOrFirstCommonPublicGatewayMatchmaker implements RelayMatchmakingAlgorithm {
	
	//=================================================================================================
	// members
	private static final Logger logger = LogManager.getLogger(GetFirstCommonPreferredIfAnyOrFirstCommonPublicGatewayMatchmaker.class);
	
	@Autowired
	private GatekeeperDBService gatekeeperDBService;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Override
	public Relay doMatchmaking(final RelayMatchmakingParameters parameters) {
		logger.debug("GetFirstCommonPreferredIfAnyOrFirstCommonPublicGatewayMatchmaker.doMatchmaking started...");
		
		Assert.notNull(parameters, "RelayMatchmakingParameters is null");
		Assert.notNull(parameters.getCloud(), "Cloud is null");
		Assert.notNull(parameters.getPreferredGatewayRelays(), "Preferred relay list is null");
		Assert.notNull(parameters.getKnownGatewayRelays(), "Known relay list is null");
		
		if (!parameters.getCloud().getGatewayRelays().isEmpty() && !parameters.getPreferredGatewayRelays().isEmpty()) {			
			final Relay commonPreferredRelay = getFirstPreferredCommonRelay(parameters.getCloud().getGatewayRelays(), parameters.getPreferredGatewayRelays());
			if (commonPreferredRelay != null) {
				return commonPreferredRelay;
			}
		}
		
		final List<Relay> publicGatewayRelays = gatekeeperDBService.getPublicGatewayRelays();		
		if (!publicGatewayRelays.isEmpty() && !parameters.getKnownGatewayRelays().isEmpty()) {
			return getFirstPublicCommonRelay(publicGatewayRelays, parameters.getKnownGatewayRelays());			
		}
		
		return null;
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private Relay getFirstPreferredCommonRelay(final Set<CloudGatewayRelay> relayConnections, final List<RelayRequestDTO> relayRequests) {
		for (final CloudGatewayRelay relayConn : relayConnections) {
			for (final RelayRequestDTO requestedRelay : relayRequests) {				
				if (relayConn.getRelay().getAddress().equalsIgnoreCase(requestedRelay.getAddress()) && relayConn.getRelay().getPort() == requestedRelay.getPort()) {						
					return relayConn.getRelay();
				}						
			}
		}
		
		return null;
	}
	
	//-------------------------------------------------------------------------------------------------
	private Relay getFirstPublicCommonRelay(final List<Relay> relays, final List<RelayRequestDTO> relayRequests) {
		for (final Relay relay : relays) {
			for (final RelayRequestDTO requestedRelay : relayRequests) {				
				if (relay.getAddress().equalsIgnoreCase(requestedRelay.getAddress()) && relay.getPort() == requestedRelay.getPort()) {						
					return relay;
				}						
			}
		}
		
		return null;
	}
}