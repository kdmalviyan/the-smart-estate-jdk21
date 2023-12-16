package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.customer.entities.Customer;
import com.sfd.thesmartestate.customer.services.CustomerService;
import com.sfd.thesmartestate.lms.entities.Budget;
import com.sfd.thesmartestate.lms.entities.Comment;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.lms.helpers.BudgetHelper;
import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.projects.services.ProjectService;
import com.sfd.thesmartestate.security.exceptions.UserNotFoundException;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.services.UserService;
import com.sfd.thesmartestate.users.teams.entities.Team;
import com.sfd.thesmartestate.users.teams.exceptions.TeamException;
import com.sfd.thesmartestate.users.teams.services.TeamService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressFBWarnings("EI_EXPOSE_REP")

public class LeadServiceHelper {
    private final LeadStatusService leadStatusService;
    private final LeadTypeService leadTypeService;
    private final LeadSourceService leadSourceService;
    private final BudgetHelper budgetHelper;
    private final CustomerService customerService;
    private final LeadInventorySizeService leadInventorySizeService;
    private final UserService userService;
    private final ProjectService projectService;
    private final TeamService teamService;

    private final static Random RANDOM = new Random();

    public void addMetadataDetails(Lead lead) {
        lead.setStatus(leadStatusService.findById(lead.getStatus().getId()));
        lead.setSource(leadSourceService.findById(lead.getSource().getId()));
        lead.setType(leadTypeService.findById(lead.getType().getId()));
        lead.setLeadInventorySize(leadInventorySizeService.findById(lead.getLeadInventorySize().getId()));
    }

    public void addCustomerDetails(Lead lead) {
        Customer customer = customerService.findByPhone(lead.getCustomer().getPhone());
        if (Objects.nonNull(customer)) {
            lead.setCustomer(customer);
        }
    }

    public void addBudgetDetails(Lead lead) {
        Budget budget = lead.getBudget();
        Double absoluteStartAmount = budgetHelper.convertToAbsoluteValue(budget.getStartUnit(), budget.getStartAmount());
        Double absoluteEndAmount = budgetHelper.convertToAbsoluteValue(budget.getEndUnit(), budget.getEndAmount());
        budget.setAbsoluteStartAmount(absoluteStartAmount);
        budget.setAbsoluteEndAmount(absoluteEndAmount);
    }

    public void addBasicDetails(Lead lead) {
        lead.setCreatedAt(LocalDateTime.now());
        lead.setCreatedBy(userService.findLoggedInUser());
    }

    public void addInitialComment(Lead lead) {
        //Update comment fields
        Comment comment = lead.getComments()
                .stream()
                .findFirst()
                .orElseThrow(() -> new LeadException("Lead has an empty comment, you must provide a comment at the time of lead creation"));
        comment.setCreatedBy(userService.findLoggedInUser());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setCommentType("Inquiry");
    }

    public void addProjectDetails(Lead lead) {
        Project project = projectService.findById(lead.getProject().getId());
        lead.setProject(project);
    }

    public void addLeadAssignment(Lead lead) {
        Project project = lead.getProject();
        // username -> List<user> (same user repeated into the team, it represents number of teams it acts as supervisor
        Map<String, List<Employee>> teamLeaders = findAllTeamLeadersInAProject(project);
        List<Employee> employees = teamLeaders
                .entrySet()
                .stream().min(sortWithValues())
                .orElseThrow(() -> new TeamException("No Team found for assignment"))
                .getValue();
        Employee employee = employees.get(randomIndex(employees.size()));
        if (Objects.isNull(employee))
            throw new UserNotFoundException("No User found for lead assignment");
        lead.setAssignedTo(employee);
    }

    public void addAssignedToDetails(Lead lead) {
        lead.setAssignedTo(userService.findById(lead.getAssignedTo().getId()));
    }

    /**
     * 1 project multiple teams
     */
    private Map<String, List<Employee>> findAllTeamLeadersInAProject(Project project) {
        return teamService.findByProjectId(project.getId())
                .stream()
                .map(Team::getSupervisor)
                .collect(Collectors.groupingBy(Employee::getUsername));
    }


    private int randomIndex(int size) {
        if (size == 0) {
            throw new UserNotFoundException("No User found for lead assignment");
        }
        return RANDOM.nextInt(size);
    }

    private Comparator<Map.Entry<String, List<Employee>>> sortWithValues() {
        return (a, b) -> {
            if (a.getValue().size() > b.getValue().size()) {
                return -1;
            }
            if (a.getValue().size() < b.getValue().size()) {
                return -1;
            }
            return 0;
        };
    }
}
