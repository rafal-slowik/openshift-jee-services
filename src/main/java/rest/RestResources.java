package rest;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.annotations.cache.NoCache;

import config.ConfigProperties;
import enums.Triangle;
import service.ServiceFunctions;

/**
 * 
 * <p>
 * This class provides a RESTful service operations.
 * </p>
 * 
 * @author Rafal Slowik
 * 
 *
 */
@Path("")
@RequestScoped
public class RestResources {

	@Inject
	private ConfigProperties properties;
	@Inject
	private ServiceFunctions service;

	/* constants */
	public static final String INVALID_REQUEST = "invalid_request";
	public static final String RESOURCE_NOT_FOUND = "resource_not_found";
	public static final int MAX_NUMBER_OF_CHARACTERS = 1655;
	public static final int MAX_NUMBER_OF_FIBONACCI = 92;

	/**
	 * The service calculates the n-th number of Fibonacci sequence. Only query
	 * parameters will be accepted. The service starts the calculation if
	 * <code>n</code> parameter will be found. The parameters' names are case
	 * sensitive.
	 * 
	 * @param info
	 * @return response ({@link Response})
	 */
	@GET
	@Path("/{fibonacci : (?i)fibonacci}")
	@Produces(MediaType.APPLICATION_JSON)
	@NoCache
	public Response getFibonacciOf(@Context UriInfo info) {

		long n;
		long multiplayer;

		String temporaryVar = info.getQueryParameters().getFirst("n");
		if (temporaryVar == null && info.getQueryParameters().size() > 0) {
			String message = properties.formatProperty(RESOURCE_NOT_FOUND, info.getRequestUri().toString());
			return createResponseWithMessage(Status.BAD_REQUEST, message).build();
		}
		try {
			n = Long.parseLong(StringUtils.trim(temporaryVar));
			multiplayer = n < 0 ? (long) Math.pow(-1, (long)(n + 1)) : 1;
			n = Math.abs(n);
		} catch (NumberFormatException e) {
			return createResponseWithMessage(Status.BAD_REQUEST, properties.findByKey(INVALID_REQUEST)).build();
		}

		if (n > MAX_NUMBER_OF_FIBONACCI) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		long result = service.getFibonacciOf(n) * multiplayer;
		return Response.status(Status.OK).entity(result).build();
	}

	/**
	 * The service returns string token obtained during the recruitment
	 * registration process at Readify.
	 * 
	 * @return response ({@link Response})
	 */
	@GET
	@Path("/{token : (?i)token}")
	@Produces(MediaType.APPLICATION_JSON)
	@NoCache
	public Response token() {
		return createOkRequestResponse(properties.findByKey("token")).build();
	}

	/**
	 * The service reverses the letters of each word of the sentence and doesn't
	 * change the order of sentence.
	 * 
	 * @param info
	 * @return response ({@link Response})
	 */
	@GET
	@Path("/{reverseWords : (?i)reverseWords}")
	@Produces(MediaType.APPLICATION_JSON)
	@NoCache
	public Response reverseSentence(@Context UriInfo info) {

		String sentence = info.getQueryParameters().getFirst("sentence");
		if (StringUtils.isEmpty(sentence)) {
			return createOkRequestResponse("\"\"").build();
		}

		if (sentence.length() > MAX_NUMBER_OF_CHARACTERS) {
			return createResponseWithMessage(Status.NOT_FOUND, properties.findByKey("service_unavaliable")).build();
		}

		StringBuilder sb = service.reverseSentence(sentence);
		String result = StringEscapeUtils.escapeJava(sb.toString());
		return createOkRequestResponse("\"" + result + "\"").build();
	}

	/**
	 * Given three sides of a triangle (query parameters 'a', 'b' and 'c'), the
	 * service recognizes the type of triangle.
	 * 
	 * @param info
	 * @return response ({@link Response})
	 */
	@GET
	@Path("/{triangleType : (?i)triangleType}")
	@Produces(MediaType.APPLICATION_JSON)
	@NoCache
	public Response triangleType(@Context UriInfo info) {

		long a;
		long b;
		long c;

		String strA = info.getQueryParameters().getFirst("a");
		String strB = info.getQueryParameters().getFirst("b");
		String strC = info.getQueryParameters().getFirst("c");
		if (strA == null || strB == null || strC == null) {
			String message = properties.formatProperty(RESOURCE_NOT_FOUND, info.getRequestUri().toString());
			return createResponseWithMessage(Status.NOT_FOUND, message).build();
		}

		try {
			a = Long.parseLong(strA.trim());
			b = Long.parseLong(strB.trim());
			c = Long.parseLong(strC.trim());
			if (a > Integer.MAX_VALUE || b > Integer.MAX_VALUE || c > Integer.MAX_VALUE) {
				return createResponseWithMessage(Status.BAD_REQUEST, properties.findByKey(INVALID_REQUEST)).build();
			}
		} catch (NumberFormatException e) {
			return createResponseWithMessage(Status.BAD_REQUEST, properties.findByKey(INVALID_REQUEST)).build();
		}

		Triangle triangle = Triangle.getTriangleType(a, b, c);
		String responseEntity = properties.findByKey(triangle.getPropertyKey());

		return createOkRequestResponse(responseEntity).build();
	}

	private <E> Response.ResponseBuilder createOkRequestResponse(E entity) {
		return Response.status(Response.Status.OK).entity(entity);
	}

	private Response.ResponseBuilder createResponseWithMessage(Status status, String message) {
		Map<String, String> responseObj = new HashMap<>();
		responseObj.put("message", message);
		return Response.status(status).entity(responseObj);
	}
}
