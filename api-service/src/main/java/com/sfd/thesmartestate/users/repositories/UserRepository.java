package com.sfd.thesmartestate.users.repositories;

import com.sfd.thesmartestate.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor {
    User findByUsername(String username);

    User findByName(String name);

    List<User> getUserByNameStartsWithAndProjectName(String name, String projectName);

    User findByEmail(String username);

    List<User> findByProjectId(Long projectId);
}