package at.arz.ngs.security;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import at.arz.ngs.api.RoleName;
import at.arz.ngs.converter.jpa.RoleNameConverter;

@Entity
@Table(name = "ROLE")
@NamedQueries({@NamedQuery(name = Role.QUERY_ALL, query = "SELECT r FROM Role r"),
		@NamedQuery(name = Role.QUERY_BY_ROLENAME, query = "SELECT r FROM Role r WHERE r.roleName = :rname"),
		@NamedQuery(name = Role.QUERY_BY_OID, query = "SELECT r FROM Role r WHERE r.oid = :oid")})
public class Role {

	public static final String QUERY_ALL = "Role.getAll";
	public static final String QUERY_BY_ROLENAME = "Role.findByUniqueKey";
	public static final String QUERY_BY_OID = "Role.findByOID";

	@Id
	@GeneratedValue(generator = "ngs.role", strategy = GenerationType.TABLE)
	private long oid;

	@Column(name = "ROLE_NAME", unique = true)
	@Convert(converter = RoleNameConverter.class)
	private RoleName roleName;

	@JoinColumn(name = "PERMISSION_OID")
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private List<Permission> permissions;

	@OneToMany(mappedBy = "role")
	private List<User_Role> user_roles;

	protected Role() {
		//jpa constructor
	}

	public Role(RoleName roleName) {
		this.roleName = roleName;
		permissions = new LinkedList<>();
	}

	public long getOid() {
		return oid;
	}

	public RoleName getRoleName() {
		return roleName;
	}

	public List<Permission> getPermissions() {
		return Collections.unmodifiableList(permissions);
	}

	public void addPermission(Permission permission) {
		permissions.add(permission);
	}

	public void removePermission(Permission permission) {
		permissions.remove(permission);
	}

	public void addUser_Role(User_Role userRole) {
		user_roles.add(userRole);
	}

	public void removeUser_Role(User_Role userRole) {
		user_roles.remove(userRole);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
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
		Role other = (Role) obj;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		}
		else if (!roleName.equals(other.roleName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return roleName.getName();
	}
}
