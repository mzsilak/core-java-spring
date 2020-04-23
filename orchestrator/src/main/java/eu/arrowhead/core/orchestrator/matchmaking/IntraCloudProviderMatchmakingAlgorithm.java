package eu.arrowhead.core.orchestrator.matchmaking;

import eu.arrowhead.api.orchestration.model.OrchestrationResultDTO;

import java.util.List;

public interface IntraCloudProviderMatchmakingAlgorithm {

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public OrchestrationResultDTO doMatchmaking(final List<OrchestrationResultDTO> orList, final IntraCloudProviderMatchmakingParameters params);
}