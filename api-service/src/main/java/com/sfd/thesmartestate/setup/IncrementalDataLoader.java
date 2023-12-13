package com.sfd.thesmartestate.setup;

import com.sfd.thesmartestate.common.entities.Role;
import com.sfd.thesmartestate.common.services.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Configuration
@Transactional
@Slf4j
public class IncrementalDataLoader {
    public IncrementalDataLoader(final RoleService roleService) {
        updateRoles(roleService);
    }

    private void updateRoles(RoleService roleService) {
        List<Role> roles = roleService.findAllWithoutPrefixRole();
        for (Role role : roles) {
            if (!role.getName().startsWith("ROLE_")) {
                Role r = roleService.findByName("ROLE_" + role.getName());
                if (Objects.isNull(r)) {
                    roleService.update(role);
                }
            }
        }
    }
}
