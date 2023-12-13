package com.sfd.thesmartestate.importdata.services;

import com.sfd.thesmartestate.customer.entities.Customer;
import com.sfd.thesmartestate.customer.services.CustomerService;
import com.sfd.thesmartestate.importdata.dto.ErrorDto;
import com.sfd.thesmartestate.importdata.dto.XlsLeadRowDto;
import com.sfd.thesmartestate.lms.entities.Budget;
import com.sfd.thesmartestate.lms.entities.LeadStatus;
import com.sfd.thesmartestate.lms.entities.LeadType;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.lms.helpers.BudgetHelper;
import com.sfd.thesmartestate.lms.services.LeadStatusService;
import com.sfd.thesmartestate.lms.services.LeadTypeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")
public class ImportUtilsService {
    public static final String ERROR = "ERROR";
    private final LeadStatusService leadStatusService;
    private final LeadTypeService leadTypeService;
    private final CustomerService customerService;

    private final BudgetHelper budgetHelper;


    /**
     * //   40 to 55 lac
     * //   85 to 1 cr
     * //   1 to 1.5 cr
     * change
     *
     * @return budget
     */
    public Budget populateBudget(XlsLeadRowDto xlsRow, List<ErrorDto> errors) {
        String unit;
        double startAmount = 0;
        double endAmount = 0;
        Budget budget = new Budget();
        String budgetText = xlsRow.getBudget();
        try {
            if (budgetText.toLowerCase().contains("above")) {
                String[] budgetdata = budgetText.toLowerCase().stripLeading().stripTrailing().split("above");
                String endDetails = budgetdata[1].stripLeading().stripTrailing();
                unit = endDetails.split(" ")[1];
                startAmount = Double.parseDouble(endDetails.split(" ")[0].stripLeading().stripTrailing());
                endAmount = 100.00;

            } else {
                String[] budgetdata = budgetText.stripLeading().stripTrailing().split("to");
                String endDetails = budgetdata[1].stripLeading().stripTrailing();
                unit = endDetails.split(" ")[1];
                startAmount = Double.parseDouble(budgetdata[0].stripLeading().stripTrailing());
                endAmount = Double.parseDouble(endDetails.split(" ")[0]);
            }

            if (startAmount < endAmount) {
                if ("cr".equalsIgnoreCase(unit)) {
                    unit = "CRORE";
                }
                budget.setStartUnit(unit.toUpperCase());
                budget.setEndUnit(unit.toUpperCase());
            } else {
                budget.setStartUnit("LAC");
                budget.setEndUnit("CRORE");
            }
            budget.setStartAmount(startAmount);
            budget.setEndAmount(endAmount);
        } catch (Exception e) {
            budget.setStartUnit("LAC");
            budget.setEndUnit("LAC");
            budget.setStartAmount(startAmount);
            budget.setEndAmount(endAmount);
            errors.add(new ErrorDto("Error in budget text " + budgetText, ERROR, xlsRow.getRownum(), -1));
            return null;
        }
        Double absoluteStartValue = budgetHelper.convertToAbsoluteValue(budget.getEndUnit(), budget.getEndAmount());
        Double absoluteEndValue = budgetHelper.convertToAbsoluteValue(budget.getEndUnit(), budget.getEndAmount());
        budget.setAbsoluteStartAmount(absoluteStartValue);
        budget.setAbsoluteEndAmount(absoluteEndValue);
        return budget;
    }


    public LeadType getLeadType(String type) {
        LeadType leadType = leadTypeService.findByType(type);
        if (Objects.isNull(leadType)) {
            leadType = leadTypeService.findByType("COLD");
        }
        return leadType;
    }

    public LeadStatus getStatus(String status) {
        LeadStatus leadStatus = leadStatusService.findByName(status.toUpperCase());
        if (Objects.isNull(leadStatus)) {
            leadStatus = leadStatusService.findByName("ACTIVE");
        }
        return leadStatus;
    }

    public boolean isValidLead(XlsLeadRowDto lead, Number rowNumber, List<ErrorDto> errors) {

        boolean customerNameEmpty, customerPhoneEmpty, leadSourceEmpty, projectEmpty, budgetEmpty, assignTo, customerPhoneValid;

        customerNameEmpty = isEmpty(rowNumber, errors, lead.getCustomerName(), " Customer name empty");
        customerPhoneValid = checkValidPhone(rowNumber, errors, lead.getCustomerPhone());
        leadSourceEmpty = isEmpty(rowNumber, errors, lead.getLeadSource(), " Lead source is empty");
        projectEmpty = isEmpty(rowNumber, errors, lead.getProjectName(), " Project is empty");
        budgetEmpty = isEmpty(rowNumber, errors, lead.getBudget(), " Budget is empty");
        assignTo = isEmpty(rowNumber, errors, lead.getAssignTo(), " assignTo is empty");

        return customerNameEmpty && leadSourceEmpty && projectEmpty && budgetEmpty && assignTo && customerPhoneValid;
    }


    public boolean checkValidPhone(Number rowNumber, List<ErrorDto> errors, String phoneNumber) {
        String message = " Customer phone not a number";
        try {
            if (phoneNumber != null) {
                Long.valueOf(phoneNumber);
                return true;
            }
            return false;
        } catch (Exception e) {
            errors.add(new ErrorDto(message + "-" + phoneNumber, "IGNORED", rowNumber.intValue(), -1L));
            log.error("NOT ADDED:: Not a number on row number : " + rowNumber + message);
            return false;
        }
    }

    public boolean checkValidPhone(String phoneNumber) {
        try {
            if (StringUtils.hasText(phoneNumber)) {
                Long.valueOf(phoneNumber);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isEmpty(Number rowNumber, List<ErrorDto> errors, String customerName, String message) {
        boolean isEmpty = true;
        if (!StringUtils.hasText(customerName)) {
            errors.add(new ErrorDto(message, "IGNORED", rowNumber.intValue(), -1L));
            log.error("NOT ADDED:: Empty value row number : " + rowNumber + message);
            isEmpty = false;
        }
        return isEmpty;
    }

    public void checkExistingAndSaveCustomer(List<XlsLeadRowDto> rows) {
        log.info("Start saving customer" + LocalDateTime.now());
        Set<Customer> customerToImport = new HashSet<>();
        List<XlsLeadRowDto> customerPhones = rows.stream().filter(row -> checkValidPhone(row.getCustomerPhone())).distinct().collect(Collectors.toList());
        for (XlsLeadRowDto xlsLeadRowDto : customerPhones) {
            try {
                Customer dbCustomer = customerService.findByPhone(xlsLeadRowDto.getCustomerPhone().trim());
                if (Objects.isNull(dbCustomer)) {
                    Customer customer = new Customer();
                    customer.setName(xlsLeadRowDto.getCustomerName().trim());
                    customer.setPhone(xlsLeadRowDto.getCustomerPhone().trim());
                    customer.setEmail(xlsLeadRowDto.getCustomerEmail().trim());
                    customer.setAlternatePhone(xlsLeadRowDto.getCustomerAlternatePhone());
                    customerToImport.add(customer);
                } else {
                    log.info("Customer with name : " + dbCustomer.getName() + " and mobile: " + dbCustomer.getPhone() + " Already in system ");
                }
            } catch (Exception e) {
                throw new LeadException("Error for mobile no." + xlsLeadRowDto.getCustomerPhone() + " Please skip this record for now , Details--> findByPhone " + e.getLocalizedMessage());
            }
        }
        if (!customerToImport.isEmpty()) {
            log.info("Db customer load" + LocalDateTime.now());
            customerService.saveAll(customerToImport);
            log.info("Db customer End" + LocalDateTime.now());
        }
        log.info("End saving customer" + LocalDateTime.now());
    }


}
