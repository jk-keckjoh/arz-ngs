package at.arz.ngs.api;

/**
 * Ein ScriptName ist eine Komposition von dem zugehörgien ApplicationName und EnvironmentName, seperiert durch einen
 * "_"
 * 
 * @author rpri333
 *
 */
public class ScriptName {

	private String name;

	/**
	 * Sets a custom name to the script, if no name was specified (like a 1:1 mapping, e.g. one ServiceInstance has
	 * exactly one ScriptName). This script name is unique, because it is built out of ServiceInstance Primary Key
	 * 
	 * @param environmentName
	 * @param serviceInstanceName
	 * @param hostName
	 */
	public ScriptName(	EnvironmentName environmentName,
						ServiceInstanceName serviceInstanceName,
						HostName hostName,
						ServiceName serviceName) {
		name = "@@"+ environmentName.hashCode()
				+ ""
				+ serviceInstanceName.hashCode()
				+ ""
				+ hostName.hashCode()
				+ ""
				+ serviceName.hashCode();
	}

	public ScriptName(String scriptName) {
		this.name = scriptName;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ScriptName other = (ScriptName) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
