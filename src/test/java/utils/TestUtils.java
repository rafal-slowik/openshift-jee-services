package utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

public class TestUtils {

	public static final String QUOTATION_MARKS = "\"\"";
	
	public static UriInfo mockUriInfo(String uri, MultivaluedMap<String, String> pathParameters)
			throws URISyntaxException {
		UriInfo uriInfo = mock(UriInfo.class);
		URI requestUri = new URI(uri);
		when(uriInfo.getRequestUri()).thenReturn(requestUri);
		when(uriInfo.getQueryParameters()).thenReturn(pathParameters);
		return uriInfo;
	}
	
	public static MultivaluedMap<String, String> prepareMultivalueMapSingleParameter(String parameterName, String value) {
		MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
		map.add(parameterName, value);
		return map;
	}
}
