package com.sfd.thesmartestate.importdata.controllers;

import com.sfd.thesmartestate.importdata.dto.ImportResponseDto;
import com.sfd.thesmartestate.importdata.services.ImportRawLeadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "uploadRawLead")
@AllArgsConstructor
@CrossOrigin("*")
public class ImportRawLeadController {

    private final ImportRawLeadService importRawLeadService;

    @PostMapping
    public ResponseEntity<ImportResponseDto> uploadLead(@RequestParam("file") MultipartFile file) {
        ImportResponseDto responseDto = new ImportResponseDto();

        if (Boolean.TRUE.equals(importRawLeadService.hasExcelFormat(file))) {
            responseDto = importRawLeadService.uploadLeads(file, responseDto);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

}
