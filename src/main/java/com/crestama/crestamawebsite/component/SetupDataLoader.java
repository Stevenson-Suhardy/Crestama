package com.crestama.crestamawebsite.component;

import com.crestama.crestamawebsite.entity.Permission;
import com.crestama.crestamawebsite.entity.Role;
import com.crestama.crestamawebsite.entity.User;
import com.crestama.crestamawebsite.service.permission.PermissionService;
import com.crestama.crestamawebsite.service.role.RoleService;
import com.crestama.crestamawebsite.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    private UserService userService;
    private RoleService roleService;
    private PermissionService permissionService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SetupDataLoader(UserService userService, RoleService roleService,
                           PermissionService permissionService,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        Permission readPermission = createPermissionIfNotFound("READ_PERMISSION");
        Permission writePermission = createPermissionIfNotFound("WRITE_PERMISSION");
        Permission deletePermission = createPermissionIfNotFound("DELETE_PERMISSION");

        List<Permission> adminPermissions = Arrays.asList(
                readPermission, writePermission, deletePermission
        );
        List<Permission> staffPermissions = Arrays.asList(
                readPermission, writePermission
        );
        createRoleIfNotFound("ROLE_ADMIN", adminPermissions);
        createRoleIfNotFound("ROLE_STAFF", staffPermissions);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPermission));

        User admin = userService.findByEmail("admin");

        if (admin == null) {
            Role adminRole = roleService.findByName("ROLE_ADMIN");
            User user = new User();

            user.setFirstName("admin");
            user.setLastName("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setEmail("admin");
            user.setRoles(Arrays.asList(adminRole));
            user.setEnabled(true);

            userService.save(user);
        }

        alreadySetup = true;
    }

    @Transactional
    public Permission createPermissionIfNotFound(String name) {
        Permission permission = permissionService.findByName(name);

        if (permission == null) {
            permission = new Permission(name);
            permissionService.save(permission);
        }
        return permission;
    }

    @Transactional
    public Role createRoleIfNotFound(String name, Collection<Permission> permissions) {
        Role role = roleService.findByName(name);

        if (role == null) {
            role = new Role(name);
            role.setPermissions(permissions);
            roleService.save(role);
        }
        return role;
    }
}
