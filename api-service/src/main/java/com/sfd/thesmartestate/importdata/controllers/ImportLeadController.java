package com.sfd.thesmartestate.importdata.controllers;

import com.sfd.thesmartestate.importdata.dto.ImportResponseDto;
import com.sfd.thesmartestate.importdata.services.ImportLeadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "uploadLead")
@AllArgsConstructor
@CrossOrigin("*")
public class ImportLeadController {

    private final ImportLeadService importLeadService;

    @PostMapping
    public ResponseEntity<ImportResponseDto> uploadLead(@RequestParam("file") MultipartFile file) {
        ImportResponseDto responseDto = new ImportResponseDto();

        if (Boolean.TRUE.equals(importLeadService.hasExcelFormat(file))) {
            responseDto = importLeadService.uploadLeads(file, responseDto);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

}
