package com.sfd.thesmartestate.importdata.services;

import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.importdata.dto.ImportInventoryResponseDto;
import com.sfd.thesmartestate.importdata.dto.XlsInventoryRowDto;
import com.sfd.thesmartestate.projects.entities.InventoryStatus;
import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.projects.inventory.Inventory;
import com.sfd.thesmartestate.projects.inventory.InventoryService;
import com.sfd.thesmartestate.projects.services.InventoryStatusService;
import com.sfd.thesmartestate.projects.services.ProjectService;
import com.sfd.thesmartestate.users.entities.User;
import com.sfd.thesmartestate.users.services.UserService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class ImportInventoryServiceImpl implements ImportInventoryService {

    private final InventoryStatusService inventoryStatusService;
    private final InventoryService inventoryService;
    private final ProjectService projectService;
    private final UserService userService;
    public static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    //Checks file format
    public Boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    @Override
    public ImportInventoryResponseDto uploadInventory(MultipartFile file, ImportInventoryResponseDto responseDto, Long projectId) {

        log.info("Start saving Leads" + LocalDateTime.now());
        if (file == null) {
            responseDto.setErrors(Collections.singletonList("File must not be null"));
            return responseDto;
        }
        if (Boolean.FALSE.equals(hasExcelFormat(file))) {
            responseDto.setMessage("Please upload a file with excel format");
            return responseDto;
        }
        //upload file and save data
        InventoryImportData inventoryimportdata = uploadFileAndSaveInventory(file, projectId);
        // prepare response
        prepareResponse(responseDto, inventoryimportdata);

        return responseDto;
    }

    private void prepareResponse(ImportInventoryResponseDto responseDto, InventoryImportData inventoryImportData) {
        responseDto.setMessage("Inventory File Uploaded successfully");
        log.info("Inventory File Uploaded successfully. Total inventories added" + inventoryImportData.getInventories());
        responseDto.setErrors(inventoryImportData.getErrors());
        responseDto.setInventories(inventoryImportData.getInventories());
        responseDto.setRowsSuccess(inventoryImportData.getInventories().size());
    }

    private InventoryImportData uploadFileAndSaveInventory(MultipartFile file, Long projectId) {
        XlsInventoryFileReader fileReader = new XlsInventoryFileReader();
        //Excel data in bean
        List<String> errors = new ArrayList<>();
        //load inventory rows
        List<XlsInventoryRowDto> rows = fileReader.getInventoryRows(file, errors);
        // import response
        InventoryImportData inventoryImportData = new InventoryImportData();

        Set<Inventory> inventories = new HashSet<>();
        Project project = projectService.findById(projectId);
        Set<Inventory> projectInventories = project.getInventories();
        InventoryStatus inventoryStatus = inventoryStatusService.findByName(Constants.AVAILABLE);
        User loggedInUser = userService.findLoggedInUser();
        for (XlsInventoryRowDto xlsInventoryRowDto : rows) {
            try {
                if (isInventoryValid(xlsInventoryRowDto.getTower(), xlsInventoryRowDto.getInventoryName(), projectInventories)) {
                    Inventory inventory = new Inventory();
                    inventory.setCreatedAt(LocalDateTime.now());
                    inventory.setCorner(xlsInventoryRowDto.isCorner());
                    inventory.setName(xlsInventoryRowDto.getInventoryName());
                    inventory.setSize(Long.valueOf(xlsInventoryRowDto.getSize()));
                    inventory.setTower(xlsInventoryRowDto.getTower());
                    inventory.setInventoryStatus(inventoryStatus);
                    inventories.add(inventory);
                } else {
                    errors.add("Entry already exists for tower-" + xlsInventoryRowDto.getTower()
                            + " and Inventory-" + xlsInventoryRowDto.getInventoryName());
                }
            } catch (Exception e) {
                log.error("Error in creating inventory bean {}", e.getMessage());
            }
        }
        projectInventories.addAll(inventories);
        project = projectService.update(project);
        inventoryImportData.setInventories(project.getInventories());
        inventoryImportData.setErrors(errors);

        return inventoryImportData;
    }

    private boolean isInventoryValid(String tower, String inventoryName, Set<Inventory> inventories) {
        return inventories.stream().noneMatch(inventory -> inventory.getName().equalsIgnoreCase(inventoryName)
                && inventory.getTower().equalsIgnoreCase(tower));
    }

}

@Data
class InventoryImportData {
    int rowNumber;
    int rowsSkipped;
    Set<Inventory> inventories = new HashSet<>();
    List<String> errors = new ArrayList<>();
}

