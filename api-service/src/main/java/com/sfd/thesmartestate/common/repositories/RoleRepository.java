package com.sfd.thesmartestate.common.repositories;


import com.sfd.thesmartestate.common.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);

    @Query("SELECT r from Role r WHERE r.name NOT LIKE 'ROLE_%'")
    List<Role> findAllWithoutPrefixRole();
}
