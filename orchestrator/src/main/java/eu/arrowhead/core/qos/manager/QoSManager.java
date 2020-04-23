package eu.arrowhead.core.qos.manager;

import eu.arrowhead.api.orchestration.model.OrchestrationFormRequestDTO;
import eu.arrowhead.api.orchestration.model.OrchestrationResultDTO;
import eu.arrowhead.api.systemregistry.model.SystemRequestDTO;

import java.util.List;

public interface QoSManager {

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	// verification-related
	public List<OrchestrationResultDTO> verifyServices(final List<OrchestrationResultDTO> orList, final OrchestrationFormRequestDTO request);
	
	//-------------------------------------------------------------------------------------------------
	// reservation-related
	public List<OrchestrationResultDTO> filterReservedProviders(final List<OrchestrationResultDTO> orList, final SystemRequestDTO requester);
	public List<OrchestrationResultDTO> reserveProvidersTemporarily(final List<OrchestrationResultDTO> orList, final SystemRequestDTO requester); // returns with the temp locked results
	public void confirmReservation(final OrchestrationResultDTO selected, final List<OrchestrationResultDTO> orList, final SystemRequestDTO requester);
	
}