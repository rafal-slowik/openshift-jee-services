package rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import config.ConfigProperties;
import service.ServiceFunctions;
import utils.TestUtils;

@RunWith(MockitoJUnitRunner.class)
public class RestResourcesFibonacciTest {

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
	public void getFibonacciOfTestN31() {
		long expectedResult = 1346269;
		String n = "31";
		fibonacciTest(n, expectedResult, Status.OK);
	}

	@Test
	public void getFibonacciOfTestNMinus32() {
		long expectedResult = -2178309;
		String n = "-32";
		fibonacciTest(n, expectedResult, Status.OK);
	}

	@Test
	public void getFibonacciOfTestN32() {
		long expectedResult = 2178309;
		String n = "32";
		fibonacciTest(n, expectedResult, Status.OK);
	}
	
	@Test
	public void getFibonacciOfTestN92() {
		long expectedResult = 7540113804746346429l;
		String n = "92";
		fibonacciTest(n, expectedResult, Status.OK);
	}
	
	@Test
	public void getFibonacciOfTestNMinus92() {
		long expectedResult = -7540113804746346429l;
		String n = "-92";
		fibonacciTest(n, expectedResult, Status.OK);
	}
	
	@Test
	public void getFibonacciOfTestN93() {
		Long expectedResult = null;
		String n = "93";
		fibonacciTest(n, expectedResult, Status.BAD_REQUEST);
	}
	
	@Test
	public void getFibonacciOfTestNMinus93() {
		Long expectedResult = null;
		String n = "-93";
		fibonacciTest(n, expectedResult, Status.BAD_REQUEST);
	}
	
	@Test
	public void getFibonacciOfTestNMinus10000() {
		Long expectedResult = null;
		String n = "-10000";
		fibonacciTest(n, expectedResult, Status.BAD_REQUEST);
	}
	
	@Test
	public void getFibonacciOfTestNText() {
		String n = "Text";
		String message = "Some message";
		Map<String, String> expectedResult = new HashMap<>();
		expectedResult.put("message", message);
		when(configProperties.findByKey(Mockito.anyString())).thenReturn(message);
		fibonacciTest(n, expectedResult, Status.BAD_REQUEST);
	}
	
	@Test
	public void getFibonacciOfTestNoNParam() {
		String message = "Some other message";
		Map<String, String> expectedResult = new HashMap<>();
		expectedResult.put("message", message);
		MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
		map.add("c", "d");
		map.add("e", "f");
		when(configProperties.formatProperty(Mockito.anyString(), Mockito.anyString())).thenReturn(message);
		
		fibonacciTest(expectedResult, Status.BAD_REQUEST, map);
	}
	
	@Test
	public void getFibonacciOfTestNoParams() {
		String message = "Some next message";
		when(configProperties.findByKey(Mockito.anyString())).thenReturn(message);
		Map<String, String> expectedResult = new HashMap<>();
		expectedResult.put("message", message);
		MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
		when(configProperties.formatProperty(Mockito.anyString(), Mockito.anyString())).thenReturn(message);
		
		fibonacciTest(expectedResult, Status.BAD_REQUEST, map);
	}

	////// help methods ///////////
	private void fibonacciTest(Object expected, Status expectedResponseStatus, MultivaluedMap<String, String> map) {
		Object expectedResult = expected;
		UriInfo uriInfo = null;
		try {
			uriInfo = TestUtils.mockUriInfo("127.0.0.1", map);
		} catch (URISyntaxException e) {
			fail("The exception shouldn't be thrown!");
		}
		when(serviceFunctions.getFibonacciOf(Mockito.anyLong())).thenCallRealMethod();
		Response response = resources.getFibonacciOf(uriInfo);

		assertEquals(expectedResponseStatus.getStatusCode(), response.getStatus());
		assertEquals(expectedResult, response.getEntity());

	}
	
	
	private void fibonacciTest(String n, Object expected, Status expectedResponseStatus) {
		Object expectedResult = expected;
		UriInfo uriInfo = null;
		try {
			uriInfo = TestUtils.mockUriInfo("127.0.0.1", prepareMultivalueMapFibonacci("n", n));
		} catch (URISyntaxException e) {
			fail("The exception shouldn't be thrown!");
		}
		when(serviceFunctions.getFibonacciOf(Mockito.anyLong())).thenCallRealMethod();
		Response response = resources.getFibonacciOf(uriInfo);

		assertEquals(expectedResponseStatus.getStatusCode(), response.getStatus());
		assertEquals(expectedResult, response.getEntity());

	}

	private MultivaluedMap<String, String> prepareMultivalueMapFibonacci(String parameterName, String value) {
		MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
		map.add(parameterName, value);
		return map;
	}
}
