package eu.arrowhead.common.dto.internal;

import eu.arrowhead.api.cloud.model.CloudRequestDTO;
import eu.arrowhead.api.systemregistry.model.SystemRequestDTO;
import eu.arrowhead.common.Utilities;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TokenGenerationRequestDTO implements Serializable {

	//=================================================================================================
	// members
	
	private static final long serialVersionUID = -662827797790310767L;
	
	private SystemRequestDTO consumer;
	private CloudRequestDTO consumerCloud;
	private List<TokenGenerationProviderDTO> providers = new ArrayList<>();
	private String service;
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public TokenGenerationRequestDTO() {}
	
	//-------------------------------------------------------------------------------------------------
	public TokenGenerationRequestDTO(final SystemRequestDTO consumer, final CloudRequestDTO consumerCloud, final List<TokenGenerationProviderDTO> providers, final String service) {
		Assert.notNull(consumer, "Consumer is null.");
		Assert.isTrue(providers != null && !providers.isEmpty(), "Provider list is null or empty.");
		Assert.isTrue(!Utilities.isEmpty(service), "Service is null or blank.");
		
		this.consumer = consumer;
		this.consumerCloud = consumerCloud;
		this.providers = providers;
		this.service = service;
	}
	
	//-------------------------------------------------------------------------------------------------
	public SystemRequestDTO getConsumer() { return consumer; }
	public CloudRequestDTO getConsumerCloud() { return consumerCloud; }
	public List<TokenGenerationProviderDTO> getProviders() { return providers; }
	public String getService() { return service; }
	
	//-------------------------------------------------------------------------------------------------
	public void setConsumer(final SystemRequestDTO consumer) { this.consumer = consumer; }
	public void setConsumerCloud(final CloudRequestDTO consumerCloud) { this.consumerCloud = consumerCloud; }
	public void setProviders(final List<TokenGenerationProviderDTO> providers) { this.providers = providers; }
	public void setService(final String service) { this.service = service; }
}