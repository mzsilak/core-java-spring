package eu.arrowhead.core.qos.manager;

import eu.arrowhead.api.orchestration.model.OrchestrationResultDTO;

import java.util.Map;

public interface QoSVerifier {

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public boolean verify(final OrchestrationResultDTO result, final Map<String,String> qosRequirements, final Map<String,String> commands);
}