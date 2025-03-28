package com.jobportal.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {
    
    String storeFile(MultipartFile file, Long userId);
    
    String getResumeFileName(Long userId);
    
    Path getResumeFilePath(Long userId);
    
    boolean deleteResume(Long userId);
    
    Stream<Path> loadAll();
    
    Resource loadFileAsResource(Path filePath);
}