package at.arz.ngs.resources.exceptionMapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import at.arz.ngs.api.exception.EnvironmentNotFound;
import at.arz.ngs.resources.NgsApiError;

@Provider
public class EnvironmentNotFoundMapper
		implements ExceptionMapper<EnvironmentNotFound> {

	@Override
	public Response toResponse(EnvironmentNotFound e) {
		NgsApiError error = new NgsApiError(e.getClass().getSimpleName(), e.getMessage());
		return Response	.status(Response.Status.CONFLICT)
						.type(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_XML)
						.entity(error)
						.build();
	}

}
