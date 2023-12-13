package com.sfd.thesmartestate.uipermissions;

import com.sfd.thesmartestate.common.entities.Role;
import com.sfd.thesmartestate.common.services.RoleService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressFBWarnings("EI_EXPOSE_REP")

public class PermissionTabServiceImpl implements PermissionTabService {
    private final PermissionTabRepository permissionTabRepository;
    private final RoleService roleService;

    @Override
    public PermissionTab findByPath(String name) {
        return permissionTabRepository.findByPath(name);
    }

    @Override
    public PermissionTab findById(Long id) {
        return permissionTabRepository.findById(id).orElse(null);
    }

    @Override
    public List<PermissionTab> findAll() {
        return permissionTabRepository.findAll();
    }

    @Override
    public void saveAll(List<PermissionTab> permissionTabs) {
        permissionTabRepository.saveAll(permissionTabs);
    }

    @Override
    public PermissionTab addPermission(PermissionTab permissionTab, String parentModuleName) {
        if (permissionTab.isTopMenu()) {
            permissionTab.setGroupTitle(false);
            return permissionTabRepository.save(permissionTab);
        } else {
            PermissionTab parentPermissionTab = findByModuleName(parentModuleName);
            permissionTab.setGroupTitle(false);
            parentPermissionTab.getSubmenu().add(permissionTab);
            return permissionTabRepository.save(parentPermissionTab);
        }
    }

    @Override
    public Set<PermissionTab> addPermissionToRole(PermissionTab permissionTab, String roleName, String addRemove) {
        Role role = roleService.addRemovePermissionsToRole(roleName, permissionTab, addRemove);
        return role.getUiPermissions();
    }

    public PermissionTab findByModuleName(String moduleName) {
        return permissionTabRepository.findByModuleName(moduleName);
    }

}
