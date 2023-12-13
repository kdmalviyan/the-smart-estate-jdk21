package com.sfd.thesmartestate.importdata.services;

import com.sfd.thesmartestate.importdata.dto.ImportInventoryResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImportInventoryService {

    ImportInventoryResponseDto uploadInventory(MultipartFile file, ImportInventoryResponseDto responseDto, Long projectId);

    Boolean hasExcelFormat(MultipartFile file);
}
