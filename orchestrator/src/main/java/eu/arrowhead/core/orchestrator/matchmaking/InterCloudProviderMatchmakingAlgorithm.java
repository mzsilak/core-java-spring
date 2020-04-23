package eu.arrowhead.core.orchestrator.matchmaking;

import eu.arrowhead.api.orchestration.model.OrchestrationResponseDTO;

public interface InterCloudProviderMatchmakingAlgorithm {
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public OrchestrationResponseDTO doMatchmaking(final InterCloudProviderMatchmakingParameters params);
}