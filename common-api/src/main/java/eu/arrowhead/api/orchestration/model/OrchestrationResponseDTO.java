package eu.arrowhead.api.orchestration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class OrchestrationResponseDTO implements Serializable {

	//=================================================================================================
	// members
	
	private static final long serialVersionUID = -7019947320156696116L;
	
	protected List<OrchestrationResultDTO> response = new ArrayList<>();
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public OrchestrationResponseDTO() {}

	//-------------------------------------------------------------------------------------------------
	public OrchestrationResponseDTO(final List<OrchestrationResultDTO> response) {
		this.response = response != null ? response : List.of();
	}

	//-------------------------------------------------------------------------------------------------
	public List<OrchestrationResultDTO> getResponse() { return response; }

	//-------------------------------------------------------------------------------------------------
	public void setResponse(final List<OrchestrationResultDTO> response) {
		if (response != null) {
			this.response = response;
		}
	}
}