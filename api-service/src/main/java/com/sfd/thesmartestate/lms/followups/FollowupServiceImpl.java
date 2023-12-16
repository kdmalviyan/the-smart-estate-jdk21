package com.sfd.thesmartestate.lms.followups;

import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.common.LeadEvents;
import com.sfd.thesmartestate.common.responsemapper.FollowUpResponseMapper;
import com.sfd.thesmartestate.common.services.LeadEventService;
import com.sfd.thesmartestate.lms.dto.FollowupResponseDto;
import com.sfd.thesmartestate.lms.dto.PageableFilterDto;
import com.sfd.thesmartestate.lms.dto.PageableFollowupResponse;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.lms.services.LeadStatusService;
import com.sfd.thesmartestate.lms.services.LeadUpdateService;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.services.UserService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class FollowupServiceImpl implements FollowupService {
    private final FollowupRepository repository;
    private final UserService userService;
    private final LeadEventService leadEventService;
    private final LeadStatusService leadStatusService;
    private final LeadUpdateService leadUpdateService;

    @Override
    public List<Followup> findAll() {
        return repository.findAll();
    }

    @Override
    public Followup create(Followup followup) {
        followup.setCreatedBy(userService.findLoggedInUser());
        followup.setCreatedAt(LocalDateTime.now());
        return repository.save(followup);
    }

    @Override
    public Followup create(String followupTime, Lead lead) {
        Followup followup = new Followup();
        followup.setLead(lead);
        followup.setFollowupTime(LocalDateTime.parse(followupTime, DateTimeFormatter.ofPattern("M/dd/yyyy, h:mm:ss a", Locale.ENGLISH)));
        followup.setFollowupMessage("Setting Follow up for time " + followup.getFollowupTime());

        List<Followup> followups = repository.findByLeadAndIsOpen(lead, true);
        if (followups.size() > 0) {
            throw new LeadException("More than one one followup exists for Lead please close them first");
        }
        followup = create(followup);
        return followup;
    }


    @Override
    public Followup update(Followup followup) {
        followup.setLastUpdateAt(LocalDateTime.now());
        followup.setUpdatedBy(userService.findLoggedInUser());
        return repository.save(followup);
    }

    @Override
    public Followup findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Followup followup) {
        repository.delete(followup);
    }

    @Override
    public Long findAllOpenCount() {
        LocalDateTime followupStart = LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        LocalDateTime followupEnd = LocalDateTime.now()
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(0);
        return findByIsOpenAndFollowupTimeGreaterThan(true, followupStart, followupEnd);
    }

    @Override
    public Long findByIsOpenAndFollowupTimeGreaterThan(boolean isOpen, LocalDateTime followupStart, LocalDateTime followupEnd) {

        Employee loggedInEmployee = userService.findLoggedInUser();
        if (loggedInEmployee.isSuperAdmin() || loggedInEmployee.isAdmin()) {
            return repository.findByIsOpenAndFollowupTimeBetweenStartAndEndDate(isOpen, followupStart, followupEnd);
        }
        return repository.findByUserIsOpenAndFollowupTimeBetweenStartAndEndDate(isOpen, followupStart, followupEnd, loggedInEmployee.getId());
    }

    @Override
    public PageableFollowupResponse findAllFollowupPageable(PageableFilterDto filterRequestPayload) {
        LocalDateTime followupStartTime = filterRequestPayload.getStartDate();
        LocalDateTime followupEndTime = filterRequestPayload.getEndDate();
        Double startAmount = Objects.nonNull(filterRequestPayload.getBudgetFrom()) ? filterRequestPayload.getBudgetFrom() : 0D;
        Double endAmount = Objects.nonNull(filterRequestPayload.getBudgetTo()) ? filterRequestPayload.getBudgetTo() : Double.MAX_VALUE;

        String searchText = "";
        if (Objects.nonNull(filterRequestPayload.getSearchText())) {
            searchText = filterRequestPayload.getSearchText();
        }

        String deactivationReason = "";
        if (Objects.nonNull(filterRequestPayload.getDeactivationReason())) {
            deactivationReason = filterRequestPayload.getDeactivationReason();
        }

        String status = "";
        if (Objects.nonNull(filterRequestPayload.getStatus())) {
            status = filterRequestPayload.getStatus();
        }
        Page<Followup> page = getFollowupPage(filterRequestPayload, followupStartTime, followupEndTime, startAmount,
                endAmount, searchText, deactivationReason, status, userService.findLoggedInUser());

        List<FollowupResponseDto> leadResponseDtoList = page.getContent().stream()
                .map(FollowUpResponseMapper::mapToFollowupResponse
                ).collect(Collectors.toList());

        return new PageableFollowupResponse(
                page.getTotalElements(),
                leadResponseDtoList);
    }

    private Page<Followup> getFollowupPage(PageableFilterDto filterRequestPayload, LocalDateTime followupStartTime,
                                           LocalDateTime followupEndTime, Double startAmount, Double endAmount, String searchText,
                                           String deactivationReason, String status, Employee loggedInEmployee) {
        boolean isOpen = filterRequestPayload.isOpen();

        return loggedInEmployee.isAdmin() || loggedInEmployee.isSuperAdmin()
                ?
                getAdminFollowupPage(filterRequestPayload, followupStartTime,
                        followupEndTime, startAmount, endAmount, searchText,
                        deactivationReason, status, isOpen)
                :
                getNonAdminFollowupPage(filterRequestPayload, followupStartTime,
                        followupEndTime, startAmount, endAmount, searchText,
                        deactivationReason, status, loggedInEmployee.getId(), isOpen);
    }

    private Page<Followup> getNonAdminFollowupPage(PageableFilterDto filterRequestPayload,
                                                   LocalDateTime followupStartTime,
                                                   LocalDateTime followupEndTime,
                                                   Double startAmount,
                                                   Double endAmount,
                                                   String searchText,
                                                   String deactivationReason,
                                                   String status,
                                                   Long userId,
                                                   boolean isOpen) {
        return repository.findAllPageable(
                isOpen, followupStartTime,
                followupEndTime, userId,
                searchText,
                status, createPageable(filterRequestPayload));
    }

    private Page<Followup> getAdminFollowupPage(PageableFilterDto filterRequestPayload, LocalDateTime followupStartTime,
                                                LocalDateTime followupEndTime, Double startAmount, Double endAmount,
                                                String searchText, String deactivationReason, String status, boolean isOpen) {
        return Objects.isNull(filterRequestPayload.getAssignedTo()) || Objects.equals("NO", filterRequestPayload.getAssignedTo())
                ?
                repository.findAllPageableForAdmin(
                        isOpen,
                        followupStartTime, followupEndTime,
                        searchText,
                        status, createPageable(filterRequestPayload))
                :
                getNonAdminFollowupPage(filterRequestPayload,
                        followupStartTime, followupEndTime,
                        startAmount, endAmount,
                        searchText, deactivationReason,
                        status, Long.parseLong(filterRequestPayload.getAssignedTo()),
                        isOpen);
    }

    private Pageable createPageable(PageableFilterDto filterRequestPayload) {
        return PageRequest.of(filterRequestPayload.getPageNumber(),
                filterRequestPayload.getPageSize(),
                Sort.by(Sort.Direction.fromString(filterRequestPayload.getOrderDir()), filterRequestPayload.getOrderColumn()));
    }

    @Override
    public Followup findOpenFollowupByLead(Lead lead) {
        List<Followup> followups = repository.findByLeadAndIsOpen(lead, true);
        if (followups.size() > 0) {
            return followups.get(0);
        }
        throw new LeadException("More than one one followup exists for Lead " + lead.getId());
    }

    @Override
    public Set<Followup> findFollowupByLead(Lead lead) {
        return repository.findByLead(lead);
    }

    @Override
    public void updateLeadStatus(Followup followup) {
        Lead lead = followup.getLead();
        lead.setUpdatedBy(userService.findLoggedInUser());
        lead.setStatus(leadStatusService.findByName(Constants.FOLLOW_UP_COMPLETE));
        leadEventService.setLeadEventComment(lead, LeadEvents.STATUS_CHANGED, followup);
        lead = leadUpdateService.updateLeadFollowup(lead, followup);
        followup.setLead(lead);
    }
}