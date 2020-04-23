package eu.arrowhead.core.gatekeeper.service.matchmaking;

import eu.arrowhead.common.database.entity.Relay;
import org.springframework.util.Assert;

public class GetFirstGatekeeperMatchmaker implements RelayMatchmakingAlgorithm {

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	/** 
	 * This algorithm returns the first Gatekeeper Relay, no matter if it is GATEKEEPER_RELAY or GENERAL_RELAY type
	 */
	@Override
	public Relay doMatchmaking(final RelayMatchmakingParameters parameters) {
		Assert.notNull(parameters, "RelayMatchmakingParameters is null");
		Assert.notNull(parameters.getCloud(), "Cloud is null");
		Assert.isTrue(parameters.getCloud().getGatekeeperRelays() != null && !parameters.getCloud().getGatekeeperRelays().isEmpty(), "GatekeeperRelaysList is null or empty.");
		
		return parameters.getCloud().getGatekeeperRelays().iterator().next().getRelay();
	}
}