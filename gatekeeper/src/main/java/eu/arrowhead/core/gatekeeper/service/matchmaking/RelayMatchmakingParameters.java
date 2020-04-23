package eu.arrowhead.core.gatekeeper.service.matchmaking;

import eu.arrowhead.common.database.entity.Cloud;
import eu.arrowhead.common.dto.internal.RelayRequestDTO;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class RelayMatchmakingParameters {

	//=================================================================================================
	// members
	
	protected Cloud cloud;
	protected List<RelayRequestDTO> preferredGatewayRelays = new ArrayList<>();
	protected List<RelayRequestDTO> knownGatewayRelays = new ArrayList<>();
	protected long randomSeed = System.currentTimeMillis();
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public RelayMatchmakingParameters(final Cloud cloud) {
		Assert.notNull(cloud, "cloud is null.");
		this.cloud = cloud;
	}

	//-------------------------------------------------------------------------------------------------
	public Cloud getCloud() { return cloud; }
	public long getRandomSeed() { return randomSeed; }
	public List<RelayRequestDTO> getPreferredGatewayRelays() { return preferredGatewayRelays; }
	public List<RelayRequestDTO> getKnownGatewayRelays() { return knownGatewayRelays; }

	//-------------------------------------------------------------------------------------------------
	public void setCloud(final Cloud cloud) { this.cloud = cloud; }
	public void setRandomSeed(final long randomSeed) { this.randomSeed = randomSeed; }
	public void setPreferredGatewayRelays(final List<RelayRequestDTO> preferredGatewayRelays) { this.preferredGatewayRelays = preferredGatewayRelays; }
	public void setKnownGatewayRelays(final List<RelayRequestDTO> knownGatewayRelays) { this.knownGatewayRelays = knownGatewayRelays; }	
}