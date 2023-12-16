package com.sfd.thesmartestate.lms.services;


import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.common.LeadEvents;
import com.sfd.thesmartestate.common.responsemapper.LeadResponseMapper;
import com.sfd.thesmartestate.common.responsemapper.ResponseDto;
import com.sfd.thesmartestate.importdata.dto.ErrorDto;
import com.sfd.thesmartestate.lms.dto.*;
import com.sfd.thesmartestate.lms.entities.Comment;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.lms.followups.Followup;
import com.sfd.thesmartestate.lms.followups.FollowupService;
import com.sfd.thesmartestate.lms.helpers.PageableFilterRequestHelper;
import com.sfd.thesmartestate.lms.repositories.LeadRepository;
import com.sfd.thesmartestate.notifications.NotificationMessage;
import com.sfd.thesmartestate.notifications.entities.OneTimePassword;
import com.sfd.thesmartestate.notifications.enums.NotificationType;
import com.sfd.thesmartestate.notifications.enums.OTPTarget;
import com.sfd.thesmartestate.notifications.enums.OTPType;
import com.sfd.thesmartestate.notifications.services.NotificationManager;
import com.sfd.thesmartestate.notifications.services.OTPService;
import com.sfd.thesmartestate.notifications.web.EmailNotificationMessage;
import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.projects.services.ProjectService;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.services.EmployeeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressFBWarnings("EI_EXPOSE_REP")

public class LeadServiceImpl implements LeadService {
    private final LeadUpdateService leadUpdateService;
    private final LeadRepository leadRepository;
    private final ProjectService projectService;
    private final EmployeeService employeeService;
    private final LeadStatusService leadStatusService;
    private final PageableFilterRequestHelper pageableFilterRequestHelper;
    private final DeactivationReasonService deactivationReasonService;
    private final OTPService otpService;
    private final NotificationManager notificationManager;
    private final FollowupService followupService;
    private final LeadServiceHelper leadServiceHelper;

    @Value("${notification.otp.recipients}")
    private String otpRecipients;

    @Override
    public List<Lead> findAll() {
        Employee employee = employeeService.findLoggedInEmployee();
        if (employee.isAdmin() || employee.isSuperAdmin()) {
            return leadRepository.findAll();
        }
        return leadRepository.findByAssignedTo(employee).stream()
                .filter(l -> !Constants.DEACTIVE.equalsIgnoreCase(l.getStatus().getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Lead create(Lead lead) {
        if (Objects.nonNull(lead)) {
            leadUpdateService.validateDuplicateLeads(lead);
            leadServiceHelper.addProjectDetails(lead);
            leadServiceHelper.addInitialComment(lead);
            leadServiceHelper.addBasicDetails(lead);
            leadServiceHelper.addBudgetDetails(lead);
            leadServiceHelper.addCustomerDetails(lead);
            leadServiceHelper.addMetadataDetails(lead);
            if (lead.getAssignedTo() == null) {
                leadServiceHelper.addLeadAssignment(lead);
            } else {
                leadServiceHelper.addAssignedToDetails(lead);
            }
            return leadRepository.saveAndFlush(lead);
        }
        throw new LeadException("Lead can't not be null");
    }

    @Override
    public Lead findById(Long id) {
        return leadRepository.findById(id).orElseThrow(() -> new LeadException("Lead not found for id " + id));
    }

    @Override
    @Transactional
    public void delete(Lead lead) {
        leadRepository.delete(lead);
    }

    @Override
    public void save(Lead lead) {
        leadRepository.save(lead);
    }

    @Override
    @Transactional
    public void saveAll(Collection<Lead> leads) {
        leadRepository.saveAllAndFlush(leads);
    }

    @Override

    public Lead findByCustomerPhoneAndProjectName(String phone, String projectName) {
        return leadRepository.findByCustomerPhoneAndProjectName(phone, projectName).orElse(null);
    }

    @Override
    public List<Lead> findByCustomerPhoneAndProjectId(String phone, Long projectId) {
        return leadRepository.findByCustomerPhoneAndProjectId(phone, projectId);
    }

    public TransferLeadResponse transferLeads(TransferLeadDto transferLeadDto, LeadEvents leadEvent) {
        switch (transferLeadDto.getLeadTransferType()) {
            case COPY:
                return copy(transferLeadDto);
            case TRANSFER:
                return transfer(transferLeadDto, leadEvent);
        }
        throw new LeadException("Unable to " + transferLeadDto.getLeadTransferType().name() + " lead");
    }

    private TransferLeadResponse copy(TransferLeadDto transferLeadDto) {
        Employee loggedInEmployee = employeeService.findLoggedInEmployee();
        log.info("Copying " + transferLeadDto.getLeadList() +
                " leads to " + transferLeadDto.getProject()
                + " all will be assigned to " + transferLeadDto.getAssignedTo().getUsername());
        Employee assignedEmployee = employeeService.findById(transferLeadDto.getAssignedTo().getId());
        Project project = projectService.findById(transferLeadDto.getProject().getId());
        TransferLeadResponse transferLeadResponse = new TransferLeadResponse();
        List<ErrorDto> errors = new ArrayList<>();

        List<Lead> clonedLeads = leadRepository.findAllById(transferLeadDto.getLeadList())
                .parallelStream()
                .map(oldLead -> (Lead) oldLead.clone())
                .collect(Collectors.toList());

        List<Lead> updatedLeads = new ArrayList<>();
        for (Lead lead : clonedLeads) {
            List<Lead> existingLead = leadRepository.findByCustomerPhoneAndProjectId(lead.getCustomer().getPhone(), lead.getProject().getId());
            if (!existingLead.isEmpty()) {
                errors.add(new ErrorDto("Already present in system for given project and customer " + lead.getCustomer().getPhone() + " so skipping record", "IGNORED", -1, existingLead.get(0).getId()));
                continue;
            }

            lead.setId(null);
            lead.setAssignedTo(assignedEmployee);
            lead.setProject(project);
            lead.setComments(new HashSet<>());
            lead.setCreatedAt(LocalDateTime.now());
            lead.setCreatedBy(loggedInEmployee);
            lead.setUpdatedBy(loggedInEmployee);
            lead.setLastUpdateAt(LocalDateTime.now());
            lead.setLeadAssignHistory(new HashSet<>());
            lead.setCalls(new HashSet<>());
            lead.setDeactivationReason(null);
            lead.setSiteVisit(false);
            updatedLeads.add(lead);
        }
        transferLeadResponse.setLeadList(leadRepository.saveAllAndFlush(updatedLeads));
        transferLeadResponse.setErrors(errors);
        return transferLeadResponse;
    }

    @SuppressFBWarnings("RC_REF_COMPARISON")
    private TransferLeadResponse transfer(TransferLeadDto transferLeadDto, LeadEvents leadEvent) {
        List<Lead> updatedLead = new ArrayList<>();
        TransferLeadResponse transferLeadResponse = new TransferLeadResponse();
        List<ErrorDto> errors = new ArrayList<>();
        for (Long leadId : transferLeadDto.getLeadList()) {
            Lead currentLead = leadRepository.findById(leadId).orElse(null);
            // check if for same customer and project in which we are transferring lead is already in system
            List<Lead> existingLead = leadRepository
                    .findByCustomerPhoneAndProjectId(currentLead.getCustomer().getPhone(), transferLeadDto.getProject().getId());
            Optional<Lead> isAlreadyInSystem = existingLead.stream().filter(lead -> Objects.equals(lead.getProject().getId(), transferLeadDto.getProject().getId())).findAny();

            if (isAlreadyInSystem.isPresent()) {
                // if lead is already in system check if assignee is same or not. If not then change the assignee
                Lead lead = isAlreadyInSystem.get();
                if (Objects.equals(lead.getAssignedTo().getId(), transferLeadDto.getAssignedTo().getId()) || !Objects.equals(lead.getId(), leadId)) {
                    log.info("Already present in system for given customer and already assigned to given user");
                    errors.add(new ErrorDto("Already present in system for given Project and customer ::" + currentLead.getCustomer().getPhone() + " and assigned to given user skipping record", "IGNORED", -1, currentLead.getId()));
                } else {
                    // assignee is not same so assign to given customer
                    Employee assignedEmployee = employeeService.findById(transferLeadDto.getAssignedTo().getId());
                    currentLead.setAssignedTo(assignedEmployee);
                    currentLead.setStatus(leadStatusService.findByName(Constants.ACTIVE));
                    updatedLead.add(leadUpdateService.update(currentLead, leadEvent));
                }
            } else {
                // Lead is not present in the given project so change the project of lead and assign the user
                Employee assignedEmployee = employeeService.findById(transferLeadDto.getAssignedTo().getId());
                Project project = projectService.findById(transferLeadDto.getProject().getId());
                currentLead.setAssignedTo(assignedEmployee);
                currentLead.setProject(project);
                currentLead.setStatus(leadStatusService.findByName(Constants.ACTIVE));
                updatedLead.add(leadUpdateService.update(currentLead, leadEvent));
            }
        }
        transferLeadResponse.setErrors(errors);
        transferLeadResponse.setLeadList(updatedLead);
        return transferLeadResponse;
    }

    @Override
    public PageableLeadsRespone findPageableAll(PageableFilterDto pageableFilterDto) {
        TypedQuery<Lead> query = pageableFilterRequestHelper.createExecutableQuery(pageableFilterDto);
        List<Lead> leads = query.getResultList();
        List<LeadResponseDto> leadResponseDtoList = leads.stream()
                .map(lead ->
                        {
                            Set<Followup> followups = followupService.findFollowupByLead(lead);
                            return LeadResponseMapper.mapToLeadResponse(lead, followups);
                        }
                ).collect(Collectors.toList());
        return new PageableLeadsRespone(pageableFilterRequestHelper.getCountOfAvailableItems(pageableFilterDto), leadResponseDtoList);

    }

    @Override
    public Lead deactivateLead(DeactivateLeadDto deactivateLeadDto, Long leadId) {
        Lead lead = leadRepository.findById(leadId).orElse(null);
        assert lead != null;
        lead.setStatus(leadStatusService.findByName(Constants.DEACTIVE));
        lead.setDeactivationReason(deactivationReasonService.findById(deactivateLeadDto.getDeactivationReason().getId()));
        Comment comment = deactivateLeadDto.getComment();
        comment.setCreatedBy(employeeService.findLoggedInEmployee());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setCommentType("De-Active");
        lead.getComments().add(comment);
        return leadUpdateService.update(lead, LeadEvents.DEACTIVE_LEAD);
    }

    @Override
    public Long findAllCount() {
        Employee loggedInEmployee = employeeService.findLoggedInEmployee();
        if (loggedInEmployee.isSuperAdmin() || loggedInEmployee.isAdmin()) {
            return leadRepository.count();
        }
        return leadRepository.countByAssignee(loggedInEmployee.getId());
    }

    @Override
    public Long findCountByAllActiveStatus() {
        Employee loggedInEmployee = employeeService.findLoggedInEmployee();
        if (loggedInEmployee.isSuperAdmin() || loggedInEmployee.isAdmin()) {
            return leadRepository.findCountByStatusForAllActives();
        }
        return leadRepository.countByAssigneeAndAllActiveStatus(loggedInEmployee.getId());
    }

    @Override
    public ResponseDto generateOtpToDownloadLeads() {
        log.info("Creating OTP for password change");
        String otp = otpService.generateOTP();
        Employee employee = employeeService.findLoggedInEmployee();

        otpService.saveOneTimePassword(employeeService.findLoggedInEmployee(), otp, OTPTarget.EMAIL, OTPType.DOWNLOAD_REPORT);

        String message = "Dear User! Your OTP for Download leads  is here. " + otp +
                " This is valid for 15 min." + " OTP generated by user - " + employee.getName();
        String subject = "OTP: Download Leads";
        boolean isTransactional = true;

        NotificationMessage notificationMessage = new EmailNotificationMessage(message, subject, isTransactional);
        notificationManager.send(notificationMessage, NotificationType.EMAIL, Set.of(otpRecipients.split(",")));
        log.info("OTP sent to download report");
        return ResponseDto.builder().message(notificationMessage.getSubject()).build();
    }

    @Override
    public OneTimePassword validateOtpDownloadLeadReport(String otp) {
        Employee employee = employeeService.findLoggedInEmployee();
        return validateOTP(employee, otp);
    }

    public OneTimePassword validateOTP(Employee employee, String oneTimePassword) {
        log.info("Validating OPT for password change for " + employee.getUsername());
        OneTimePassword otp = otpService.findByUsername(employee.getUsername());
        otpService.checkOtpUsed(otp);
        otpService.checkOtpValue(oneTimePassword, otp);
        otpService.checkOtpExpired(otp);
        otpService.markOTPUsed(otp);
        log.info("OTP validated");
        return otp;
    }
}
