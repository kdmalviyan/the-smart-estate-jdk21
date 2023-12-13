package com.sfd.thesmartestate.projects.services;

import com.sfd.thesmartestate.common.services.FileService;
import com.sfd.thesmartestate.projects.dto.ProjectDTO;
import com.sfd.thesmartestate.projects.entities.FileMetadata;
import com.sfd.thesmartestate.projects.entities.Project;
import com.sfd.thesmartestate.projects.exceptions.FileUploadException;
import com.sfd.thesmartestate.projects.repositories.FileMetaDataRepository;
import com.sfd.thesmartestate.projects.repositories.ProjectRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final FileService fileService;
    private final FileMetaDataRepository fileMetaDataRepository;

    @Value("${aws.s3.upload-bucket}")
    private String bucketName;

    @Override
    public Project create(Project project) {
        if (Objects.nonNull(projectRepository.findByName(project.getName()).orElse(null))) {
            throw new IllegalArgumentException("Project with name:" + project.getName() + "already exists");
        }
        project.setCreatedAt(LocalDateTime.now());
        return projectRepository.saveAndFlush(project);
    }

    @Override
    public Project update(Project project) {
        project.setLastUpdateAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    @Override
    public Project findById(Long id) {

        return projectRepository.findById(id).orElse(null);
    }

    @Override
    public List<Project> findAll() {

        return projectRepository.findAll();
    }

    @Override
    public List<ProjectDTO> findAllMinimalFields() {
        List<ProjectDTO> projects = projectRepository.findAllMinimal();
        projects = projects.stream().filter(ProjectDTO::getEnabled).distinct().collect(Collectors.toList());
        return projects;
    }

    @Override
    public void delete(Project project) {
        projectRepository.delete(project);
    }

    @Override
    public Project findByName(String projectName) {
        return projectRepository.findByName(projectName).orElse(null);
    }


    @Override
    public Project uploadAndSaveFile(Long projectId, String fileType, String vendorId, MultipartFile file) {
        validateFile(file);
        Project project = projectRepository.findById(projectId).orElse(null);
        assert project != null;
        String projectName = project.getName();
        // Save Image in S3 and then save
        String path = String.format("%s/projects/%s/%s", bucketName, projectName, fileType);
        String fileName = String.format("%s", file.getOriginalFilename());

        if ("priceList".equalsIgnoreCase(fileType)) {
            Integer size = project.getPriceFiles().size() + 1;
            fileName = size + "^" + fileName;
        }

        fileService.uploadFileToS3(path, fileName, file);
        String pathToSave = String.format("projects/%s/%s/%s", projectName, fileType, fileName);

        if ("brochure".equalsIgnoreCase(fileType)) {
            updateProjectBrochureFilePath(project, pathToSave, fileName, fileType);
        } else if ("priceList".equalsIgnoreCase(fileType)) {
            updateProjectPriceListFilePath(project, pathToSave, fileName, fileType);
        }

        return projectRepository.saveAndFlush(project);

    }

    private void validateFile(MultipartFile file) {
        //check if the file is empty
        if (file.isEmpty()) {
            throw new FileUploadException("Cannot upload empty file");
        }
        //Check if the file is a pdf
        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
            throw new FileUploadException("File uploaded is not an pdf");
        }
    }

    @Override

    public Resource downloadImage(Long fileId) {
        FileMetadata fileData = fileMetaDataRepository.findById(fileId).orElse(null);
        if (fileData != null) {
            return fileService.download(fileData.getFilePath(), bucketName);
        }
        return null;
    }


    @Override
    public Project deleteFile(Long projectId, Long id) {
        Project project = projectRepository.findById(projectId).orElse(null);
        FileMetadata fileData = fileMetaDataRepository.findById(id).orElse(null);
        if (fileData != null) {
            fileService.delete(fileData.getFilePath(), bucketName);
            assert project != null;
            project.getPriceFiles().remove(fileData);
            fileMetaDataRepository.deleteById(id);
        }
        return project;
    }


    private void updateProjectBrochureFilePath(Project project, String pathToSave, String fileName, String fileType) {
        FileMetadata fileMetadata = project.getBrochure();
        if (fileMetadata == null) {
            fileMetadata = new FileMetadata();
        }
        fileMetadata.setFilePath(pathToSave);
        fileMetadata.setFileName(fileName);
        fileMetadata.setFileType(fileType);
        fileMetadata.setCreatedAt(LocalDateTime.now());
        //fileMetadata.setCreatedBy(userService.findLoggedInUser());
        project.setBrochure(fileMetadata);
    }

    private void updateProjectPriceListFilePath(Project project, String pathToSave, String fileName, String fileType) {
        Set<FileMetadata> priceFilesList = project.getPriceFiles();
        if (priceFilesList == null) {
            priceFilesList = new HashSet<>();
        }
        //new Entry
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFilePath(pathToSave);
        fileMetadata.setFileName(fileName);
        fileMetadata.setFileType(fileType);
        fileMetadata.setCreatedAt(LocalDateTime.now());
        // fileMetadata.setCreatedBy(userService.findLoggedInUser());
        //add new entry
        priceFilesList.add(fileMetadata);
        //set
        project.setPriceFiles(priceFilesList);
    }
}
