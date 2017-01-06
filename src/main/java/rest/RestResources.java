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

import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.core.UriInfo;

import config.ConfigProperties;
import enums.Triangle;
import service.ServiceFunctions;

@Path("")
@RequestScoped
public class RestResources {

	@Inject
	private ConfigProperties properties;
	
	@Inject
	private ServiceFunctions service;
	
	private static final String INVALID_REQUEST = "invalid_request";
	private static final String RESOURCE_NOT_FOUND = "resource_not_found"; 

	@GET
	@Path("/Fibonacci")
	@Produces(MediaType.APPLICATION_JSON)
	@NoCache
	public Response getFibonacciOf(@Context UriInfo info) {
		boolean isMinus = false;
		String temporaryVar = info.getQueryParameters().getFirst("n");
		long n;

		if (temporaryVar == null && info.getQueryParameters().size() > 0) {
			String message = properties.formatProperty(RESOURCE_NOT_FOUND, info.getRequestUri().toString());
			return createBadRequestResponse(message).build();
		}
		try {
			n = Long.parseLong(temporaryVar == null ? temporaryVar : temporaryVar.trim());
			isMinus = n < 0;
			n = Math.abs(n);
		} catch (NumberFormatException e) {
			return createBadRequestResponse(properties.findByKey(INVALID_REQUEST)).build();
		}

		if (n > 92 || n < -92) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		long result = service.getFibonacciOf(n);
		if (isMinus) {
			result = (long) Math.pow(-1, n + 1) * result;
		}
		return Response.status(Status.OK).entity(result).build();
	}

	@GET
	@Path("/Token")
	@Produces(MediaType.APPLICATION_JSON)
	@NoCache
	public Response token() {
		return createOkRequestResponse(properties.findByKey("token")).build();
	}

	@GET
	@Path("/ReverseWords")
	@Produces(MediaType.APPLICATION_JSON)
	@NoCache
	public Response reverseSentence(@Context UriInfo info) {
		String sentence = info.getQueryParameters().getFirst("sentence");
		if (sentence == null || "".equals(sentence)) {
			return createOkRequestResponse("\"\"").build();
		}

		if (sentence.length() > 1655) {
			return createNotFoundRequestResponse(properties.findByKey("service_unavaliable")).build();
		}

		StringBuilder sb = service.reverseSentence(sentence);
		String result = StringEscapeUtils.escapeJava(sb.toString());
		return createOkRequestResponse("\"" + result + "\"").build();
	}

	@GET
	@Path("/TriangleType")
	@Produces(MediaType.APPLICATION_JSON)
	@NoCache
	public Response triangleType(@Context UriInfo info) {

		String strA = info.getQueryParameters().getFirst("a");
		String strB = info.getQueryParameters().getFirst("b");
		String strC = info.getQueryParameters().getFirst("c");

		long a, b, c;

		if (strA == null || strB == null || strC == null) {
			String message = properties.formatProperty(RESOURCE_NOT_FOUND, info.getRequestUri().toString());
			return createNotFoundRequestResponse(message).build();
		}
		
		try {
			a = Long.parseLong(strA.trim());
			b = Long.parseLong(strB.trim());
			c = Long.parseLong(strC.trim());
			if(a > Integer.MAX_VALUE || b > Integer.MAX_VALUE || c > Integer.MAX_VALUE) {
				return createBadRequestResponse(properties.findByKey(INVALID_REQUEST)).build();
			}
		} catch (NumberFormatException e) {
			return createBadRequestResponse(properties.findByKey(INVALID_REQUEST)).build();
		}
		
		Triangle triangle = Triangle.getTriangleType(a, b, c);
		String responseEntity = properties.findByKey(triangle.getPropertyKey());

		return createOkRequestResponse(responseEntity).build();
	}

	private <E> Response.ResponseBuilder createOkRequestResponse(E entity) {
		return Response.status(Response.Status.OK).entity(entity);
	}
	
	private <E> Response.ResponseBuilder createNotFoundRequestResponse(String message) {
		Map<String, String> responseObj = new HashMap<>();
		responseObj.put("message", message);
		return Response.status(Response.Status.NOT_FOUND).entity(responseObj);
	}

	private Response.ResponseBuilder createBadRequestResponse(String message) {
		Map<String, String> responseObj = new HashMap<>();
		responseObj.put("message", message);
		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	}
}
