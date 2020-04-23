package eu.arrowhead.common.database.service;

import eu.arrowhead.api.common.exception.ArrowheadException;
import eu.arrowhead.api.common.exception.DataNotFoundException;
import eu.arrowhead.api.common.exception.InvalidParameterException;
import eu.arrowhead.common.database.entity.Cloud;
import eu.arrowhead.common.database.repository.CloudRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CommonDBServiceTest {
	
	//=================================================================================================
	// members
	
	@InjectMocks
	private CommonDBService commonDBService; 

	@Mock
	private CloudRepository cloudRepository;

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = DataNotFoundException.class)
	public void testGetOwnCloudNoResult() {
		when(cloudRepository.findByOwnCloudAndSecure(true, true)).thenReturn(List.of());
		commonDBService.getOwnCloud(true);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = ArrowheadException.class)
	public void testGetOwnCloudTooMuchResult() {
		when(cloudRepository.findByOwnCloudAndSecure(true, true)).thenReturn(List.of(new Cloud(), new Cloud()));
		commonDBService.getOwnCloud(true);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = InvalidParameterException.class)
	public void testInsertOwnCloudCloudAlreadyExists() {
		when(cloudRepository.findByOperatorAndName("operator", "name")).thenReturn(Optional.of(new Cloud()));
		commonDBService.insertOwnCloud("operator", "name", false, null);
	}
}