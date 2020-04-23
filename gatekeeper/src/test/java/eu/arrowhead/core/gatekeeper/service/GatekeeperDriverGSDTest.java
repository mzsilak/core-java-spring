package eu.arrowhead.core.gatekeeper.service;

import eu.arrowhead.api.cloud.model.CloudRequestDTO;
import eu.arrowhead.api.serviceregistry.model.ServiceQueryFormDTO;
import eu.arrowhead.api.serviceregistry.model.ServiceRegistryResponseDTO;
import eu.arrowhead.common.database.entity.Cloud;
import eu.arrowhead.common.dto.internal.GSDPollRequestDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class GatekeeperDriverGSDTest {
	
	//=================================================================================================
	// members
		
	private GatekeeperDriver testingObject = new GatekeeperDriver();

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testSendGSDPollRequestWithNullCloudList() throws InterruptedException {
		testingObject.sendGSDPollRequest(null, getGSDPollRequestDTO());
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testSendGSDPollRequestWithEmptyCloudList() throws InterruptedException {
		testingObject.sendGSDPollRequest(new ArrayList<>(), getGSDPollRequestDTO());
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testSendGSDPollRequestWithNullGSDPollRequestDTO() throws InterruptedException {
		testingObject.sendGSDPollRequest(List.of(new Cloud()), null);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testSendGSDPollRequestWithNullRequestedService() throws InterruptedException {
		final GSDPollRequestDTO gsdPollRequestDTO = getGSDPollRequestDTO();
		gsdPollRequestDTO.setRequestedService(null);
		
		testingObject.sendGSDPollRequest(List.of(new Cloud()), gsdPollRequestDTO);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testSendGSDPollRequestWithNullRequestedServiceDefinition() throws InterruptedException {
		final GSDPollRequestDTO gsdPollRequestDTO = getGSDPollRequestDTO();
		gsdPollRequestDTO.getRequestedService().setServiceDefinitionRequirement(null);
		
		testingObject.sendGSDPollRequest(List.of(new Cloud()), gsdPollRequestDTO);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testSendGSDPollRequestWithBlankRequestedServiceDefinition() throws InterruptedException {
		final GSDPollRequestDTO gsdPollRequestDTO = getGSDPollRequestDTO();
		gsdPollRequestDTO.getRequestedService().setServiceDefinitionRequirement("   ");
		
		testingObject.sendGSDPollRequest(List.of(new Cloud()), gsdPollRequestDTO); 
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testSendGSDPollRequestWithNullRequesterCloud() throws InterruptedException {
		final GSDPollRequestDTO gsdPollRequestDTO = getGSDPollRequestDTO();
		gsdPollRequestDTO.setRequesterCloud(null);;
		
		testingObject.sendGSDPollRequest(List.of(new Cloud()), gsdPollRequestDTO);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testSendServiceReistryQueryNullQueryForm() {
		testingObject.sendServiceRegistryQuery(null);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testSendInterCloudAuthorizationCheckQueryWithNullQueryDataList() {
		testingObject.sendInterCloudAuthorizationCheckQuery(null, new CloudRequestDTO(), "test-service");
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testSendInterCloudAuthorizationCheckQueryWithNullCloud() {
		testingObject.sendInterCloudAuthorizationCheckQuery(List.of(new ServiceRegistryResponseDTO()), null, "test-service");
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testSendInterCloudAuthorizationCheckQueryWithNullServiceDefinition() {
		testingObject.sendInterCloudAuthorizationCheckQuery(List.of(new ServiceRegistryResponseDTO()), new CloudRequestDTO(), null);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testSendInterCloudAuthorizationCheckQueryWithBlankServiceDefinition() {
		testingObject.sendInterCloudAuthorizationCheckQuery(List.of(new ServiceRegistryResponseDTO()), new CloudRequestDTO(), "  ");
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------		
	private GSDPollRequestDTO getGSDPollRequestDTO() {
		final ServiceQueryFormDTO serviceQueryFormDTO = new ServiceQueryFormDTO();
		serviceQueryFormDTO.setServiceDefinitionRequirement("test-service");
		
		final CloudRequestDTO cloudRequestDTO = new CloudRequestDTO();
		cloudRequestDTO.setOperator("test-operator");
		cloudRequestDTO.setName("test-name");
		
		return new GSDPollRequestDTO(serviceQueryFormDTO, cloudRequestDTO, false);
	}
}