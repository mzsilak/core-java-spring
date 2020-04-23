package eu.arrowhead.common.dto.internal;

import eu.arrowhead.api.cloud.model.CloudRequestDTO;
import eu.arrowhead.api.systemregistry.model.SystemRequestDTO;

import java.io.Serializable;
import java.util.Map;

public class OrchestratorStoreRequestDTO implements Serializable {

	//=================================================================================================
	// members

	private static final long serialVersionUID = 6496923524186210327L;
	
	private String serviceDefinitionName;
	private Long consumerSystemId;
	private SystemRequestDTO providerSystem;
	private CloudRequestDTO cloud;
	private String serviceInterfaceName;
	private Integer priority;	
	private Map<String,String> attribute;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public OrchestratorStoreRequestDTO() {}
	
	//-------------------------------------------------------------------------------------------------
	public OrchestratorStoreRequestDTO(final String serviceDefinitionName, final Long consumerSystemId, final SystemRequestDTO providerSystem, final CloudRequestDTO cloud,
									   final String serviceInterfaceName, final Integer priority, final Map<String,String> attribute) {
		this.serviceDefinitionName = serviceDefinitionName;
		this.consumerSystemId = consumerSystemId;
		this.providerSystem = providerSystem;
		this.cloud = cloud;
		this.serviceInterfaceName = serviceInterfaceName;
		this.priority = priority;
		this.attribute = attribute;
	}
	
	//-------------------------------------------------------------------------------------------------
	public String getServiceDefinitionName() {return serviceDefinitionName;}
	public Long getConsumerSystemId() {return consumerSystemId;}
	public SystemRequestDTO getProviderSystem() {return providerSystem;}
	public CloudRequestDTO getCloud() {return cloud;}
	public String getServiceInterfaceName() {return serviceInterfaceName;} 
	public Integer getPriority() {return priority;}
	public Map<String,String> getAttribute() {return attribute;}	

	//-------------------------------------------------------------------------------------------------
	public void setServiceDefinitionName(final String serviceDefinitionName) {this.serviceDefinitionName = serviceDefinitionName;}
	public void setConsumerSystemId(final Long consumerSystemId) {this.consumerSystemId = consumerSystemId;}
	public void setProviderSystem(final SystemRequestDTO providerSystemDTO) {this.providerSystem = providerSystemDTO;}
	public void setCloud(final CloudRequestDTO cloudDTO) {this.cloud = cloudDTO;}
	public void setServiceInterfaceName(final String serviceInterfaceName) {this.serviceInterfaceName = serviceInterfaceName; }
	public void setPriority(final Integer priority) {this.priority = priority;}
	public void setAttribute(final Map<String,String> attribute) {this.attribute = attribute;}
}