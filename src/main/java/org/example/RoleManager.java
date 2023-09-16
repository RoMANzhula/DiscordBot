package org.example;

import net.dv8tion.jda.api.entities.Role;

import java.util.HashMap;
import java.util.Map;

public class RoleManager {
    private static final Map<String, Role> availableRoles = new HashMap<>();

    public static Map<String, Role> getAvailableRoles() {
        return availableRoles;
    }

    public static void initializeRoles(Iterable<Role> rolesOnServer) {
        availableRoles.clear();
        for (Role role : rolesOnServer) {
            addRole(role.getName(), role);
        }
    }

    public static void addRole(String roleName, Role role) {
        availableRoles.put(roleName.toLowerCase(), role);
    }

    public static void removeRole(String roleName) {
        availableRoles.remove(roleName.toLowerCase());
    }
}
