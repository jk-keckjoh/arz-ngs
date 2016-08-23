package at.arz.ngs.security;

import static org.junit.Assert.fail;

import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.EnvironmentRepository;
import at.arz.ngs.HostRepository;
import at.arz.ngs.ScriptRepository;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.Action;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.RoleName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.UserName;
import at.arz.ngs.api.exception.NoPermission;
import at.arz.ngs.environment.jpa.JPAEnvironmentRepository;
import at.arz.ngs.host.jpa.JPAHostRepository;
import at.arz.ngs.script.jpa.JPAScriptRepository;
import at.arz.ngs.search.SearchEngine;
import at.arz.ngs.security.commands.Actor;
import at.arz.ngs.security.permission.commands.PermissionData;
import at.arz.ngs.security.permission.commands.addToRole.AddPermissionToRole;
import at.arz.ngs.security.permission.jpa.JPAPermissionRepository;
import at.arz.ngs.security.role.commands.UserRole;
import at.arz.ngs.security.role.commands.get.RoleResponse;
import at.arz.ngs.security.role.jpa.JPARoleRepository;
import at.arz.ngs.security.user.commands.addRole.AddRoleToUser;
import at.arz.ngs.security.user.jpa.JPAUserRepository;
import at.arz.ngs.security.userrole.jpa.JPAUser_RoleRepository;
import at.arz.ngs.service.jpa.JPAServiceRepository;
import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;
import at.arz.ngs.serviceinstance.jpa.JPAServiceInstanceRepository;

public class SecurityAdminIT
		extends AbstractJpaIT {

	private ServiceInstanceAdmin serviceAdmin;

	private ServiceRepository serviceRepository;
	private HostRepository hostRepository;
	private EnvironmentRepository environmentRepository;
	private ScriptRepository scriptRepository;
	private ServiceInstanceRepository instanceRepository;

	private SecurityAdmin securityAdmin;
	private PermissionRepository permissionRepository;
	private RoleRepository roleRepository;
	private UserRepository userRepository;
	private User_RoleRepository userRoleRepository;

	@Before
	public void setUpBeforeClass() {
		serviceRepository = new JPAServiceRepository(getEntityManager());
		hostRepository = new JPAHostRepository(getEntityManager());
		environmentRepository = new JPAEnvironmentRepository(getEntityManager());
		scriptRepository = new JPAScriptRepository(getEntityManager());
		instanceRepository = new JPAServiceInstanceRepository(getEntityManager());
		serviceAdmin = new ServiceInstanceAdmin(serviceRepository,
												hostRepository,
												environmentRepository,
												instanceRepository,
												scriptRepository,
												new SearchEngine(getEntityManager()));

		permissionRepository = new JPAPermissionRepository(getEntityManager());
		roleRepository = new JPARoleRepository(getEntityManager());
		userRepository = new JPAUserRepository(getEntityManager());
		userRoleRepository = new JPAUser_RoleRepository(getEntityManager());
		securityAdmin = new SecurityAdmin(permissionRepository,
											roleRepository,
											userRepository,
											userRoleRepository);

		ScriptData scriptData = new ScriptData();
		scriptData.setPathStart("start");
		CreateNewServiceInstance command = new CreateNewServiceInstance();
		command.setEnvironmentName("env1");
		command.setHostName("host1");
		command.setInstanceName("instance1");
		command.setServiceName("serv1");
		command.setScript(scriptData);
		serviceAdmin.createNewServiceInstance(command);

		userRepository.addUser(new UserName("daniel"));
		userRepository.addUser(new UserName("admin"));
		userRepository.addUser(new UserName("alex"));
		userRepository.addUser(new UserName("user1"));
		userRepository.addUser(new UserName("user2"));
		// roleRepository.addRole(new RoleName("Administrator"));
		roleRepository.addRole(new RoleName("entwickler"));

		// userRoleRepository.addUser_Role(userRepository.getUser(new UserName("daniel")),
		// roleRepository.getRole(new RoleName("entwickler")),
		// true);

		// securityAdmin.addPermissionToRole(actor, command);

		Actor admin = new Actor(userRepository.getUser(new UserName("admin")).getUserName().toString()); // preset admin
		// to
		// admin-rights
		roleRepository.addRole(new RoleName(SecurityAdmin.ADMIN));
		AddRoleToUser addRoleToUserCommand = new AddRoleToUser("admin", SecurityAdmin.ADMIN, true);
		securityAdmin.addRoleToUser(admin, addRoleToUserCommand);

		RoleResponse adminRoles = securityAdmin.getRolesForUser("admin");
		for (UserRole ur : adminRoles.getUserRoles()) {
			System.err.println("Role for admin: " + ur.getRoleName());
		}

	}

	// @Test
	public void proofPermission() {
		Actor actor = new Actor(userRepository.getUser(new UserName("daniel")).getUserName().toString());
		Actor admin = new Actor(userRepository.getUser(new UserName("admin")).getUserName().toString());
		AddRoleToUser addRoleToUserCommand = new AddRoleToUser("daniel", "entwickler", true);
		securityAdmin.addRoleToUser(admin, addRoleToUserCommand);
		PermissionData permissionData = new PermissionData("env1", "serv1", Action.all.name());
		AddPermissionToRole addPermissionToRoleCommand = new AddPermissionToRole("entwickler", permissionData);
		securityAdmin.addPermissionToRole(admin, addPermissionToRoleCommand);
		securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv1"), Action.all, actor);
		try {
			securityAdmin.proofPerformAction(new EnvironmentName("env2"), new ServiceName("serv1"), Action.all, actor);
			fail();
		} catch (NoPermission e) {
			// wanted
		}
		try {
			securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv2"), Action.all, actor);
			fail();
		} catch (NoPermission e) {
			// wanted
		}
	}

	// @Test
	public void proofPermission2() {
		Actor actor = new Actor(userRepository.getUser(new UserName("daniel")).getUserName().toString());
		Actor admin = new Actor(userRepository.getUser(new UserName("admin")).getUserName().toString());
		AddRoleToUser addRoleToUserCommand = new AddRoleToUser("daniel", "entwickler", true);
		securityAdmin.addRoleToUser(admin, addRoleToUserCommand);
		PermissionData permissionData = new PermissionData("env1", "serv1", Action.start.name());
		AddPermissionToRole addPermissionToRoleCommand = new AddPermissionToRole("entwickler", permissionData);
		securityAdmin.addPermissionToRole(admin, addPermissionToRoleCommand);
		securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv1"), Action.start, actor);
		try {
			securityAdmin.proofPerformAction(	new EnvironmentName("env2"),
												new ServiceName("serv1"),
												Action.start,
												actor);
			fail();
		} catch (NoPermission e) {
			// wanted
		}
		try {
			securityAdmin.proofPerformAction(	new EnvironmentName("env1"),
												new ServiceName("serv2"),
												Action.start,
												actor);
			fail();
		} catch (NoPermission e) {
			// wanted
		}
		try {
			securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv1"), Action.stop, actor);
			fail();
		} catch (NoPermission e) {
			// wanted
		}
	}

	@Test
	public void addPermissiontoRole() {
		Actor admin = new Actor(userRepository.getUser(new UserName("admin")).getUserName().toString());
		Actor actor = new Actor(userRepository.getUser(new UserName("daniel")).getUserName().toString());
		PermissionData permissionData = new PermissionData("env1", "serv1", Action.start.name());
		AddPermissionToRole addPermissionToRoleCommand = new AddPermissionToRole("entwickler", permissionData);
		try {
			securityAdmin.addPermissionToRole(actor, addPermissionToRoleCommand);
			fail();
		} catch (NoPermission e) {
			// wanted
		}
		securityAdmin.addPermissionToRole(admin, addPermissionToRoleCommand);
	}

	@Test
	public void addRoleToUser() {
		Actor actor = new Actor(userRepository.getUser(new UserName("daniel")).getUserName().toString());
		Actor actor2 = new Actor(userRepository.getUser(new UserName("alex")).getUserName().toString());
		Actor actor3 = new Actor(userRepository.getUser(new UserName("user1")).getUserName().toString());
		Actor actor4 = new Actor(userRepository.getUser(new UserName("user2")).getUserName().toString());
		Actor admin = new Actor(userRepository.getUser(new UserName("admin")).getUserName().toString());
		AddRoleToUser addRoleToUserCommand = new AddRoleToUser("daniel", "entwickler", false);
		AddRoleToUser addRoleToUserCommand2 = new AddRoleToUser("user2", "entwickler", true);
		securityAdmin.addRoleToUser(admin, addRoleToUserCommand2);
		try {
			securityAdmin.addRoleToUser(actor, addRoleToUserCommand);
			fail();
		} catch (NoPermission e) {
			// wanted
		}
		try {
			securityAdmin.addRoleToUser(actor2, addRoleToUserCommand);
			fail();
		} catch (NoPermission e) {
			// wanted
		}
		try {
			securityAdmin.addRoleToUser(actor3, addRoleToUserCommand);
			fail();
		} catch (NoPermission e) {
			// wanted
		}
		securityAdmin.addRoleToUser(actor4, addRoleToUserCommand);
	}


	/**
	 * cleanup table entries
	 */
	@After
	public void cleanup() {
		Query d1 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICEINSTANCE");
		d1.executeUpdate();
		Query d2 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICE");
		d2.executeUpdate();
		Query d3 = super.getEntityManager().createNativeQuery("DROP TABLE HOST");
		d3.executeUpdate();
		Query d4 = super.getEntityManager().createNativeQuery("DROP TABLE ENVIRONMENT");
		d4.executeUpdate();
		Query d5 = super.getEntityManager().createNativeQuery("DROP TABLE SCRIPT");
		d5.executeUpdate();
		Query d7 = super.getEntityManager().createNativeQuery("DROP TABLE USER_ROLE");
		d7.executeUpdate();
		Query d8 = super.getEntityManager().createNativeQuery("DROP TABLE USER_");
		d8.executeUpdate();
		Query d10 = super.getEntityManager().createNativeQuery("DROP TABLE PERMISSION_ROLE"); // jpa generated table
		d10.executeUpdate();
		Query d9 = super.getEntityManager().createNativeQuery("DROP TABLE ROLE");
		d9.executeUpdate();
		Query d6 = super.getEntityManager().createNativeQuery("DROP TABLE PERMISSION");
		d6.executeUpdate();
	}

}
