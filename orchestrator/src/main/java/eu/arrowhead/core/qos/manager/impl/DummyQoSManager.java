package eu.arrowhead.core.qos.manager.impl;

import eu.arrowhead.api.orchestration.model.OrchestrationFormRequestDTO;
import eu.arrowhead.api.orchestration.model.OrchestrationResultDTO;
import eu.arrowhead.api.systemregistry.model.SystemRequestDTO;
import eu.arrowhead.core.qos.manager.QoSManager;

import java.util.List;

public class DummyQoSManager implements QoSManager {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Override
	public List<OrchestrationResultDTO> filterReservedProviders(final List<OrchestrationResultDTO> orList, final SystemRequestDTO requester) {
		return orList;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public List<OrchestrationResultDTO> reserveProvidersTemporarily(final List<OrchestrationResultDTO> orList, final SystemRequestDTO requester) {
		return orList;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public void confirmReservation(final OrchestrationResultDTO selected, final List<OrchestrationResultDTO> orList, final SystemRequestDTO requester) {
		// intentionally do nothing
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public List<OrchestrationResultDTO> verifyServices(final List<OrchestrationResultDTO> orList, final OrchestrationFormRequestDTO request) {
		return orList;
	}
}