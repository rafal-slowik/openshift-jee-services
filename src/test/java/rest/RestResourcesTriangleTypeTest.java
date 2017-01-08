package rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static utils.TestUtils.mockUriInfo;

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

@RunWith(MockitoJUnitRunner.class)
public class RestResourcesTriangleTypeTest {

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
	public void triangleTypeSingleNullParameter() {
		MultivaluedMap<String, String> paramMap = new MultivaluedHashMap<>();
		paramMap.add("a", null);
		paramMap.add("b", " 5 ");
		paramMap.add("c", "6");

		Map<String, String> expectedResult = new HashMap<>();
		String errorMessage = "some error message";
		expectedResult.put("message", errorMessage);

		when(configProperties.formatProperty(Mockito.anyString(), Mockito.anyString())).thenReturn(errorMessage);

		triangleTypeTest(expectedResult, Status.NOT_FOUND, paramMap);
	}

	@Test
	public void triangleTypeOneParameterGreaterThanMaxInt() {
		MultivaluedMap<String, String> paramMap = new MultivaluedHashMap<>();
		paramMap.add("a", String.valueOf((long) Integer.MAX_VALUE + 1));
		paramMap.add("b", " 5 ");
		paramMap.add("c", "6");

		Map<String, String> expectedResult = new HashMap<>();
		String errorMessage = "other error message";
		expectedResult.put("message", errorMessage);

		when(configProperties.findByKey(Mockito.anyString())).thenReturn(errorMessage);

		triangleTypeTest(expectedResult, Status.BAD_REQUEST, paramMap);
	}

	@Test
	public void triangleTypeOneParameterNotInt() {
		MultivaluedMap<String, String> paramMap = new MultivaluedHashMap<>();
		paramMap.add("a", " tetx");
		paramMap.add("b", " 5 ");
		paramMap.add("c", "6");

		Map<String, String> expectedResult = new HashMap<>();
		String errorMessage = "other error message";
		expectedResult.put("message", errorMessage);

		when(configProperties.findByKey(Mockito.anyString())).thenReturn(errorMessage);

		triangleTypeTest(expectedResult, Status.BAD_REQUEST, paramMap);
	}

	// isosceles = "Isosceles"
	// scalene = "Scalene"
	// equilateral = "Equilateral"
	// error = "Error"
	//
	@Test
	public void triangleTypeScalene() {
		MultivaluedMap<String, String> paramMap = new MultivaluedHashMap<>();
		paramMap.add("a", " 3");
		paramMap.add("b", " 5 ");
		paramMap.add("c", "6");

		String expectedResult = "\"Scalene\"";

		when(configProperties.findByKey(Mockito.anyString())).thenCallRealMethod();

		triangleTypeTest(expectedResult, Status.OK, paramMap);
	}

	@Test
	public void triangleTypeEquilateral() {
		MultivaluedMap<String, String> paramMap = new MultivaluedHashMap<>();
		paramMap.add("a", " 5");
		paramMap.add("b", " 5 ");
		paramMap.add("c", "5");

		String expectedResult = "\"Equilateral\"";

		when(configProperties.findByKey(Mockito.anyString())).thenCallRealMethod();

		triangleTypeTest(expectedResult, Status.OK, paramMap);
	}

	@Test
	public void triangleTypeIsosceles() {
		MultivaluedMap<String, String> paramMap = new MultivaluedHashMap<>();
		paramMap.add("a", " 4");
		paramMap.add("b", " 5 ");
		paramMap.add("c", "5");

		String expectedResult = "\"Isosceles\"";

		when(configProperties.findByKey(Mockito.anyString())).thenCallRealMethod();

		triangleTypeTest(expectedResult, Status.OK, paramMap);
	}

	@Test
	public void triangleTypeErrorNegativeParameter() {
		MultivaluedMap<String, String> paramMap = new MultivaluedHashMap<>();
		paramMap.add("a", " -4");
		paramMap.add("b", " 5 ");
		paramMap.add("c", "5");

		String expectedResult = "\"Error\"";

		when(configProperties.findByKey(Mockito.anyString())).thenCallRealMethod();

		triangleTypeTest(expectedResult, Status.OK, paramMap);
	}

	@Test
	public void triangleTypeErrorNotProperProportions() {
		MultivaluedMap<String, String> paramMap = new MultivaluedHashMap<>();
		paramMap.add("a", " 4");
		paramMap.add("b", " 5 ");
		paramMap.add("c", "11");

		String expectedResult = "\"Error\"";

		when(configProperties.findByKey(Mockito.anyString())).thenCallRealMethod();

		triangleTypeTest(expectedResult, Status.OK, paramMap);
	}

	////// help methods ///////////
	private void triangleTypeTest(Object expected, Status expectedResponseStatus, MultivaluedMap<String, String> map) {
		Object expectedResult = expected;
		UriInfo uriInfo = null;
		try {
			uriInfo = mockUriInfo("127.0.0.1", map);
		} catch (URISyntaxException e) {
			fail("The exception shouldn't be thrown!");
		}
		Response response = resources.triangleType(uriInfo);

		assertEquals(expectedResponseStatus.getStatusCode(), response.getStatus());
		assertEquals(expectedResult, response.getEntity());

	}
}
