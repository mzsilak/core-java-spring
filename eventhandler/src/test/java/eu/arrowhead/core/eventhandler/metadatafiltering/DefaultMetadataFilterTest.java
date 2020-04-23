package eu.arrowhead.core.eventhandler.metadatafiltering;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class DefaultMetadataFilterTest {
	
	//=================================================================================================
	// members
	
	@InjectMocks
	private final MetadataFilteringAlgorithm algorithm = new DefaultMetadataFilter();
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testDoFilteringEqualMaps() {
		final Map<String,String> map = Map.of("a", "a1", "b", "b1", "c", "c1");
		final MetadataFilteringParameters params = new MetadataFilteringParameters(map, map);
		
		final boolean result = algorithm.doFiltering(params);
		
		assertTrue(result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testDoFilteringMapsDifferentCaseInValuesOk() {
		final Map<String,String> map0 = Map.of("a", "a1", "b", "b1", "c", "c1");
		final Map<String,String> map1 = Map.of("a", "A1", "b", "B1", "c", "C1");
		final MetadataFilteringParameters params = new MetadataFilteringParameters(map0, map1);
		
		final boolean result = algorithm.doFiltering(params);
		
		assertTrue(result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testDoFilteringMapsMoreEntriesInEventMetaDataThanInFilterOk() {
		final Map<String,String> map0 = Map.of("a", "a1");
		final Map<String,String> map1 = Map.of("a", "a1", "b", "b1", "c", "c1");
		final MetadataFilteringParameters params = new MetadataFilteringParameters(map0, map1);
		
		final boolean result = algorithm.doFiltering(params);
		
		assertTrue(result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testDoFilteringMapsDifferentCaseInKeysNotOk() {
		final Map<String,String> map0 = Map.of("a", "a1", "b", "b1", "c", "c1");
		final Map<String,String> map1 = Map.of("A", "a1", "b", "b1", "c", "c1");
		final MetadataFilteringParameters params = new MetadataFilteringParameters(map0, map1);
		
		final boolean result = algorithm.doFiltering(params);
		
		assertFalse(result);
		
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testDoFilteringMapsNoEventMetadataEntryForFilterKeyNotOk() {
		final Map<String,String> map0 = Map.of("a", "a1", "b", "b1", "c", "c1");
		final Map<String,String> map1 = Map.of("a", "a1", "b", "b1" );
		final Map<String,String> map2 = Map.of("a", "a1", "b", "b1", "d", "c1");
		
		final MetadataFilteringParameters params0 = new MetadataFilteringParameters(map0, map1);
		final MetadataFilteringParameters params1 = new MetadataFilteringParameters(map0, map2);
		
		final boolean result0 = algorithm.doFiltering(params0);
		final boolean result1 = algorithm.doFiltering(params1);
		
		assertFalse(result0);
		assertFalse(result1);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testDoFilteringMapsSameKeysDifferentValuesNotOk() {
		final Map<String,String> map0 = Map.of("a", "a1", "b", "b1", "c", "c1");
		final Map<String,String> map1 = Map.of("a", "b1", "b", "a1", "c", "c1");
		final MetadataFilteringParameters params = new MetadataFilteringParameters(map0, map1);
		
		final boolean result = algorithm.doFiltering(params);
		
		assertFalse(result);
	}
}