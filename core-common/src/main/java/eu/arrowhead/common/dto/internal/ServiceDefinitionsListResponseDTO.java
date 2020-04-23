package eu.arrowhead.common.dto.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import eu.arrowhead.api.serviceregistry.model.ServiceDefinitionResponseDTO;

import java.io.Serializable;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class ServiceDefinitionsListResponseDTO implements Serializable {

	//=================================================================================================
	// members
	
	private static final long serialVersionUID = 6737069652359698446L;
	
	private List<ServiceDefinitionResponseDTO> data;
	private long count;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public ServiceDefinitionsListResponseDTO() {}

	//-------------------------------------------------------------------------------------------------
	public ServiceDefinitionsListResponseDTO(final List<ServiceDefinitionResponseDTO> data, final long count) {
		this.data = data;
		this.count = count;
	}

	//-------------------------------------------------------------------------------------------------
	public List<ServiceDefinitionResponseDTO> getData() { return data; }
	public long getCount() { return count; }
	
	//-------------------------------------------------------------------------------------------------
	public void setData(final List<ServiceDefinitionResponseDTO> data) { this.data = data; }
	public void setCount(final long count) { this.count = count; }
}