package com.icarapp.icar.service;

import java.util.List;

import com.icarapp.icar.model.Role;
import com.icarapp.icar.model.User;


public interface RoleService {
    List<Role> getRoles();
    void createRole(Role theRole);

    void deleteRole(Long id);
    Role findByName(String name);

    User removeUserFromRole(Long userId, Long roleId);
    User assignRoleToUser(Long userId, Long roleId);
    Role removeAllUsersFromRole(Long roleId);
}
