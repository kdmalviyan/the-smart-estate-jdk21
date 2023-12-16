package com.sfd.thesmartestate.users.repositories;

import com.sfd.thesmartestate.users.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor {
    Employee findByUsername(String username);

    Employee findByName(String name);

    List<Employee> getUserByNameStartsWithAndProjectName(String name, String projectName);

    Employee findByEmail(String username);

    List<Employee> findByProjectId(Long projectId);

    Employee findByEmployeeUniqueId(String employeeUniqueId);
}