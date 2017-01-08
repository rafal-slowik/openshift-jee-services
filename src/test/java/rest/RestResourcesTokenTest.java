package rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import config.ConfigProperties;

@RunWith(MockitoJUnitRunner.class)
public class RestResourcesTokenTest {

	@Mock
	private ConfigProperties configProperties;

	@InjectMocks
	private RestResources resources;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void token() {
		String myExampleToken = "abc-abshdg-ahdhhdgch";
		when(configProperties.findByKey(Mockito.anyString())).thenReturn(myExampleToken);
		
		Response response = resources.token();
		
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(myExampleToken, response.getEntity());
	}
}
