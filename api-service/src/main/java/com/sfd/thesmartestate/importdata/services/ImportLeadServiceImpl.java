package com.sfd.thesmartestate.importdata.services;

import com.sfd.thesmartestate.customer.services.CustomerService;
import com.sfd.thesmartestate.importdata.dto.ErrorDto;
import com.sfd.thesmartestate.importdata.dto.ImportResponseDto;
import com.sfd.thesmartestate.importdata.dto.XlsLeadRowDto;
import com.sfd.thesmartestate.lms.entities.*;
import com.sfd.thesmartestate.lms.services.LeadInventorySizeService;
import com.sfd.thesmartestate.lms.services.LeadService;
import com.sfd.thesmartestate.lms.services.LeadSourceService;
import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.projects.services.ProjectService;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.services.EmployeeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
@Primary
public class ImportLeadServiceImpl implements ImportLeadService {

    public static final String FILE_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String ERROR = "ERROR";

    private final LeadService leadService;

    private final LeadSourceService leadSourceService;
    private final ProjectService projectService;

    private final EmployeeService employeeService;
    private final CustomerService customerService;
    private final LeadInventorySizeService leadInventorySizeService;
    private final ImportUtilsService importUtilsService;


    @Override
    public ImportResponseDto uploadLeads(MultipartFile file, ImportResponseDto responseDto) {

        log.info("Start saving Leads" + LocalDateTime.now());
        if (file == null) {
            responseDto.setErrors(Collections.singletonList(new ErrorDto("File must not be null", "FILE_EMPTY", -1, -1L)));
            return responseDto;
        }
        if (Boolean.TRUE.equals(hasExcelFormat(file))) {
            //upload file and save data
            LeadImportData leadImportData = uploadFileAndSaveLeads(file);
            // prepare response
            prepareResponse(responseDto, leadImportData);

            return responseDto;
        } else if (Boolean.FALSE.equals(hasExcelFormat(file))) {
            responseDto.setMessage("Please upload a file with excel format");
            return responseDto;
        } else {
            responseDto.setMessage("Vendor Id now mentioned");
            return responseDto;
        }
    }


    private LeadImportData uploadFileAndSaveLeads(MultipartFile file) {
        XlsLeadFileReader fileReader = new XlsLeadFileReader();
        //Excel data in bean
        List<XlsLeadRowDto> rows = fileReader.getLeadRows(file);
        //Save all new customer
        importUtilsService.checkExistingAndSaveCustomer(rows);
        //save lead source

        LeadImportData leadImportData = validateAndGenerateLeadData(rows);
        Set<Lead> leadsToSave = leadImportData.getLeads();
        if (!leadsToSave.isEmpty()) {
            log.info("Started saving" + LocalDateTime.now());
            saveAllExcelUploadedLeads(leadsToSave);
            log.info("End saving" + LocalDateTime.now());
        }

        return leadImportData;
    }

    private LeadImportData validateAndGenerateLeadData(List<XlsLeadRowDto> rows) {
        LeadImportData leadImportData = new LeadImportData();
        Employee loggedInEmployee = employeeService.findLoggedInEmployee();

        int rowNumber = 1;
        int rowsSkipped = 0;
        List<ErrorDto> errors = new ArrayList<>();
        List<ErrorDto> warnings = new ArrayList<>();
        Set<Lead> leadsToSave = new HashSet<>();
        for (XlsLeadRowDto xlsRow : rows) {
            //Check Lead duplicate
            boolean isValid = importUtilsService.isValidLead(xlsRow, rowNumber, errors);
            xlsRow.setRownum(rowNumber);

            if (isValid) {
                List<Employee> defaultAssignedToEmployee = employeeService.getUserByNameStartsWithAndProjectName(xlsRow.getAssignTo(), xlsRow.getProjectName());
                if (defaultAssignedToEmployee.isEmpty()) {
                    errors.add(new ErrorDto("Assigned user " + xlsRow.getAssignTo() + " not found for the project :" + xlsRow.getProjectName(), "USER_NOT_FOUND", rowNumber, -1L));
                    rowsSkipped++;
                    rowNumber++;
                    continue;
                }

                if (checkLeadDuplicate(xlsRow.getCustomerPhone(), xlsRow.getProjectName(), rowNumber, warnings)) {
                    rowsSkipped++;
                    rowNumber++;
                    log.info("checkLeadDuplicate Skipping lead because of duplicate");
                    continue;
                }
                Lead lead = createLeadData(loggedInEmployee, defaultAssignedToEmployee.get(0), xlsRow, errors);
                if (lead == null) {
                    log.info("Skipping lead because of error");
                    continue;
                }
                leadsToSave.add(lead);
            }
            rowNumber++;
        }

        leadImportData.setRowsSkipped(rowsSkipped);
        leadImportData.setRowNumber(rowNumber);
        leadImportData.setLeads(leadsToSave);
        leadImportData.setWarnings(warnings);
        leadImportData.setErrors(errors);
        return leadImportData;
    }

    private Lead createLeadData(Employee loggedInEmployee, Employee defaultAssignedToEmployee, XlsLeadRowDto xlsRow, List<ErrorDto> errors) {
        Lead lead = new Lead();
        LeadSource leadSource = leadSourceService.findByName(xlsRow.getLeadSource().trim());
        if (Objects.isNull(leadSource)) {
            errors.add(new ErrorDto("Lead source not found please add this source if its new", ERROR, xlsRow.getRownum(), -1));
            return null;
        }
        Project project = projectService.findByName(xlsRow.getProjectName().trim());
        if (Objects.isNull(project)) {
            errors.add(new ErrorDto("Project not found in the system add new project if its new", ERROR, xlsRow.getRownum(), -1));
            return null;
        }
        Budget budget = importUtilsService.populateBudget(xlsRow, errors);
        if (Objects.isNull(budget)) {
            errors.add(new ErrorDto("Budget Format not correct please check ", ERROR, xlsRow.getRownum(), -1));
            return null;
        }
        LeadInventorySize leadInventorySize = populateInventorySize(xlsRow.getSize());
        if (Objects.isNull(leadInventorySize)) {
            errors.add(new ErrorDto("Lead inventory Size not found please check", ERROR, xlsRow.getRownum(), -1));
            return null;
        }
        lead.setSource(leadSource);
        lead.setProject(project);
        lead.setAssignedTo(defaultAssignedToEmployee);
        lead.setCustomer(customerService.findByPhone(xlsRow.getCustomerPhone()));
        lead.setType(importUtilsService.getLeadType(xlsRow.getLeadType()));
        lead.setStatus(importUtilsService.getStatus(xlsRow.getLeadStatus()));
        lead.setBudget(budget);
        lead.setLeadInventorySize(leadInventorySize);
        lead.setComments(getComment(xlsRow.getComment(), loggedInEmployee));
        lead.setCreatedBy(loggedInEmployee);
        lead.setCreatedAt(LocalDateTime.now());
        log.info("Lead Ready to save phone -{},project -{},", xlsRow.getCustomerPhone(), xlsRow.getProjectName());
        return lead;
    }

    //Checks file format
    public Boolean hasExcelFormat(MultipartFile file) {
        return FILE_TYPE.equals(file.getContentType());
    }




    private Set<Comment> getComment(String comment, Employee loggedInEmployee) {
        Comment addComment = new Comment();
        Set<Comment> commentSet = new HashSet<>();
        if (StringUtils.hasText(comment)) {
            addComment.setMessage(comment);
        } else {
            addComment.setMessage("Lead uploaded from Excel file");
        }
        addComment.setCommentType("Inquiry");
        addComment.setCreatedAt(LocalDateTime.now());
        addComment.setCreatedBy(loggedInEmployee);
        commentSet.add(addComment);
        return commentSet;
    }

    private void saveAllExcelUploadedLeads(Collection<Lead> leads) {
        leadService.saveAll(leads);
    }

    private boolean checkLeadDuplicate(String phone, String projectName, Number rowNumber, List<ErrorDto> warning) {
        log.info("checkLeadDuplicate with Customer Phone:  {}  and Project: {}", phone, projectName);
        // This is added as we have duplicate leads in system because of transfer leads
        Project project = projectService.findByName(projectName.trim());
        List<Lead> existingLead = leadService.findByCustomerPhoneAndProjectId(phone.trim(), project.getId());
        if (Objects.nonNull(existingLead) && !existingLead.isEmpty()) {
            warning.add(new ErrorDto("Lead with Customer Phone: " + existingLead.get(0).getCustomer().getPhone() + " and Project: " + existingLead.get(0).getProject().getName() + " already exists.", "DUPLICATE_LEAD", rowNumber.intValue(), existingLead.get(0).getId()));
            log.info("DUPLICATE:: row number :" + rowNumber + ", Lead with Customer Phone: " + existingLead.get(0).getCustomer().getPhone() + " and Project: " + existingLead.get(0).getProject().getName() + " already exists.");
            return true;
        } else {
            log.error("checkLeadDuplicate Lead with Customer Phone:  {}  and Project: {} not PRESENT", phone, projectName);
        }
        return false;
    }

    private void prepareResponse(ImportResponseDto responseDto, LeadImportData leadImportData) {
        responseDto.setMessage("File Uploaded successfully");
        log.info("File Uploaded successfully. Total " + leadImportData.getLeads().size() + " records added.");
        responseDto.setRowsSkipped(leadImportData.getRowsSkipped());
        responseDto.setWarnings(leadImportData.getWarnings());
        responseDto.setErrors(leadImportData.getErrors());
        responseDto.setRowsSuccess(leadImportData.getLeads().size());
    }

    public LeadInventorySize populateInventorySize(String size) {
        return leadInventorySizeService.findBySize(size);
    }
}

@Data
class LeadImportData {
    int rowNumber;
    int rowsSkipped;
    Set<Lead> leads = new HashSet<>();
    List<ErrorDto> errors = new ArrayList<>();
    List<ErrorDto> warnings = new ArrayList<>();
}

