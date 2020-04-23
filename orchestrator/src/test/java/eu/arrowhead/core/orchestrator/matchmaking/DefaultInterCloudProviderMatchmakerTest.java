package eu.arrowhead.core.orchestrator.matchmaking;

import eu.arrowhead.api.orchestration.model.OrchestrationResponseDTO;
import eu.arrowhead.api.orchestration.model.OrchestrationResultDTO;
import eu.arrowhead.api.orchestration.model.PreferredProviderDataDTO;
import eu.arrowhead.api.systemregistry.model.SystemRequestDTO;
import eu.arrowhead.common.dto.internal.ICNResultDTO;
import eu.arrowhead.api.systemregistry.model.SystemResponseDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class DefaultInterCloudProviderMatchmakerTest {

	//=================================================================================================
	// members
	
	private InterCloudProviderMatchmakingAlgorithm algorithm = new DefaultInterCloudProviderMatchmaker();
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Test
	public void doMatchmakingNoMatchingPreferredProviderNoStoreOrchestrationOk() {		
		final SystemResponseDTO system1 = new SystemResponseDTO(1, "system1", "localhost", 1234, null, null, null);
		final SystemResponseDTO system2 = new SystemResponseDTO(1, "system2", "localhost", 4567, null, null, null);
		
		final SystemRequestDTO reqSystem = new SystemRequestDTO();
		reqSystem.setSystemName("other");
		reqSystem.setAddress("127.0.0.1");
		reqSystem.setPort(1111);
		final PreferredProviderDataDTO ppDTO = new PreferredProviderDataDTO();
		ppDTO.setProviderSystem(reqSystem);
		
		final OrchestrationResultDTO orDTO1 = new OrchestrationResultDTO();
		orDTO1.setProvider(system1);
		
		final OrchestrationResultDTO orDTO2 = new OrchestrationResultDTO();
		orDTO2.setProvider(system2);
		
		final List<OrchestrationResultDTO> orchestrationResultDTOList = List.of(orDTO1, orDTO2);
		final ICNResultDTO icnResultDTO = new ICNResultDTO();
		icnResultDTO.setResponse(orchestrationResultDTOList);
		
		final List<PreferredProviderDataDTO> preferredProviderDataDTOList = List.of(ppDTO);
		final boolean storeOchestration = false;
		
		final InterCloudProviderMatchmakingParameters params = new InterCloudProviderMatchmakingParameters(icnResultDTO, preferredProviderDataDTOList, storeOchestration);		
		final OrchestrationResponseDTO orchestrationResponseDTO = algorithm.doMatchmaking(params);
		
		Assert.assertEquals(1, orchestrationResponseDTO.getResponse().size());
		Assert.assertTrue(orDTO1.getProvider().getSystemName().equalsIgnoreCase(orchestrationResponseDTO.getResponse().get(0).getProvider().getSystemName()));
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void doMatchmakingNoMatchingPreferredProviderStoreOrchestrationOk() {		
		final SystemResponseDTO system1 = new SystemResponseDTO(1, "system1", "localhost", 1234, null, null, null);
		final SystemResponseDTO system2 = new SystemResponseDTO(1, "system2", "localhost", 4567, null, null, null);
		
		final SystemRequestDTO reqSystem = new SystemRequestDTO();
		reqSystem.setSystemName("other");
		reqSystem.setAddress("127.0.0.1");
		reqSystem.setPort(1111);
		final PreferredProviderDataDTO ppDTO = new PreferredProviderDataDTO();
		ppDTO.setProviderSystem(reqSystem);
		
		final OrchestrationResultDTO orDTO1 = new OrchestrationResultDTO();
		orDTO1.setProvider(system1);
		
		final OrchestrationResultDTO orDTO2 = new OrchestrationResultDTO();
		orDTO2.setProvider(system2);
		
		final List<OrchestrationResultDTO> orchestrationResultDTOList = List.of(orDTO1, orDTO2);
		final ICNResultDTO icnResultDTO = new ICNResultDTO();
		icnResultDTO.setResponse(orchestrationResultDTOList);
		
		final List<PreferredProviderDataDTO> preferredProviderDataDTOList = List.of(ppDTO);
		final boolean storeOchestration = true;
		
		final InterCloudProviderMatchmakingParameters params = new InterCloudProviderMatchmakingParameters(icnResultDTO, preferredProviderDataDTOList, storeOchestration);		
		final OrchestrationResponseDTO orchestrationResponseDTO = algorithm.doMatchmaking(params);
		
		Assert.assertTrue(orchestrationResponseDTO.getResponse().isEmpty());
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void doMatchmakingMatchingPreferredProviderStoreOrchestrationOk() {		
		final SystemResponseDTO system1 = new SystemResponseDTO(1, "system1", "localhost", 1234, null, null, null);
		final SystemResponseDTO system2 = new SystemResponseDTO(1, "system2", "localhost", 4567, null, null, null);
		
		final SystemRequestDTO reqSystem = new SystemRequestDTO();
		reqSystem.setSystemName("system1");
		reqSystem.setAddress("localhost");
		reqSystem.setPort(1234);
		final PreferredProviderDataDTO ppDTO = new PreferredProviderDataDTO();
		ppDTO.setProviderSystem(reqSystem);
		
		final OrchestrationResultDTO orDTO1 = new OrchestrationResultDTO();
		orDTO1.setProvider(system1);
		
		final OrchestrationResultDTO orDTO2 = new OrchestrationResultDTO();
		orDTO2.setProvider(system2);
		
		final List<OrchestrationResultDTO> orchestrationResultDTOList = List.of(orDTO1, orDTO2);
		final ICNResultDTO icnResultDTO = new ICNResultDTO();
		icnResultDTO.setResponse(orchestrationResultDTOList);
		
		final List<PreferredProviderDataDTO> preferredProviderDataDTOList = List.of(ppDTO);
		final boolean storeOchestration = true;
		
		final InterCloudProviderMatchmakingParameters params = new InterCloudProviderMatchmakingParameters(icnResultDTO, preferredProviderDataDTOList, storeOchestration);		
		final OrchestrationResponseDTO orchestrationResponseDTO = algorithm.doMatchmaking(params);
		
		Assert.assertEquals(1, orchestrationResponseDTO.getResponse().size());
		Assert.assertTrue(orDTO1.getProvider().getSystemName().equalsIgnoreCase(orchestrationResponseDTO.getResponse().get(0).getProvider().getSystemName()));
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void doMatchmakingNullParams() {		
		final InterCloudProviderMatchmakingParameters params = null;		
		algorithm.doMatchmaking(params);
	}
}