package com.sfd.thesmartestate.importdata.services;

import com.sfd.thesmartestate.importdata.dto.ImportResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImportLeadService {

    ImportResponseDto uploadLeads(MultipartFile file, ImportResponseDto responseDto);

    Boolean hasExcelFormat(MultipartFile file);
}
