package eu.arrowhead.common.dto.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class OrchestratorStoreListResponseDTO implements Serializable {

	//=================================================================================================
	// members
	
	private static final long serialVersionUID = -6965119621038969559L;
	
	private List<OrchestratorStoreResponseDTO> data = new ArrayList<>();
	private long count;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public OrchestratorStoreListResponseDTO() {}

	//-------------------------------------------------------------------------------------------------
	public OrchestratorStoreListResponseDTO(final List<OrchestratorStoreResponseDTO> orchestratorStoreResponeDTOList, final long totalNumberOfOrchestratorStores) {
		this.data = orchestratorStoreResponeDTOList;
		this.count = totalNumberOfOrchestratorStores;
	}
	
	//-------------------------------------------------------------------------------------------------
	public List<OrchestratorStoreResponseDTO> getData() { return data; }
	public long getCount() { return count; }

	//-------------------------------------------------------------------------------------------------
	public void setData(final List<OrchestratorStoreResponseDTO> data) { this.data = data; }
	public void setCount(final long count) { this.count = count; }
}