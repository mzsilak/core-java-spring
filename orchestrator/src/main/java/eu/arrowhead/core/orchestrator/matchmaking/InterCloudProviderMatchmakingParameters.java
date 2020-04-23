package eu.arrowhead.core.orchestrator.matchmaking;

import eu.arrowhead.api.orchestration.model.PreferredProviderDataDTO;
import eu.arrowhead.common.dto.internal.ICNResultDTO;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class InterCloudProviderMatchmakingParameters {
	
	//=================================================================================================
	// members
	
	protected ICNResultDTO icnResultDTO;
	protected List<PreferredProviderDataDTO> preferredGlobalProviders = new ArrayList<>();
	protected boolean storeOrchestration;
	
	// additional parameter can be add here to provide information to the various matchmaking algorithms
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public InterCloudProviderMatchmakingParameters(final ICNResultDTO icnResultDTO,	final List<PreferredProviderDataDTO> preferredGlobalProviders, final boolean storeOrchestration) {
		Assert.notNull(icnResultDTO, "icnResultDTO is null.");
		Assert.notNull(preferredGlobalProviders, "preferredLocalProviders list is null.");
		
		this.icnResultDTO = icnResultDTO;
		this.preferredGlobalProviders = preferredGlobalProviders;
		this.storeOrchestration = storeOrchestration;
	}

	//-------------------------------------------------------------------------------------------------
	public ICNResultDTO getIcnResultDTO( ) { return icnResultDTO; }
	public List<PreferredProviderDataDTO> getPreferredGlobalProviders() { return preferredGlobalProviders; }
	public boolean isStoreOrchestration() {return storeOrchestration; }

	//-------------------------------------------------------------------------------------------------
	public void setPreferredGlobalProviders(final List<PreferredProviderDataDTO> preferredGlobalProviders) {
		if (preferredGlobalProviders != null) {
			this.preferredGlobalProviders = preferredGlobalProviders;
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public void setIcnResponseDTO(final ICNResultDTO icnResultDTO) { this.icnResultDTO = icnResultDTO; }
	public void setStoreOrchestration(final boolean storeOrchestration) { this.storeOrchestration = storeOrchestration; }
}