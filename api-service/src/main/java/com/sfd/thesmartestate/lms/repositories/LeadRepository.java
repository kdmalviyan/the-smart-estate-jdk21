package com.sfd.thesmartestate.lms.repositories;

import com.sfd.thesmartestate.adhoc.dto.DuplicateResponse;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.employee.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LeadRepository extends JpaRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {
    Optional<Lead> findByCustomerPhoneAndProject(String phone, Project project);

    Optional<Lead> findByCustomerPhoneAndProjectName(String phone, String projectName);

    List<Lead> findByCustomerPhoneAndProjectId(String phone, Long id);

    List<Lead> findByCustomerIdAndProjectId(Long phone, Long id);

    List<Lead> findByAssignedTo(Employee employee);

    @Query(value = "select count(*) from tb_leads l join tb_lead_status s on l.status_id = s.id where s.lead_status_name " +
            "in ('ACTIVE','IN-PROCESS','FOLLOW','FOLLOW-UP','FOLLOW-UP-EXPIRE') ", nativeQuery = true)
    Long findCountByStatusForAllActives();

    @Query(value = "select count(*) from tb_leads l join tb_lead_status s on l.status_id = s.id where s.lead_status_name= :statusName", nativeQuery = true)
    Long findCountByStatus(String statusName);


    @Query(value = "select count(*) from tb_leads l join tb_lead_status s on l.status_id = s.id " +
            "where s.lead_status_name= :statusName AND createdAt > :createdStartDate " +
            "AND createdAt < :createdEndDate", nativeQuery = true)
    Long findCountByStatusAndCreateStartEndDate(String statusName, LocalDateTime createdStartDate, LocalDateTime createdEndDate);

    @Query(value = "select count(*) from tb_leads l " +
            "where l.assignee_id= :id", nativeQuery = true)
    Long countByAssignee(Long id);

    @Query(value = "select count(*) from tb_leads l " +
            "join tb_lead_status s on l.status_id = s.id " +
            "where l.assignee_id= :id " +
            " AND s.lead_status_name in ('ACTIVE','IN-PROCESS','FOLLOW','FOLLOW-UP','FOLLOW-UP-EXPIRE') ", nativeQuery = true)
    Long countByAssigneeAndAllActiveStatus(Long id);

    @Query("FROM Lead where leadInventorySize = null")
    List<Lead> findByLeadWithNullInventorySize();

    @Query(value = "SELECT " +
            "new com.sfd.thesmartestate.adhoc.dto.DuplicateResponse(COUNT(l) as recordCount ," +
            "l.customer.id as customerId,l.project.id as projectId) " +
            "FROM Lead l " +
            "GROUP BY l.project.id,l.customer.id having COUNT(l) >1")
    List<DuplicateResponse> findCustomerAndPhoneDuplicateLeads();
}