package at.arz.ngs.ui.controllers;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.action.PerformAction;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverview;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;
import at.arz.ngs.ui.data_collections.OverviewCollection;

@RequestScoped
@Named("actionController")
public class ActionController {

	@Inject
	private ServiceInstanceAdmin admin;

	@Inject
	private ServiceInstanceController serviceInstanceController;

	private ErrorCollection errorCollection;

	@PostConstruct
	public void init() {
	}

	public String start(List<OverviewCollection> overviewList) {
		PerformAction action = new PerformAction();
		action.setPerformAction("start");

		performAction(action, overviewList);

		serviceInstanceController.formSubmit();
		return "overview";
	}

	public String stop(List<OverviewCollection> overviewList) {
		PerformAction action = new PerformAction();
		action.setPerformAction("stop");

		performAction(action, overviewList);

		serviceInstanceController.formSubmit();
		return "overview";
	}

	public String restart(List<OverviewCollection> overviewList) {
		PerformAction action = new PerformAction();
		action.setPerformAction("restart");

		performAction(action, overviewList);

		serviceInstanceController.formSubmit();
		return "overview";
	}

	public String status(List<OverviewCollection> overviewList) {
		PerformAction action = new PerformAction();
		action.setPerformAction("status");

		performAction(action, overviewList);

		serviceInstanceController.formSubmit();
		return "overview";
	}

	private void performAction(PerformAction action, List<OverviewCollection> overviewList) {
		errorCollection = new ErrorCollection();
		for (OverviewCollection oc : overviewList) {
			if (oc.isChecked()) {
				ServiceInstanceOverview serviceInstance = oc.getServiceInstance();
				System.out.println(action + " service instance: " + serviceInstance.toString());
				try {
					admin.performAction(serviceInstance.getServiceName(),
										serviceInstance.getEnvironmentName(),
										serviceInstance.getHostName(),
										serviceInstance.getInstanceName(),
										action);
				} catch (RuntimeException e) {
					errorCollection.addError(new Error(e));
					e.printStackTrace();
				}
			}
			oc.setChecked(false); // set default, no checkbox checked
		}
		if (errorCollection.getErrors().size() > 0) {
			errorCollection.setShowPopup(true);
		}
	}

	public void startSingle(String service, String environment, String host, String instance) {
		PerformAction action = new PerformAction();
		action.setPerformAction("start");

		performSingleAction(action, service, environment, host, instance);

		try {
			FacesContext.getCurrentInstance()
						.getExternalContext()
						.redirect("detailview.xhtml?instance="+ instance
									+ "&service="
									+ service
									+ "&env="
									+ environment
									+ "&host="
									+ host);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopSingle(String service, String environment, String host, String instance) {
		PerformAction action = new PerformAction();
		action.setPerformAction("stop");

		performSingleAction(action, service, environment, host, instance);

		try {
			FacesContext.getCurrentInstance()
						.getExternalContext()
						.redirect("detailview.xhtml?instance="+ instance
									+ "&service="
									+ service
									+ "&env="
									+ environment
									+ "&host="
									+ host);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void restartSingle(String service, String environment, String host, String instance) {
		PerformAction action = new PerformAction();
		action.setPerformAction("restart");

		performSingleAction(action, service, environment, host, instance);

		try {
			FacesContext.getCurrentInstance()
						.getExternalContext()
						.redirect("detailview.xhtml?instance="+ instance
									+ "&service="
									+ service
									+ "&env="
									+ environment
									+ "&host="
									+ host);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void statusSingle(String service, String environment, String host, String instance) {
		PerformAction action = new PerformAction();
		action.setPerformAction("status");

		performSingleAction(action, service, environment, host, instance);

		try {
			FacesContext.getCurrentInstance()
						.getExternalContext()
						.redirect("detailview.xhtml?instance="+ instance
									+ "&service="
									+ service
									+ "&env="
									+ environment
									+ "&host="
									+ host);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ErrorCollection getErrorCollection() {
		return errorCollection;
	}

	public void setErrorCollection(ErrorCollection errorCollection) {
		this.errorCollection = errorCollection;
	}

	private void performSingleAction(	PerformAction action,
										String service,
										String environment,
										String host,
										String instance) {
		errorCollection = new ErrorCollection();
		try {
			admin.performAction(service, environment, host, instance, action);
		} catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			e.printStackTrace();
			return;
		}
		System.out.println(action.getPerformAction()+ ": "
							+ instance
							+ "/"
							+ service
							+ "/"
							+ environment
							+ "/"
							+ host);
	}
}
