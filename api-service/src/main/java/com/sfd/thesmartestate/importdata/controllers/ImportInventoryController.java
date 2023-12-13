package com.sfd.thesmartestate.importdata.controllers;

import com.sfd.thesmartestate.importdata.dto.ImportInventoryResponseDto;
import com.sfd.thesmartestate.importdata.services.ImportInventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "uploadProjectInventory")
@AllArgsConstructor
@CrossOrigin("*")
public class ImportInventoryController {

    private final ImportInventoryService importInventoryService;

    @PostMapping("/{projectId}")
    public ResponseEntity<ImportInventoryResponseDto> uploadLead(@RequestParam("file") MultipartFile file, @PathVariable("projectId") Long projectId) {
        ImportInventoryResponseDto responseDto = new ImportInventoryResponseDto();

        responseDto = importInventoryService.uploadInventory(file, responseDto, projectId);
        if (importInventoryService.hasExcelFormat(file)) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

}
