package com.sfd.thesmartestate.lms.followups;

import com.sfd.thesmartestate.lms.entities.Lead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface FollowupRepository extends JpaRepository<Followup, Long> {
    @Query(value = "select count(*) from tb_followups where is_open = :isOpen AND followup_time >= :followupStart " +
            "AND followup_time <= :followupEnd", nativeQuery = true)
    Long findByIsOpenAndFollowupTimeBetweenStartAndEndDate(boolean isOpen, LocalDateTime followupStart, LocalDateTime followupEnd);

    @Query(value = "select count(*) from Followup f where f.isOpen = :isOpen AND followupTime >= :followupStart " +
            "AND f.followupTime <= :followupEnd and f.lead.assignedTo.id= :userId")
    Long findByUserIsOpenAndFollowupTimeBetweenStartAndEndDate(boolean isOpen, LocalDateTime followupStart, LocalDateTime followupEnd,long userId);

    List<Followup> findByLeadAndIsOpen(Lead lead, boolean isOpen);

    Set<Followup> findByLead(Lead lead);

    @Query(value = "SELECT f FROM Followup f" +
            " WHERE f.isOpen = :isOpen" +
            " AND (f.followupTime >= :followupStartTime AND f.followupTime < :followupEndTime)" +
            " AND f.lead.assignedTo.id = :assignedTo" +
           // " AND (f.lead.budget.absoluteStartAmount >= :startAmount AND f.lead.budget.absoluteEndAmount < :endAmount)" +
            " AND (f.lead.customer.name LIKE CONCAT('%',:searchText,'%') " +
            "OR f.lead.customer.email LIKE CONCAT('%',:searchText,'%') " +
            "OR f.lead.customer.phone LIKE CONCAT('%',:searchText,'%'))" +
           // " AND f.lead.deactivationReason.name LIKE CONCAT('%',:deactivationReason,'%')" +
            " AND f.lead.status.name LIKE CONCAT('%',:status,'%')")
    Page<Followup> findAllPageable(@Param("isOpen") boolean isOpen,
                                   @Param("followupStartTime") LocalDateTime followupStartTime,
                                   @Param("followupEndTime") LocalDateTime followupEndTime,
                                   @Param("assignedTo") Long assignedTo,
                                   //@Param("startAmount") Double startAmount,
                                  // @Param("endAmount") Double endAmount,
                                   @Param("searchText") String searchText,
                                   //@Param("deactivationReason") String deactivationReason,
                                   @Param("status") String status,
                                   Pageable pageable);

    @Query(value = "SELECT f FROM Followup f" +
            " WHERE f.isOpen = :isOpen" +
            " AND (f.followupTime >= :followupStartTime AND f.followupTime < :followupEndTime)" +
           // " AND (f.lead.budget.absoluteStartAmount >= :startAmount AND f.lead.budget.absoluteEndAmount < :endAmount)" +
            " AND (f.lead.customer.name LIKE CONCAT('%',:searchText,'%') " +
            "OR f.lead.customer.email LIKE CONCAT('%',:searchText,'%') " +
            "OR f.lead.customer.phone LIKE CONCAT('%',:searchText,'%'))" +
          //  " AND f.lead.deactivationReason.name LIKE CONCAT('%',:deactivationReason,'%')" +
            " AND f.lead.status.name LIKE CONCAT('%',:status,'%')")
    Page<Followup> findAllPageableForAdmin(@Param("isOpen") boolean isOpen,
                                           @Param("followupStartTime") LocalDateTime followupStartTime,
                                           @Param("followupEndTime") LocalDateTime followupEndTime,
                                           @Param("searchText") String searchText,
                                           @Param("status") String status,
                                           Pageable pageable);

   @Query("delete from Followup f where f.lead.id= :leadId")
    void deleteByLeadId(@Param("leadId")long leadId);
}
