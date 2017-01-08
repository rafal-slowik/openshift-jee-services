package rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static utils.TestUtils.QUOTATION_MARKS;
import static utils.TestUtils.mockUriInfo;
import static utils.TestUtils.prepareMultivalueMapSingleParameter;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import config.ConfigProperties;
import service.ServiceFunctions;

public class RestResourcesReverseWordsTest {
	
	@Mock
	private ServiceFunctions serviceFunctions;
	@Mock
	private ConfigProperties configProperties;

	@InjectMocks
	private RestResources resources;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getReverseWordsNullParameter() {
		reverseWordsTest(null, QUOTATION_MARKS, Status.OK);
	}

	@Test
	public void getReverseWordsEmptyStringParameter() {
		reverseWordsTest("", QUOTATION_MARKS, Status.OK);
	}

	@Test
	public void getReverseWordsExceededMaxLength() {
		String param = RandomStringUtils.randomAscii(RestResources.MAX_NUMBER_OF_CHARACTERS + 1);
		Map<String, String> expectedResult = new HashMap<>();
		String errorMessage = "some error message";
		expectedResult.put("message", errorMessage);

		when(configProperties.findByKey(Mockito.anyString())).thenReturn(errorMessage);

		reverseWordsTest(param, expectedResult, Status.NOT_FOUND);
	}
	
	@Test
	public void getReverseWordsNoSentenceParameter() {
		Map<String, String> expectedResult = new HashMap<>();
		String errorMessage = "some error message";
		expectedResult.put("message", errorMessage);
		MultivaluedMap<String, String> paramMap = new MultivaluedHashMap<>();
		paramMap.add("c", "d");
		paramMap.add("e", "f");
		when(configProperties.findByKey(Mockito.anyString())).thenReturn(errorMessage);
		reverseWordsTest(QUOTATION_MARKS, Status.OK, paramMap);
	}

	@Test
	public void getReverseWordsSimpleSentence() {
		String param = " test1 something2";
		String result = "\" 1tset 2gnihtemos\"";

		reverseWordsTest(param, result, Status.OK);
	}

	////// help methods ///////////
	private void reverseWordsTest(Object expected, Status expectedResponseStatus, MultivaluedMap<String, String> map) {
		Object expectedResult = expected;
		UriInfo uriInfo = null;
		try {
			uriInfo = mockUriInfo("127.0.0.1", map);
		} catch (URISyntaxException e) {
			fail("The exception shouldn't be thrown!");
		}
		when(serviceFunctions.reverseSentence(Mockito.anyString())).thenCallRealMethod();
		Response response = resources.reverseSentence(uriInfo);

		assertEquals(expectedResponseStatus.getStatusCode(), response.getStatus());
		assertEquals(expectedResult, response.getEntity());

	}

	private void reverseWordsTest(String sentence, Object expected, Status expectedResponseStatus) {
		Object expectedResult = expected;
		UriInfo uriInfo = null;
		try {
			uriInfo = mockUriInfo("127.0.0.1", prepareMultivalueMapSingleParameter("sentence", sentence));
		} catch (URISyntaxException e) {
			fail("The exception shouldn't be thrown!");
		}
		when(serviceFunctions.reverseSentence(Mockito.anyString())).thenCallRealMethod();
		Response response = resources.reverseSentence(uriInfo);

		assertEquals(expectedResponseStatus.getStatusCode(), response.getStatus());
		assertEquals(expectedResult, response.getEntity());

	}
}
