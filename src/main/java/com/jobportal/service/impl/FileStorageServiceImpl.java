package com.jobportal.service.impl;

import com.jobportal.config.FileStorageConfig;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.exception.StorageException;
import com.jobportal.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageServiceImpl(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new StorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file, Long userId) {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        
        // Check if the file's name contains invalid characters
        if (originalFileName.contains("..")) {
            throw new StorageException("Sorry! Filename contains invalid path sequence " + originalFileName);
        }
        
        // Generate a file name based on userId to ensure uniqueness
        String fileExtension = getFileExtension(originalFileName);
        String fileName = "resume_" + userId + fileExtension;
        
        try {
            // Create the directory if it doesn't exist
            Path userDirectory = this.fileStorageLocation.resolve(userId.toString());
            if (!Files.exists(userDirectory)) {
                Files.createDirectories(userDirectory);
            }
            
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = userDirectory.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new StorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public String getResumeFileName(Long userId) {
        Path userDirectory = this.fileStorageLocation.resolve(userId.toString());
        
        try {
            if (Files.exists(userDirectory)) {
                return Files.list(userDirectory)
                        .filter(path -> path.getFileName().toString().startsWith("resume_"))
                        .findFirst()
                        .map(path -> path.getFileName().toString())
                        .orElse(null);
            }
            return null;
        } catch (IOException ex) {
            throw new StorageException("Failed to get resume file name", ex);
        }
    }
    
    @Override
    public Path getResumeFilePath(Long userId) {
        String fileName = getResumeFileName(userId);
        if (fileName == null) {
            throw new ResourceNotFoundException("Resume not found for user " + userId);
        }
        
        return this.fileStorageLocation.resolve(userId.toString()).resolve(fileName);
    }
    
    @Override
    public boolean deleteResume(Long userId) {
        try {
            String fileName = getResumeFileName(userId);
            if (fileName == null) {
                return false;
            }
            
            Path filePath = this.fileStorageLocation.resolve(userId.toString()).resolve(fileName);
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new StorageException("Could not delete resume file for user " + userId, ex);
        }
    }
    
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.fileStorageLocation, 2)
                    .filter(path -> !path.equals(this.fileStorageLocation))
                    .filter(path -> path.getFileName().toString().startsWith("resume_"));
        } catch (IOException ex) {
            throw new StorageException("Could not load the files!", ex);
        }
    }
    
    public Resource loadFileAsResource(Path filePath) {
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found: " + filePath);
            }
        } catch (MalformedURLException ex) {
            throw new StorageException("File not found: " + filePath, ex);
        }
    }
    
    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") > 0) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
}