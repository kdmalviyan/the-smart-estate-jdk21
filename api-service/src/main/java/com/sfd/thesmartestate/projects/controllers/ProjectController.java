package com.sfd.thesmartestate.projects.controllers;

import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.projects.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "project")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.create(project));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<Project>> listAll() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @PutMapping
    public ResponseEntity<Project> update(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.update(project));
    }

    @GetMapping("/minimalFields")
    public ResponseEntity<List<ProjectDTO>> minimalFields() {
        return ResponseEntity.ok(projectService.findAllMinimalFields());
    }

    @PostMapping(
            path = "/file/upload/{projectId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Project> saveTodo(@RequestParam("fileType") String fileType,
                                            @PathVariable("projectId") Long projectId,
                                            @RequestHeader("X-VendorID") String vendorId,
                                            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(projectService.uploadAndSaveFile(projectId, fileType, vendorId, file));
    }

    @GetMapping(value = "/file/download/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadTodoImage(@PathVariable("fileId") Long fileId) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test")
                .body(projectService.downloadImage(fileId));
    }

    @GetMapping(value = "/file/delete/{projectId}/{fileId}")
    public ResponseEntity<Project> deleteFile(@PathVariable("projectId") Long projectId, @PathVariable("fileId") Long fileId) {
        return ResponseEntity.ok().body(projectService.deleteFile(projectId, fileId));
    }
}
