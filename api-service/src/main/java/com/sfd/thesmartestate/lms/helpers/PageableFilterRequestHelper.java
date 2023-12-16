package com.sfd.thesmartestate.lms.helpers;

import com.sfd.thesmartestate.lms.dto.FilterRequestPayload;
import com.sfd.thesmartestate.lms.dto.PageableFilterDto;
import com.sfd.thesmartestate.lms.entities.Budget;
import com.sfd.thesmartestate.lms.entities.Lead;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.lms.rawleads.RawLead;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.services.UserService;
import com.sfd.thesmartestate.users.teams.entities.Team;
import com.sfd.thesmartestate.users.teams.services.TeamService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class PageableFilterRequestHelper {

    private final EntityManager entityManager;
    private final UserService userService;
    private final TeamService teamService;

    private final BudgetHelper budgetHelper;

    public PageableFilterDto buildPageableFilterRequest(FilterRequestPayload payload) {
        PageableFilterDto.PageableFilterDtoBuilder pageableFilterBuilder = PageableFilterDto.builder()
                .withIsOpen(payload.isOpen())
                .withOrderColumn(payload.getOrderColumn())
                .withOrderDir(payload.getOrderDir())
                .withPageNumber(payload.getPageNumber())
                .withPageSize(payload.getPageSize())
                .withStartDate(LocalDate.parse(payload.getStartDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay())
                .withEndDate(LocalDate.parse(payload.getEndDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atTime(23, 59, 59, 999));

        buildWithSearchText(payload.getSearchText(), pageableFilterBuilder);
        buildWithStatus(payload.getStatus(), pageableFilterBuilder);
        buildWithSource(payload.getSource(), pageableFilterBuilder);
        buildWithType(payload.getType(), pageableFilterBuilder);
        buildWithAssignedTo(payload.getAssignedTo(), pageableFilterBuilder);
        buildWithDeactivationReason(payload.getDeactivationReason(), pageableFilterBuilder);
        buildWithProjectName(payload.getProject(), pageableFilterBuilder);
        buildWithInventorySize(payload.getLeadInventorySize(), pageableFilterBuilder);
        buildWithBudgetRange(payload.getBudget(), pageableFilterBuilder);
        return pageableFilterBuilder.build();
    }


    public PageableFilterDto buildPageableFilterRequestForRawLead(FilterRequestPayload payload) {
        PageableFilterDto.PageableFilterDtoBuilder pageableFilterBuilder = PageableFilterDto.builder()
                .withOrderColumn(payload.getOrderColumn())
                .withOrderDir(payload.getOrderDir())
                .withPageNumber(payload.getPageNumber())
                .withPageSize(payload.getPageSize())
                .withStartDate(LocalDate.parse(payload.getStartDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay())
                .withEndDate(LocalDate.parse(payload.getEndDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atTime(23, 59, 59, 999));

        buildWithSearchText(payload.getSearchText(), pageableFilterBuilder);
        buildWithProjectName(payload.getProject(), pageableFilterBuilder);
        return pageableFilterBuilder.build();
    }

    private void buildWithBudgetRange(Budget budget, PageableFilterDto.PageableFilterDtoBuilder pageableFilterBuilder) {
        if (Objects.isNull(budget)) {
            budget = budgetHelper.createDefaultBudget();
        }
        if (Objects.isNull(budget.getStartAmount())) {
            budget.setStartAmount(0D);
        }
        if (Objects.isNull(budget.getEndAmount())) {
            budget.setEndAmount(Double.MAX_VALUE);
        }

        pageableFilterBuilder.withBudgetFrom(budgetHelper.convertToAbsoluteValue(budget.getStartUnit(), budget.getStartAmount()));
        pageableFilterBuilder.withBudgetTo(budgetHelper.convertToAbsoluteValue(budget.getEndUnit(), budget.getEndAmount()));
    }

    private void buildWithProjectName(String projectName, PageableFilterDto.PageableFilterDtoBuilder leadPageableFiltersBuilder) {
        if (Objects.nonNull(projectName) && StringUtils.hasText(projectName)) {

            leadPageableFiltersBuilder.withProject(projectName);

        }
    }

    private void buildWithInventorySize(String inventorySize, PageableFilterDto.PageableFilterDtoBuilder leadPageableFiltersBuilder) {
        if (Objects.nonNull(inventorySize) && StringUtils.hasText(inventorySize)) {

            leadPageableFiltersBuilder.withLeadInventorySize(inventorySize);

        }
    }


    private void buildWithDeactivationReason(String deactivationReason, PageableFilterDto.PageableFilterDtoBuilder leadPageableFiltersBuilder) {
        if (Objects.nonNull(deactivationReason) && StringUtils.hasText(deactivationReason)) {
            leadPageableFiltersBuilder.withDeactivationReason(deactivationReason);
        }
    }

    private void buildWithAssignedTo(String assignedTo, PageableFilterDto.PageableFilterDtoBuilder leadPageableFiltersBuilder) {
        if (Objects.nonNull(assignedTo) && StringUtils.hasText(assignedTo)) {
            Employee employee = userService.findLoggedInUser();
            if (employee.isSuperAdmin() || employee.isAdmin()) {
                leadPageableFiltersBuilder.withAssignedTo(assignedTo);
            } else {
                List<Team> team = teamService.findTeamsByMemberIdAndIsActive(employee.getId(), true);
                // check if logged user is supervisor
                if (!team.isEmpty()) {
                    long supervisorId;
                    supervisorId = team.stream().filter(Team::getIsActive).findFirst().get().getSupervisor().getId();
                    //used in case of filter
                    if (supervisorId == employee.getId()) {
                        leadPageableFiltersBuilder.withAssignedTo(assignedTo);
                    } else {
                        //in team but not supervisor
                        leadPageableFiltersBuilder.withAssignedTo(employee.getId().toString());
                    }
                } else {
                    //not supervisor and not in team so fetch logged in user leads only
                    leadPageableFiltersBuilder.withAssignedTo(employee.getId().toString());
                }
            }
        }
    }

    private void buildWithType(String type, PageableFilterDto.PageableFilterDtoBuilder leadPageableFiltersBuilder) {
        if (Objects.nonNull(type) && StringUtils.hasText(type)) {
            leadPageableFiltersBuilder.withType(type);
        }
    }

    private void buildWithStatus(String status, PageableFilterDto.PageableFilterDtoBuilder leadPageableFiltersBuilder) {
        if (Objects.nonNull(status) && StringUtils.hasText(status)) {
            leadPageableFiltersBuilder.withStatus(status);
        }
    }

    private void buildWithSource(String source, PageableFilterDto.PageableFilterDtoBuilder leadPageableFiltersBuilder) {
        if (Objects.nonNull(source) && StringUtils.hasText(source)) {
            leadPageableFiltersBuilder.withSource(source);
        }
    }

    public long getCountOfAvailableItems(PageableFilterDto pageableFilterDto) {
        Query queryTotal = entityManager.createQuery
                (createSelectQuery("select count(*) from Lead l", pageableFilterDto));
        queryTotal.setParameter("startDate", pageableFilterDto.getStartDate());
        queryTotal.setParameter("endDate", pageableFilterDto.getEndDate());
        return (long) queryTotal.getSingleResult();
    }
    public long getCountOfAvailableRawLeadItems(PageableFilterDto pageableFilterDto) {
        Query queryTotal = entityManager.createQuery
                (createSelectQueryRawLead("select count(*) from RawLead l", pageableFilterDto));
        queryTotal.setParameter("startDate", pageableFilterDto.getStartDate());
        queryTotal.setParameter("endDate", pageableFilterDto.getEndDate());
        return (long) queryTotal.getSingleResult();
    }

    private void buildWithSearchText(String searchText, PageableFilterDto.PageableFilterDtoBuilder leadPageableFiltersBuilder) {
        if (Objects.nonNull(searchText) && StringUtils.hasText(searchText)) {
            leadPageableFiltersBuilder.withSearchText(searchText);
        }
    }

    public TypedQuery<Lead> createExecutableQuery(PageableFilterDto pageableFilterDto) {
        TypedQuery<Lead> query =
                entityManager.createQuery(createSelectQuery("select l from Lead l", pageableFilterDto), Lead.class);
        int pageNumber = pageableFilterDto.getPageNumber();
        int pageSize = pageableFilterDto.getPageSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        query.setParameter("startDate", pageableFilterDto.getStartDate());
        query.setParameter("endDate", pageableFilterDto.getEndDate());
        return query;
    }

    public TypedQuery<RawLead> createExecutableQueryRawLead(PageableFilterDto pageableFilterDto) {
        TypedQuery<RawLead> query =
                entityManager.createQuery(createSelectQueryRawLead("select l from RawLead l", pageableFilterDto), RawLead.class);
        int pageNumber = pageableFilterDto.getPageNumber();
        int pageSize = pageableFilterDto.getPageSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        query.setParameter("startDate", pageableFilterDto.getStartDate());
        query.setParameter("endDate", pageableFilterDto.getEndDate());
        return query;
    }

    private String createSelectQuery(String query, PageableFilterDto pageableFilterDto) {
        StringBuilder queryBuilder = new StringBuilder(query);
        boolean searchTextApplied = applyTextSearchFilter(pageableFilterDto, queryBuilder);
        boolean typeFilterApplied = applyFilter(pageableFilterDto.getType(), searchTextApplied, queryBuilder, " l.type.id=");

        boolean assignedToFilterApplied = isAssignedToFilterApplied(pageableFilterDto, queryBuilder, searchTextApplied, typeFilterApplied);


        boolean statusFilterApplied = applyStatusNameFilter(queryBuilder,
                searchTextApplied || typeFilterApplied || assignedToFilterApplied,
                pageableFilterDto);

        boolean sourceFilterApplied = applySourceNameFilter(queryBuilder, searchTextApplied
                || typeFilterApplied
                || assignedToFilterApplied
                || statusFilterApplied, pageableFilterDto);

        boolean deactiveFilterApplied = applyDeactiveLeadsFilter(queryBuilder, searchTextApplied
                || sourceFilterApplied
                || typeFilterApplied
                || assignedToFilterApplied
                || statusFilterApplied);
        boolean deactivationReasonFilterApplied = applyDeactivationReasonFilter(queryBuilder,
                searchTextApplied
                        || typeFilterApplied
                        || sourceFilterApplied
                        || assignedToFilterApplied
                        || statusFilterApplied
                        || deactiveFilterApplied,
                pageableFilterDto, "l.deactivationReason.id=");

        boolean projectNameFilterApplied = applyProjectNameFilter(queryBuilder,
                searchTextApplied
                        || typeFilterApplied
                        || sourceFilterApplied
                        || assignedToFilterApplied
                        || statusFilterApplied
                        || deactiveFilterApplied
                        || deactivationReasonFilterApplied,
                pageableFilterDto);

        boolean inventorySizeFilterApplied = applyInventorySizeReasonFilter(queryBuilder,
                searchTextApplied
                        || typeFilterApplied
                        || sourceFilterApplied
                        || assignedToFilterApplied
                        || statusFilterApplied
                        || deactiveFilterApplied
                        || deactivationReasonFilterApplied
                        || projectNameFilterApplied,

                pageableFilterDto, " l.leadInventorySize.id=");


        applyDateRangeFilter(queryBuilder,
                searchTextApplied
                        || typeFilterApplied
                        || sourceFilterApplied
                        || assignedToFilterApplied
                        || statusFilterApplied
                        || deactiveFilterApplied
                        || deactivationReasonFilterApplied
                        || projectNameFilterApplied
                        || inventorySizeFilterApplied, pageableFilterDto.getOrderColumn());
        // Do not change the below this line
        applyBudgetRangeFilter(pageableFilterDto, queryBuilder);
        applyOrderByClause(pageableFilterDto, queryBuilder);
        return queryBuilder.toString();
    }

    private String createSelectQueryRawLead(String query, PageableFilterDto pageableFilterDto) {
        StringBuilder queryBuilder = new StringBuilder(query);
        boolean searchTextApplied = applyTextSearchFilterRawLead(pageableFilterDto, queryBuilder);

        applyDateRangeFilter(queryBuilder,
                searchTextApplied
                , pageableFilterDto.getOrderColumn());

        applyOrderByClause(pageableFilterDto, queryBuilder);
        return queryBuilder.toString();
    }


    private boolean isAssignedToFilterApplied(PageableFilterDto pageableFilterDto, StringBuilder queryBuilder, boolean searchTextApplied, boolean typeFilterApplied) {
        Employee employee = userService.findLoggedInUser();
        if (employee.isSuperAdmin() || employee.isAdmin()) {
            return applyFilter(pageableFilterDto.getAssignedTo(),
                    typeFilterApplied
                            || searchTextApplied, queryBuilder, " l.assignedTo.id=");
        } else {
            return applyAssignedToFilterForNonAdmin(queryBuilder,
                    searchTextApplied || typeFilterApplied,
                    pageableFilterDto, employee);
        }
    }

    private boolean applyAssignedToFilterForNonAdmin(StringBuilder queryBuilder, boolean filterApplied, PageableFilterDto pageableFilterDto, Employee employee) {

        if (Objects.nonNull(pageableFilterDto.getAssignedTo()) && StringUtils.hasText(pageableFilterDto.getAssignedTo())) {

            if (filterApplied) {
                queryBuilder.append(" AND (");
            } else {
                queryBuilder.append(" WHERE (");
            }
            return fetchAssignedToForNonAdminUsers(queryBuilder, pageableFilterDto, employee);
        }
        return false;

    }

    /**
     * If user is non admin or user is Team leader then fetch all subordinates lead
     *
     * @param queryBuilder
     * @param pageableFilterDto
     * @param employee
     * @return
     */
    private boolean fetchAssignedToForNonAdminUsers(StringBuilder queryBuilder, PageableFilterDto pageableFilterDto, Employee employee) {
        List<Team> teams = teamService.findTeamsByMemberIdAndIsActive(employee.getId(), true);
        List<Long> teamMembers = new ArrayList<>();

        if (!teams.isEmpty()) {
            Team team = teams.stream().filter(Team::getIsActive).findFirst().get();
            teamMembers = team.getMembers().stream().map(Employee::getId).collect(Collectors.toList());
        }
        if (!teamMembers.isEmpty()) {
            long supervisorId = teams.get(0).getSupervisor().getId();
            //Supervisor and no filter applied so fetch all subordinated details
            if (supervisorId == employee.getId()
                    && pageableFilterDto.getAssignedTo().equalsIgnoreCase("NO")) {

                createAssignedToQuery(queryBuilder, teamMembers);

            } else {
                Optional<Long> isSubordinate = teamMembers.stream().filter(userId -> pageableFilterDto.getAssignedTo().equals(userId.toString())).findAny();
                // Leader applied filter for user which is not his subordinate so check for it ,in case of false
                // show all his users list by default
                if (isSubordinate.isPresent()) {
                    queryBuilder.append("l.assignedTo.id in(" + pageableFilterDto.getAssignedTo());
                } else {
                    // default show all his ordinates list
                    createAssignedToQuery(queryBuilder, teamMembers);
                }
            }
            queryBuilder.append("))");
            return true;

        } else {
            queryBuilder.append("l.assignedTo.id in(" + pageableFilterDto.getAssignedTo() + ")");
            queryBuilder.append(")");
            return true;
        }
    }

    private static void createAssignedToQuery(StringBuilder queryBuilder, List<Long> teamMembers) {
        queryBuilder.append("l.assignedTo.id in(" + teamMembers.get(0));
        for (int i = 0; i < teamMembers.size(); i++) {
            queryBuilder.append("," + teamMembers.get(i));
        }
    }

    private boolean applyProjectNameFilter(StringBuilder queryBuilder, boolean filterApplied, PageableFilterDto pageableFilterDto) {
        if (Objects.nonNull(pageableFilterDto.getProject())) {

            if (filterApplied) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            queryBuilder
                    .append("l.project.name like '%")
                    .append(pageableFilterDto.getProject())
                    .append("%'");
            return true;
        }
        return false;
    }

    private boolean applyStatusNameFilter(StringBuilder queryBuilder, boolean filterApplied, PageableFilterDto pageableFilterDto) {
        if (Objects.nonNull(pageableFilterDto.getStatus())) {

            if (filterApplied) {
                queryBuilder.append(" AND (");
            } else {
                queryBuilder.append(" WHERE (");
            }
            if (pageableFilterDto.getStatus() != null) {
                String[] statusListed = pageableFilterDto.getStatus().split(",");
                for (int i = 0; i < statusListed.length; i++) {
                    queryBuilder.append("l.status.name ='").append(statusListed[i]).append("'");
                    if (i < statusListed.length - 1) {
                        queryBuilder.append(" OR ");
                    }
                }
            }
            queryBuilder.append(" )");
            return true;
        }
        return false;
    }

    private boolean applySourceNameFilter(StringBuilder queryBuilder, boolean filterApplied, PageableFilterDto pageableFilterDto) {
        if (Objects.nonNull(pageableFilterDto.getSource())) {

            if (filterApplied) {
                queryBuilder.append(" AND (");
            } else {
                queryBuilder.append(" WHERE (");
            }
            queryBuilder.append("l.source.name='").append(pageableFilterDto.getSource())
                    .append("')");
            return true;
        }
        return false;
    }

    private boolean applyInventorySizeReasonFilter(StringBuilder queryBuilder,
                                                   boolean filterApplied,
                                                   PageableFilterDto pageableFilterDto,
                                                   String filterCondition) {
        if (Objects.nonNull(pageableFilterDto.getLeadInventorySize())) {
            if (filterApplied) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            queryBuilder.append(filterCondition)
                    .append(Long.valueOf(pageableFilterDto.getLeadInventorySize()));
            return true;
        }
        return false;
    }

    private boolean applyDeactivationReasonFilter(StringBuilder queryBuilder,
                                                  boolean filterApplied,
                                                  PageableFilterDto pageableFilterDto,
                                                  String filterCondition) {
        if (Objects.nonNull(pageableFilterDto.getDeactivationReason())) {
            if (filterApplied) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            queryBuilder.append(filterCondition).append(Long.valueOf(pageableFilterDto.getDeactivationReason()));
            return true;
        }
        return false;
    }

    private void applyDateRangeFilter(StringBuilder queryBuilder, boolean filterApplied, String orderColumn) {
        if (filterApplied) {
            queryBuilder.append(" AND ");
        } else {
            queryBuilder.append(" WHERE ");
        }
        queryBuilder.append("(").append(orderColumn.equals("createdAt") ?
                "(l.createdAt >= :startDate AND l.createdAt < :endDate)" :
                "(l.lastUpdateAt >= :startDate AND l.lastUpdateAt < :endDate )").append(")");
    }

    private void applyBudgetRangeFilter(PageableFilterDto pageableFilters, StringBuilder queryBuilder) {
        queryBuilder.append(" AND ")
                .append("(l.budget.absoluteStartAmount >= ")
                .append(pageableFilters.getBudgetFrom())
                .append(" AND l.budget.absoluteEndAmount <= ")
                .append(pageableFilters.getBudgetTo())
                .append(")");
    }

    boolean applyDeactiveLeadsFilter(StringBuilder queryBuilder, boolean filterApplied) {
        Employee loggedInEmployee = userService.findLoggedInUser();
        if (!loggedInEmployee.isSuperAdmin() && !loggedInEmployee.isAdmin()) {
            if (filterApplied) {
                queryBuilder.append(" AND l.status.name !='DEACTIVE'");
            } else {
                queryBuilder.append(" WHERE l.status.name !='DEACTIVE'");
            }
            return true;
        }
        return false;
    }

    private void applyOrderByClause(PageableFilterDto pageableFilterDto, StringBuilder queryBuilder) {
        String sortColumn = "createdAt";
        if (Objects.nonNull(pageableFilterDto.getOrderColumn())) {
            if ("customerName".equalsIgnoreCase(pageableFilterDto.getOrderColumn())) {
                sortColumn = "customer.name";
            } else if ("customerPhone".equalsIgnoreCase(pageableFilterDto.getOrderColumn())) {
                sortColumn = "customer.phone";
            } else if ("lastUpdateAt".equalsIgnoreCase(pageableFilterDto.getOrderColumn())) {
                sortColumn = "lastUpdateAt";
            }
        }
        queryBuilder.append(" ORDER BY")
                .append(" l.")
                .append(sortColumn)
                .append(" ")
                .append(Objects.nonNull(pageableFilterDto.getOrderDir())
                        ? pageableFilterDto.getOrderDir()
                        : "DESC");
    }

    private boolean applyFilter(String filterValue,
                                boolean isFiltersApplied,
                                StringBuilder queryBuilder,
                                String filterCondition) {
        boolean newFilterApplied = false;

        if (Objects.nonNull(filterValue)
                && StringUtils.hasText(filterValue)
                && !"NO".equalsIgnoreCase(filterValue)) {
            if (isFiltersApplied) {
                queryBuilder.append(" AND");
            } else {
                queryBuilder.append(" WHERE");
            }
            queryBuilder.append(filterCondition).append(Long.valueOf(filterValue));
            newFilterApplied = true;
        }
        return newFilterApplied;
    }

    private boolean applyTextSearchFilter(PageableFilterDto pageableFilterDto, StringBuilder queryBuilder) {
        boolean searchTextApplied = false;
        if (Objects.nonNull(pageableFilterDto.getSearchText()) && StringUtils.hasText(pageableFilterDto.getSearchText())) {
            queryBuilder
                    .append(" JOIN Customer c on l.customer.id = c.id")
                    //.append(" JOIN Watcher w on l.assignedTo.id = w.id")
                    .append(" WHERE (c.name like '%")
                    .append(pageableFilterDto.getSearchText())
                    .append("%'").append(" OR c.email like '%")
                    .append(pageableFilterDto.getSearchText())
                    .append("%'")
                    .append(" OR c.phone like '%")
                    .append(pageableFilterDto.getSearchText())
                    .append("%')");
            searchTextApplied = true;
        }
        return searchTextApplied;
    }

    private boolean applyTextSearchFilterRawLead(PageableFilterDto pageableFilterDto, StringBuilder queryBuilder) {
        boolean searchTextApplied = false;
        if (Objects.nonNull(pageableFilterDto.getSearchText()) && StringUtils.hasText(pageableFilterDto.getSearchText())) {
            queryBuilder
                    .append(" WHERE (l.customerName like '%")
                    .append(pageableFilterDto.getSearchText())
                    .append("%'").append(" OR l.customerPhone like '%")
                    .append(pageableFilterDto.getSearchText())
                    .append("%'")
                    .append(" OR l.customerEmail like '%")
                    .append(pageableFilterDto.getSearchText())
                    .append("%'")
                    .append(" OR l.projectName like '%")
                    .append(pageableFilterDto.getSearchText())
                    .append("%'")
                    .append(" OR l.isLeadConverted like '%")
                    .append(pageableFilterDto.getSearchText())
                    .append("%')");
            searchTextApplied = true;
        }
        return searchTextApplied;
    }




    public void validateRequestPayload(FilterRequestPayload filterRequestPayload) {
        if (Objects.isNull(filterRequestPayload.getOrderDir())) {
            throw new LeadException("orderDir value is missing");
        }
        if (Objects.isNull(filterRequestPayload.getOrderColumn())) {
            throw new LeadException("orderColumn value is missing");
        }
    }
}
