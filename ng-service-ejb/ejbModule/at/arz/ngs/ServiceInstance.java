package at.arz.ngs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.Status;

@Entity
public class ServiceInstance {

	@Id
	@GeneratedValue(generator = "ngs.serviceinstance", strategy = GenerationType.TABLE)
	private long oid;

	@Column(name = "SERVICEINSTANCE_NAME")
	private ServiceInstanceName serviceInstanceName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPLICATION_OID")
	private Service applicaton;
	private Host host;
	private Environment environment;
	private Status status;
	private Script script;

	public ServiceInstance() {
			// jpa constructor
	}

	public ServiceInstance(	ServiceInstanceName serviceName,
							Service applicaton,
							Host host,
							Environment environment,
							Script script,
							Status status) {
		this.environment = environment;
		this.serviceInstanceName = serviceName;
		this.host = host;
		this.applicaton = applicaton;
		this.script = script;
		this.status = status;
	}

	public long getOid() {
		return oid;
	}

	public ServiceInstanceName getServiceInstanceName() {
		return serviceInstanceName;
	}

	public Service getApplicaton() {
		return applicaton;
	}

	public Host getHost() {
		return host;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public Status getStatus() {
		return status;
	}

	public Script getScript() {
		return script;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((environment == null) ? 0 : environment.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((serviceInstanceName == null) ? 0 : serviceInstanceName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceInstance other = (ServiceInstance) obj;
		if (environment == null) {
			if (other.environment != null)
				return false;
		} else if (!environment.equals(other.environment))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (serviceInstanceName == null) {
			if (other.serviceInstanceName != null)
				return false;
		} else if (!serviceInstanceName.equals(other.serviceInstanceName))
			return false;
		return true;
	}

}
