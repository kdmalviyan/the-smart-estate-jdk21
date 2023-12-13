package com.sfd.thesmartestate.uipermissions;

import java.util.List;
import java.util.Set;

public interface PermissionTabService {

    PermissionTab findByPath(String path);

    PermissionTab findById(Long id);

    List<PermissionTab> findAll();

    void saveAll(List<PermissionTab> permissionTabs);

    PermissionTab addPermission(PermissionTab permissionTab, String parentModuleName);

    Set<PermissionTab> addPermissionToRole(PermissionTab permissionTab, String roleName, String addRemove);

}
