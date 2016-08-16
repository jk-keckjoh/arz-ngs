package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.get.ServiceInstanceResponse;

@RequestScoped
@Named("serviceInstanceDetail")
public class DetailViewController
		implements Serializable {


	private static final long serialVersionUID = 1L;

	@Inject
	private ServiceInstanceAdmin admin;

	private String instance;
	private String service;
	private String environment;
	private String host;
	private String status;
	private String version;
	private String completeName;


	private String scriptName;
	private String pathStart;
	private String pathStop;
	private String pathRestart;
	private String pathStatus;


	@PostConstruct
	public void init() {
		// ServiceInstanceResponse response = admin.getServiceInstance("arctis", "pebk123", "lnx002", "arctis_1");
		// instance = response.getInstanceName();
		// service = response.getServiceName();
		// environment = response.getEnvironmentName();
		// host = response.getHostName();
		// status = response.getStatus().toString();
		//
		// ScriptData scriptData = response.getScript();
		// scriptName = scriptData.getScriptName();
		// pathStart = scriptData.getPathStart();
		// pathStop = scriptData.getPathStop();
		// pathRestart = scriptData.getPathRestart();
		// pathStatus = scriptData.getPathStatus();
	}


	public String showDetail(String instance, String service, String environment, String host) {
		ServiceInstanceResponse response = admin.getServiceInstance(service, environment, host, instance);
		this.instance = response.getInstanceName();
		this.service = response.getServiceName();
		this.environment = response.getEnvironmentName();
		this.host = response.getHostName();
		this.status = response.getStatus().name();
		this.version = String.valueOf(response.getVersion());
		this.completeName = this.service + "/" + this.environment + "/" + this.host + "/" + this.instance;

		ScriptData scriptData = response.getScript();
		scriptName = scriptData.getScriptName();
		pathStart = scriptData.getPathStart();
		pathStop = scriptData.getPathStop();
		pathRestart = scriptData.getPathRestart();
		pathStatus = scriptData.getPathStatus();
		return "detailview";
	}

	// public String detail() {
	//
	// instance = "testInstance";
	// service = "testService";
	// environment = "testEnvironment";
	// host = "testHost";
	// return "";
	// }

	public String getCompleteName() {
		return completeName;
	}

	public String getVersion() {
		return version;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getService() {
		return service;
	}

	public String getEnvironment() {
		return environment;
	}

	public String getHost() {
		return host;
	}


	public String getStatus() {
		return status;
	}

	public String getScriptName() {
		return scriptName;
	}

	public String getPathStart() {
		return pathStart;
	}

	public String getPathStop() {
		return pathStop;
	}

	public String getPathRestart() {
		return pathRestart;
	}

	public String getPathStatus() {
		return pathStatus;
	}

}
