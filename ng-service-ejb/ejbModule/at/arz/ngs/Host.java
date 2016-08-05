package at.arz.ngs;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import at.arz.ngs.api.HostName;

@Entity
@NamedQueries({	@NamedQuery(name = "getAllHosts", query = "SELECT h FROM Host h"),
				@NamedQuery(name = "getHost", query = "SELECT h FROM Host h WHERE s.host_name = :hname") })
public class Host {

	@Id
	@GeneratedValue(generator = "ngs.host", strategy = GenerationType.TABLE)
	@Column(name = "HOST_OID")
	private long oid;

	@Column(name = "HOST_NAME", unique = true)
	private HostName hostName;


	@ManyToMany(mappedBy = "server", fetch = FetchType.LAZY)
	private List<ServiceInstance> services;

	protected Host() {
		// jpa constructor
	}

	public Host(HostName hostName) {
		this.hostName = hostName;
		services = new LinkedList<ServiceInstance>();
	}

	public void setHostName(HostName hostName) {
		this.hostName = hostName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Host other = (Host) obj;
		if (hostName == null) {
			if (other.hostName != null)
				return false;
		} else if (!hostName.equals(other.hostName))
			return false;
		return true;
	}


	public long getOid() {
		return oid;
	}

	public HostName getHostName() {
		return hostName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
		return result;
	}
}