package eu.arrowhead.common.dto.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import eu.arrowhead.api.systemregistry.model.SystemRegistryResponseDTO;

import java.io.Serializable;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class SystemRegistryListResponseDTO implements Serializable {

	//=================================================================================================
	// members

	private static final long serialVersionUID = 3892383727230105100L;

	private List<SystemRegistryResponseDTO> data;
	private long count;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public SystemRegistryListResponseDTO() {}

	//-------------------------------------------------------------------------------------------------
	public SystemRegistryListResponseDTO(final List<SystemRegistryResponseDTO> data) {
		this(data, data.size());
	}

	//-------------------------------------------------------------------------------------------------
	public SystemRegistryListResponseDTO(final List<SystemRegistryResponseDTO> data, final long count) {
		this.data = data;
		this.count = count;
	}

	//-------------------------------------------------------------------------------------------------
	public List<SystemRegistryResponseDTO> getData() {return data;}
	public long getCount() {return count;}
	
	//-------------------------------------------------------------------------------------------------
	public void setData(final List<SystemRegistryResponseDTO> data) {this.data = data;}
	public void setCount(final long count) {this.count = count;}	
}