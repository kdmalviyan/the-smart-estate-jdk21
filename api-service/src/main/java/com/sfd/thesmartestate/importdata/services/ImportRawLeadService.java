package com.sfd.thesmartestate.importdata.services;

import com.sfd.thesmartestate.customer.services.CustomerService;
import com.sfd.thesmartestate.importdata.dto.ErrorDto;
import com.sfd.thesmartestate.importdata.dto.ImportResponseDto;
import com.sfd.thesmartestate.importdata.dto.XlsLeadRowDto;
import com.sfd.thesmartestate.lms.rawleads.RawLead;
import com.sfd.thesmartestate.lms.rawleads.RawLeadService;
import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.services.UserService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.sfd.thesmartestate.importdata.services.ImportLeadServiceImpl.FILE_TYPE;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class ImportRawLeadService implements ImportLeadService {
    private final CustomerService customerService;
    private final RawLeadService rawLeadService;
    private final UserService userService;

    @Override

    public ImportResponseDto uploadLeads(MultipartFile file, ImportResponseDto responseDto) {
        log.info("Start saving Leads" + LocalDateTime.now());
        if (file == null) {
            responseDto.setErrors(Collections.singletonList(new ErrorDto("File must not be null", "FILE_EMPTY", -1, -1L)));
            return responseDto;
        }
        if (Boolean.TRUE.equals(hasExcelFormat(file))) {
            //upload file and save data
            RawLeadImportData leadImportData = uploadFileAndSaveRawLeads(file);
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


    private RawLeadImportData uploadFileAndSaveRawLeads(MultipartFile file) {
        XlsLeadFileReader fileReader = new XlsLeadFileReader();
        //Excel data in bean
        List<XlsLeadRowDto> rows = fileReader.getRawLeadRows(file);

        RawLeadImportData leadImportData = validateAndGenerateLeadData(rows);
        Set<RawLead> leadsToSave = leadImportData.getLeads();
        if (!leadsToSave.isEmpty()) {
            log.info("Started saving" + LocalDateTime.now());
            rawLeadService.saveAll(leadsToSave);
            log.info("End saving" + LocalDateTime.now());
        }
        return leadImportData;
    }

    private RawLeadImportData validateAndGenerateLeadData(List<XlsLeadRowDto> rows) {
        RawLeadImportData leadImportData = new RawLeadImportData();
        Employee loggedInEmployee = userService.findLoggedInUser();

        int rowNumber = 1;
        int rowsSkipped = 0;
        List<ErrorDto> errors = new ArrayList<>();
        List<ErrorDto> warnings = new ArrayList<>();
        Set<RawLead> rawLeadsToSave = new HashSet<>();
        Set<String> uniquePhoneNumbers = new HashSet<>();

        for (XlsLeadRowDto xlsRow : rows) {
            if (uniquePhoneNumbers.contains(xlsRow.getCustomerPhone())) {
                continue;
            } else {
                uniquePhoneNumbers.add(xlsRow.getCustomerPhone());
            }

            //Check Lead duplicate by phone
            RawLead rawLead = rawLeadService.findByCustomerPhone(xlsRow.getCustomerPhone());
            xlsRow.setRownum(rowNumber);

            if (Objects.isNull(rawLead)) {
                RawLead rawLeadData = createRawLeadData(loggedInEmployee, xlsRow);
                rawLeadsToSave.add(rawLeadData);
            } else {
                rowsSkipped++;
            }
            rowNumber++;
        }

        leadImportData.setRowsSkipped(rowsSkipped);
        leadImportData.setRowNumber(rowNumber);
        leadImportData.setLeads(rawLeadsToSave);
        leadImportData.setWarnings(warnings);
        leadImportData.setErrors(errors);
        return leadImportData;
    }

    private RawLead createRawLeadData(Employee loggedInEmployee, XlsLeadRowDto xlsRow) {
        RawLead rawLead = new RawLead();
        rawLead.setComment(xlsRow.getComment());
        rawLead.setLeadDate(LocalDate.now());
        rawLead.setCustomerEmail(xlsRow.getCustomerEmail());
        rawLead.setCustomerName(xlsRow.getCustomerName());
        rawLead.setProjectName(xlsRow.getProjectName());
        rawLead.setCreatedAt(LocalDateTime.now());
        rawLead.setCustomerPhone(xlsRow.getCustomerPhone());
        log.info("Lead Ready to save phone -{},project -{},", xlsRow.getCustomerPhone(), xlsRow.getProjectName());
        return rawLead;
    }

    public void prepareResponse(ImportResponseDto responseDto, RawLeadImportData leadImportData) {
        responseDto.setMessage("File Uploaded successfully");
        log.info("File Uploaded successfully. Total " + leadImportData.getLeads().size() + " records added.");
        responseDto.setRowsSkipped(leadImportData.getRowsSkipped());
        responseDto.setWarnings(leadImportData.getWarnings());
        responseDto.setErrors(leadImportData.getErrors());
        responseDto.setRowsSuccess(leadImportData.getLeads().size());
    }

    @Override
    public Boolean hasExcelFormat(MultipartFile file) {
        return FILE_TYPE.equals(file.getContentType());
    }
}

@Data
class RawLeadImportData {
    int rowNumber;
    int rowsSkipped;
    Set<RawLead> leads = new HashSet<>();
    List<ErrorDto> errors = new ArrayList<>();
    List<ErrorDto> warnings = new ArrayList<>();
}

