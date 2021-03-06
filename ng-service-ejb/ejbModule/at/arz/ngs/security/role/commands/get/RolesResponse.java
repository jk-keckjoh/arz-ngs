package at.arz.ngs.security.role.commands.get;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RolesResponse {

	private List<String> roles;

	public RolesResponse() {
		roles = new LinkedList<>();
	}

	public List<String> getRoles() {
		return Collections.unmodifiableList(roles);
	}

	public void addRole(String role) {
		roles.add(role);
	}
}
