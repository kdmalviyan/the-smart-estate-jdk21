package com.sfd.thesmartestate.uipermissions;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionTabRepository extends JpaRepository<PermissionTab, Long> {
    PermissionTab findByPath(String path);

    PermissionTab findByModuleName(String moduleName);
}
