package at.arz.ngs.resources.exceptionWrapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import at.arz.ngs.api.exception.ActionInProgress;
import at.arz.ngs.resources.NgsApiError;

@Provider
public class AlreadyPerformWrapper
		implements ExceptionMapper<ActionInProgress> {

	@Override
	public Response toResponse(ActionInProgress e) {
		NgsApiError error = new NgsApiError(e.getClass().getSimpleName(), e.getMessage());
		return Response	.status(Response.Status.CONFLICT)
						.type(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_XML)
						.entity(error)
						.build();
	}

}
