package com.sfd.thesmartestate.common.services;

import com.sfd.thesmartestate.common.entities.Role;
import com.sfd.thesmartestate.uipermissions.PermissionTab;

import java.util.List;

public interface RoleService {
    long count();

    void saveAll(List<Role> roleList);

    void save(Role role);

    Role findByName(String roleName);

    List<Role> findAll();

    Role update(Role role);
    void assignPermissionsToRole(String roleName, List<PermissionTab> permissionTabs);
    Role addRemovePermissionsToRole(String roleName, PermissionTab permissionTabs, String addRemove);

    List<Role> findAllWithoutPrefixRole();
}
