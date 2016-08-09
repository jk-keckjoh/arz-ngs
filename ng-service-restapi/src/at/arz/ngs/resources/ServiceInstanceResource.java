package at.arz.ngs.resources;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverviewList;
import at.arz.ngs.serviceinstance.commands.get.ServiceInstanceResponse;
import at.arz.ngs.serviceinstance.commands.remove.RemoveServiceInstance;
import at.arz.ngs.serviceinstance.commands.update.UpdateServiceInstance;

@Path("/instances")
public class ServiceInstanceResource {

	@Inject
	private ServiceInstanceAdmin instanceAdmin;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createNewServiceInstance(CreateNewServiceInstance command) {
		try {
			instanceAdmin.createNewServiceInstance(command);

			String path = command.getServiceName()+ "/"
							+ command.getEnvironmentName()
							+ "/"
							+ command.getHostName()
							+ "/"
							+ command.getInstanceName();

			URI location = URI.create("/instances/" + path);
			return Response.ok().status(Status.CREATED).location(location).build();
		} catch (RuntimeException e) { // TODO later on if more than one exception can be thrown, more Status can be
										// sent
			URI location = URI.create("/instances/");
			return Response.notModified().status(Status.CONFLICT).location(location).build();
		}
		
	}

	/**
	 * Must be exactly specified with the parameters of the old data. The new data relies in the JSON/XML object
	 * 
	 * @param serviceName The old Param.
	 * @param environmentName The old Param.
	 * @param hostName The old Param.
	 * @param instanceName The old Param.
	 * @param command Object with the new data.
	 * @return Returns a HTTP status if the operation was successul or not.
	 */
	@PUT
	@Path("{service}/{environment}/{host}/{name}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateServiceInstance(	@PathParam("service") String serviceName,
											@PathParam("environment") String environmentName,
											@PathParam("host") String hostName,
											@PathParam("name") String instanceName,
											UpdateServiceInstance command) {

		try {
			// instanceAdmin.execute(command);

			String path = command.getServiceName()+ "/"
							+ command.getEnvironmentName()
							+ "/"
							+ command.getHostName()
							+ "/"
							+ command.getInstanceName();

			URI location = URI.create("/instances/" + path);
			return Response.ok().status(Status.OK).location(location).build();
		} catch (RuntimeException e) {
			URI location = URI.create("/instances/");
			return Response.notModified().status(Status.CONFLICT).location(location).build();
		}
	}

	/**
	 * The params must be set for exactly specifying which instance should be deleted.
	 * 
	 * @param serviceName
	 * @param environmentName
	 * @param hostName
	 * @param instanceName
	 * @param command Additional information for deletion. Must be set, especially the version tag.
	 */
	@DELETE
	@Path("{service}/{environment}/{host}/{name}")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response deleteServiceInstance(	@PathParam("service") String serviceName,
										@PathParam("environment") String environmentName,
										@PathParam("host") String hostName,
										@PathParam("name") String instanceName,
										RemoveServiceInstance command) {

		try {
			// instanceAdmin.execute(command

			URI location = URI.create("/instances/");
			return Response.ok().status(Status.OK).location(location).build();
		} catch (RuntimeException e) {
			URI location = URI.create("/instances/");
			return Response.notModified().status(Status.CONFLICT).location(location).build();
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public ServiceInstanceResponse getServiceInstance(	@PathParam("service") String serviceName,
														@PathParam("environment") String environmentName,
														@PathParam("host") String hostName,
														@PathParam("name") String instanceName) {

		try {
			ServiceInstanceResponse response = null; // = instanceAdmin.get
			return response;

		} catch (RuntimeException e) {
			return null;
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public ServiceInstanceOverviewList getServiceInstances(	@QueryParam("serviceName") String serviceName,
															@QueryParam("environmentName") String environmentName,
															@QueryParam("hostName") String hostName,
															@QueryParam("instanceName") String instanceName) {

		if (serviceName == null || serviceName.equals("")) {
			serviceName = "*";
		}
		if (environmentName == null || environmentName.equals("")) {
			environmentName = "*";
		}
		if (hostName == null || hostName.equals("")) {
			serviceName = "*";
		}
		if (instanceName == null || instanceName.equals("")) {
			instanceName = "*";
		}

		try {
			ServiceInstanceOverviewList overview = null; // = instanceAdmin.get
			return overview;

		} catch (RuntimeException e) {
			return null;
		}
	}
}