package at.arz.ngs.job;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.Job;
import at.arz.ngs.JobRepository;
import at.arz.ngs.ServiceInstance;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.api.Action;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.JobId;
import at.arz.ngs.api.ServiceInstanceLocation;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.UserName;
import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.User;
import at.arz.ngs.security.UserRepository;

@Stateless
public class JobScheduler {

	@Resource
	private SessionContext context;

	@Inject
	private ServiceInstanceRepository serviceInstanceRepo;

	@Inject
	private UserRepository userRepository;

	@Inject
	private SecurityAdmin securityAdmin;

	@Inject
	private JobRepository jobRepository;

	@Inject
	private JobExecutor jobExecutor;

	public JobId scheduleAction(Action action, ServiceName service, EnvironmentName env,
			ServiceInstanceLocation... locations) {
		return scheduleAction(action, service, env, new HashSet<>(Arrays.asList(locations)));
	}

	public JobId scheduleAction(Action action, ServiceName service, EnvironmentName env,
			Set<ServiceInstanceLocation> locations) {
		if (locations.isEmpty()) {
			throw new IllegalArgumentException("At least a single service location is required");
		}

		List<ServiceInstance> instances = serviceInstanceRepo.getServiceInstances(service, env);
		List<ServiceInstance> filtered = new LinkedList<>();
		for (ServiceInstance instance : instances) {
			if (locations.contains(
					new ServiceInstanceLocation(instance.getHost().getHostName(), instance.getServiceInstanceName()))) {
				if (instance.getJob() == null) {
					filtered.add(instance);
					continue;
				}
				throw new IllegalStateException("Job already scheduled!");
			}
		}

		String actor = context.getCallerPrincipal().getName();
		User creator = userRepository.getUser(new UserName(actor)); //if passes -> user exists

		securityAdmin.proofPerformAction(env, service, action); //if passes -> user has rights to perform the actions

		JobId id = new JobId();
		Job job = new Job(id, creator, action, filtered);

		jobRepository.addJob(job);

		// TODO JobRepository anlegen
		// TODO Job persistieren
		// Job asynchron ausführen + pessimistic locking
		return id;
	}

	public void startJob(JobId jobId) {
		jobExecutor.executeJob(jobId);
	}

	//	public void notifyActionCompleted(JobId jobId, siUNI...){ //nicht hier einfügen
	//		 //TODO
	//	}

}
