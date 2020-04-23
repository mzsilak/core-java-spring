package eu.arrowhead.core.gatekeeper.service.matchmaking;

import eu.arrowhead.api.orchestration.model.OrchestrationResultDTO;

import java.util.List;

public interface ICNProviderMatchmakingAlgorithm {

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public OrchestrationResultDTO doMatchmaking(final List<OrchestrationResultDTO> orList, final ICNProviderMatchmakingParameters params);
}