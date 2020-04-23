package eu.arrowhead.common.dto.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import eu.arrowhead.api.orchestration.model.OrchestrationResponseDTO;
import eu.arrowhead.api.orchestration.model.OrchestrationResultDTO;

import java.io.Serializable;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class ICNResultDTO extends OrchestrationResponseDTO implements Serializable {
	
	//=================================================================================================
	// members

	private static final long serialVersionUID = -286883397386724556L;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public ICNResultDTO() {
		super();
	}

	//-------------------------------------------------------------------------------------------------
	public ICNResultDTO(final List<OrchestrationResultDTO> response) {
		super(response);
	}
}