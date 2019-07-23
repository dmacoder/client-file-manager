package com.bellsoft.updater.api.v1.repository.file;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bellsoft.updater.api.v1.domain.file.FimClientFileTb;

public interface UpdaterRepository extends JpaRepository<FimClientFileTb, String> {
    
}
