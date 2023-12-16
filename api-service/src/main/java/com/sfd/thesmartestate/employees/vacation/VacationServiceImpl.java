package com.sfd.thesmartestate.employees.vacation;

import com.sfd.thesmartestate.employees.vacation.action.VacationAction;
import com.sfd.thesmartestate.employees.vacation.action.VacationActionService;
import com.sfd.thesmartestate.employees.vacation.exceptions.VacationException;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.entities.LoginDetails;
import com.sfd.thesmartestate.users.services.LoginDetailsService;
import com.sfd.thesmartestate.users.services.EmployeeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressFBWarnings("EI_EXPOSE_REP")

public class VacationServiceImpl implements VacationService {
    private final VacationRepository repository;
    private final VacationActionService vacationActionService;
    private final EmployeeService employeeService;
    private final LoginDetailsService loginDetailsService;
    @Override
    public Vacation create(Vacation vacation) {
        addApprover(vacation);
        vacation.validate();
        return repository.save(vacation);
    }

    @Override
    public Vacation reject(VacationUpdateDto vacationUpdateDto) {
        Vacation vacation = repository.findById(vacationUpdateDto.getVacationId())
                .orElseThrow(() -> new VacationException("Vacation not found"));
        VacationAction vacationAction = createVacationAction(vacationUpdateDto,
                VacationStatus.REJECTED.name());
        vacation.getActions().add(vacationAction);
        vacation.setStatus(VacationStatus.REJECTED);
       return repository.save(vacation);
    }

    @Override
    public Vacation approve(VacationUpdateDto vacationUpdateDto) {
        Vacation vacation = repository.findById(vacationUpdateDto.getUserId())
                .orElseThrow(() -> new VacationException("Vacation not found"));
        VacationAction vacationAction = createVacationAction(vacationUpdateDto,
                VacationStatus.APPROVED.name());
        vacation.getActions().add(vacationAction);
        vacation.setStatus(VacationStatus.APPROVED);
        return repository.save(vacation);
    }

    @Override
    public List<Vacation> findByStatus(VacationStatus vacationStatus) {
        Employee employee = employeeService.findLoggedInEmployee();
        return repository.findByStatusAndCreatedForUsername(vacationStatus, employee.getUsername());
    }

    @Override
    public Vacation changeStatus(Long vacationId, VacationStatus fromStatus, VacationStatus toStatus) {
        Vacation vacation = repository.findById(vacationId).orElseThrow(() -> new VacationException("Vacation not found"));
        if(!vacation.getStatus().equals(fromStatus)) {
            throw new VacationException("Invalid current status");
        }
        Employee employee = employeeService.findLoggedInEmployee();
        VacationAction vacationAction = createVacationAction(
                new VacationUpdateDto(vacation.getId(), employee.getId(),
                        "Status changed from " + fromStatus + " to " + toStatus),
                "Status changed");
        vacation.getActions().add(vacationAction);
        vacation.setStatus(toStatus);
        return vacation;
    }

    @Override
    public List<Vacation> findMyVacations() {
        Employee employee = employeeService.findLoggedInEmployee();
        return repository.findByCreatedForUsername(employee.getUsername());
    }

    @Override
    public List<Vacation> findVacationByApprover(Long userId) {
        return repository.findByApprover(userId);
    }

    @Override
    public List<Vacation> findVacationByApproverAndStatus(Long userId, VacationStatus status) {
        return repository.findVacationByApproverAndStatus(userId, status);
    }

    @Override
    public List<Vacation> findVacationApprovedByMe() {
        Employee employee = employeeService.findLoggedInEmployee();
        return repository.findVacationByApproverAndStatus(employee.getId(), VacationStatus.APPROVED);
    }

    @Override
    public List<Vacation> findVacationForMyApproval() {
        Employee employee = employeeService.findLoggedInEmployee();
        return repository.findVacationByApproverAndStatusNot(employee.getId(), VacationStatus.APPROVED);
    }

    private VacationAction createVacationAction(VacationUpdateDto vacationUpdateDto, String name) {
        VacationAction vacationAction = new VacationAction();
        vacationAction.setActionDateTime(LocalDateTime.now());
        vacationAction.setVacationId(vacationUpdateDto.getVacationId());
        vacationAction.setComment(vacationUpdateDto.getComment());
        vacationAction.setUserId(vacationUpdateDto.getUserId());
        vacationAction.setName(name);
        vacationAction = vacationActionService.create(vacationAction);
        return vacationAction;
    }

    private void addApprover(Vacation vacation) {
        // Add approver only if already not added from frontend
        if(Objects.isNull(vacation.getApprover())) {
            LoginDetails loginDetails = loginDetailsService.findByUsername(vacation.getCreatedForUsername());
            Employee employee = employeeService.findByEmployeeUniqueId(loginDetails.getEmployeeUniqueId());
            Employee approver = employee.getSupervisor();
            vacation.setApprover(approver.getId());
        }
    }
}
