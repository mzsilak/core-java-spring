package eu.arrowhead.common;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceConfigurationError;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import eu.arrowhead.common.dto.ErrorMessageDTO;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.exception.ExceptionType;

@RunWith(SpringRunner.class)
public class UtilitiesTest {

	//=================================================================================================
	// members

	private static final String OS_NAME = "os.name";
	private static final String WINDOWS_PREFIX = "win";
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testToPrettyJSONInputNull() {
		final String result = Utilities.toPrettyJson(null);
		Assert.assertNull(result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testToPrettyJSONNotJSON() {
		final String result = Utilities.toPrettyJson("abc");
		Assert.assertEquals("abc", result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testToPrettyJSONValidObject() {
		final String osName = System.getProperty(OS_NAME).toLowerCase();
		final String expected =  osName.startsWith(WINDOWS_PREFIX) ? "{\r\n  \"a\" : 1\r\n}" : "{\n  \"a\" : 1\n}";
		
		final String result = Utilities.toPrettyJson("{ \"a\": 1 }");
		Assert.assertEquals(expected, result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testToPrettyJSONValidList() {
		final String osName = System.getProperty(OS_NAME).toLowerCase();
		final String expected =  osName.startsWith(WINDOWS_PREFIX) ? "[ {\r\n  \"a\" : 1\r\n} ]" : "[ {\n  \"a\" : 1\n} ]";
		
		final String result = Utilities.toPrettyJson("[{ \"a\": 1 }]");
		Assert.assertEquals(expected, result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testFromJSONNullString() {
		final ErrorMessageDTO result = Utilities.fromJson(null, ErrorMessageDTO.class);
		Assert.assertNull(result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testFromJSONNullClass() {
		final Object result = Utilities.fromJson("does not matter", null);
		Assert.assertNull(result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = ArrowheadException.class)
	public void testFromJSONConvertFailed() {
		Utilities.fromJson("wrong JSON", ErrorMessageDTO.class);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testFromJSONConvertSuccess() {
		ErrorMessageDTO result = Utilities.fromJson("{ \"exceptionType\": \"AUTH\" }", ErrorMessageDTO.class);
		Assert.assertEquals(ExceptionType.AUTH, result.getExceptionType());
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testText2MapNull() {
		Assert.assertNull(Utilities.text2Map(null));
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testText2MapEmpty() {
		final Map<String,String> result = Utilities.text2Map("    ");
		Assert.assertEquals(0, result.size());
	}

	//-------------------------------------------------------------------------------------------------
	@Test
	public void testText2MapOne() {
		final Map<String,String> result = Utilities.text2Map("abc = def");
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("def", result.get("abc"));
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testText2MapTwoOneWOValue() {
		final Map<String,String> result = Utilities.text2Map("abc =, def= ghi   ");
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("", result.get("abc"));
		Assert.assertEquals("ghi", result.get("def"));
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testText2MapDecoding() {
		final Map<String,String> result = Utilities.text2Map("ab%3D = def,  ghi = i%2Cj");
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("def", result.get("ab="));
		Assert.assertEquals("i,j", result.get("ghi"));
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testMap2TextNull() {
		Assert.assertNull(Utilities.map2Text(null));
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testMap2TextEmpty() {
		final String result = Utilities.map2Text(Collections.<String,String>emptyMap());
		Assert.assertEquals("", result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testMap2TextNormal() {
		final Map<String,String> map = new HashMap<String,String>(2);
		map.put("abc", "def");
		map.put("ghi", "jkl");
		final String result = Utilities.map2Text(map);
		Assert.assertTrue(result.contains("abc=def"));
		Assert.assertTrue(result.contains("ghi=jkl"));
	}

	//-------------------------------------------------------------------------------------------------
	@Test
	public void testMap2TextEncoding() {
		final Map<String,String> map = new HashMap<String,String>(1);
		map.put("ab=", "d,f");
		final String result = Utilities.map2Text(map);
		Assert.assertEquals("ab%3D=d%2Cf", result);
	}

	//-------------------------------------------------------------------------------------------------
	@Test
	public void testCalculateHttpStatusFromArrowheadExceptionBradbury() {
		final ArrowheadException ex = new ArrowheadException("Fahrenheit", 451);
		final HttpStatus result = Utilities.calculateHttpStatusFromArrowheadException(ex);
		Assert.assertEquals(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testCalculateHttpStatusFromArrowheadExceptionInternalServerError() {
		final ArrowheadException ex = new ArrowheadException("Does not matter.");
		final HttpStatus result = Utilities.calculateHttpStatusFromArrowheadException(ex);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testGetCertCNFromSubjectOk() {
		final String result = Utilities.getCertCNFromSubject("cn=abc.def.gh");
		Assert.assertEquals("abc.def.gh", result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testGetCertCNFromSubjectNotOk() {
		final String result = Utilities.getCertCNFromSubject("abc.def.gh");
		Assert.assertNull(result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testGetCertCNFromSubjectNullParameter() {
		final String result = Utilities.getCertCNFromSubject(null);
		Assert.assertNull(result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = ServiceConfigurationError.class)
	public void testGetFirstCertFromKeyStoreNotInitializedKeyStore() throws KeyStoreException {
		final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		Utilities.getFirstCertFromKeyStore(keystore);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test(expected = ServiceConfigurationError.class)
	public void testGetFirstCertFromKeyStoreEmptyKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(null, null);
		Utilities.getFirstCertFromKeyStore(keystore);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testIsKeyStoreCNArrowheadValidGood() {
		final boolean result = Utilities.isKeyStoreCNArrowheadValid("service.cloud.operator.arrowhead.eu");
		Assert.assertTrue(result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testIsKeyStoreCNArrowheadValidWrongFormat() {
		final boolean result = Utilities.isKeyStoreCNArrowheadValid("service;cloud;operator;arrowhead;eu");
		Assert.assertFalse(result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testIsKeyStoreCNArrowheadValidTooShort() {
		final boolean result = Utilities.isKeyStoreCNArrowheadValid("cloud.operator.arrowhead.eu");
		Assert.assertFalse(result);
	}

	//-------------------------------------------------------------------------------------------------
	@Test
	public void testIsKeyStoreCNArrowheadValidWrongSuffix() {
		final boolean result = Utilities.isKeyStoreCNArrowheadValid("service.cloud.operator.arrowhead.hu");
		Assert.assertFalse(result);
	}

	//-------------------------------------------------------------------------------------------------
	@Test
	public void testIsKeyStoreCNArrowheadValidWrongMasterName() {
		final boolean result = Utilities.isKeyStoreCNArrowheadValid("service.cloud.operator.arowhead.eu");
		Assert.assertFalse(result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testIsKeyStoreCNArrowheadValidCompareWithCloudCNGood() {
		final boolean result = Utilities.isKeyStoreCNArrowheadValid("service.cloud.operator.arrowhead.eu", "cloud.OPERATOR.arrowhead.eu");
		Assert.assertTrue(result);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Test
	public void testIsKeyStoreCNArrowheadValidCompareWithCloudCNWrongFormat() {
		final boolean result = Utilities.isKeyStoreCNArrowheadValid("service;cloud;operator;arrowhead;eu", "cloud.operator.arrowhead.eu");
		Assert.assertFalse(result);
	}

	//-------------------------------------------------------------------------------------------------
	@Test
	public void testIsKeyStoreCNArrowheadValidCompareWithCloudCNDifferentCloud() {
		final boolean result = Utilities.isKeyStoreCNArrowheadValid("service.cloud2.operator.arrowhead.eu", "cloud.OPERATOR.arrowhead.eu");
		Assert.assertFalse(result);
	}
}