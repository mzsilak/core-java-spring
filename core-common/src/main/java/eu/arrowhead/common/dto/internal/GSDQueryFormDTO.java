package eu.arrowhead.common.dto.internal;

import eu.arrowhead.api.cloud.model.CloudRequestDTO;
import eu.arrowhead.api.serviceregistry.model.ServiceQueryFormDTO;

import java.io.Serializable;
import java.util.List;

public class GSDQueryFormDTO implements Serializable {

	//=================================================================================================
	// members

	private static final long serialVersionUID = -6651144276576501218L;
	
	private ServiceQueryFormDTO requestedService;
	private List<CloudRequestDTO> preferredClouds;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------	
	public GSDQueryFormDTO() {}
	
	//-------------------------------------------------------------------------------------------------
	public GSDQueryFormDTO(final ServiceQueryFormDTO requestedService, final List<CloudRequestDTO> preferredClouds) {
		this.requestedService = requestedService;
		this.preferredClouds = preferredClouds;
	}

	//-------------------------------------------------------------------------------------------------
	public ServiceQueryFormDTO getRequestedService() { return requestedService; }
	public List<CloudRequestDTO> getPreferredClouds() { return preferredClouds; }

	//-------------------------------------------------------------------------------------------------
	public void setRequestedService(final ServiceQueryFormDTO requestedService) { this.requestedService = requestedService; }
	public void setPreferredClouds(final List<CloudRequestDTO> preferredClouds) { this.preferredClouds = preferredClouds; }
}