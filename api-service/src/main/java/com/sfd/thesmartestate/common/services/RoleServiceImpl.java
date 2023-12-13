package com.sfd.thesmartestate.common.services;

import com.sfd.thesmartestate.common.entities.Role;
import com.sfd.thesmartestate.common.repositories.RoleRepository;
import com.sfd.thesmartestate.uipermissions.PermissionTab;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void saveAll(List<Role> roles) {
        repository.saveAll(roles);
    }

    @Override
    public void save(Role role) {
        repository.save(role);
    }

    @Override
    public Role findByName(String roleName) {
        return repository.findByName(roleName);
    }

    @Override
    public List<Role> findAll() {
        return repository.findAll();
    }

    @Override
    public Role update(Role role) {
        return repository.save(role);
    }

    @Override
    public void assignPermissionsToRole(String roleName, List<PermissionTab> permissionTabs) {
        Role role = findByName(roleName);
        if (Objects.nonNull(role)) {
            if (Objects.isNull(role.getUiPermissions()) || role.getUiPermissions().size() == 0) {
                role.setUiPermissions(new HashSet<>(permissionTabs));
                update(role);
            }
        }
    }

    @Override
    public Role addRemovePermissionsToRole(String roleName, PermissionTab permissionTab, String addRemove) {
        Role role = findByName(roleName);
        Set<PermissionTab> permissionTabs = role.getUiPermissions();
        if ("ADD".equalsIgnoreCase(addRemove)) {
            //IF SUBMENU
            //PARENT ME ADD KAR DO
            //loop on metadata submenu
            permissionTabs.add(permissionTab);

        } else {
            permissionTabs.remove(permissionTab);
        }
        role.setUiPermissions(new HashSet<>(permissionTabs));
        return update(role);
    }

    @Override
    public List<Role> findAllWithoutPrefixRole() {
        return repository.findAllWithoutPrefixRole();
    }
}
