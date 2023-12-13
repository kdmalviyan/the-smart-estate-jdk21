package com.sfd.thesmartestate.projects.repositories;

import com.sfd.thesmartestate.projects.entities.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetaDataRepository extends JpaRepository<FileMetadata, Long> {
}
