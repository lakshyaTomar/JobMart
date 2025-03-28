package com.jobportal.controller;

import com.jobportal.dto.ResumeResponse;
import com.jobportal.dto.UserDTO;
import com.jobportal.enums.UserRole;
import com.jobportal.service.FileStorageService;
import com.jobportal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUserProfile() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateCurrentUserProfile(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUserProfile(userDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
    
    @PostMapping("/resume")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<ResumeResponse> uploadResume(@RequestParam("file") MultipartFile file) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO currentUser = userService.getCurrentUserProfile();
        
        if (currentUser.getRole() != UserRole.JOB_SEEKER) {
            return ResponseEntity.badRequest().body(
                ResumeResponse.builder()
                    .message("Only job seekers can upload resumes")
                    .build()
            );
        }
        
        // Validate file type
        String fileExtension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename())).toLowerCase();
        if (!isValidResumeExtension(fileExtension)) {
            return ResponseEntity.badRequest().body(
                ResumeResponse.builder()
                    .message("Only PDF, DOC, DOCX files are allowed")
                    .build()
            );
        }
        
        String fileName = fileStorageService.storeFile(file, currentUser.getId());
        
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/resume/")
                .toUriString();
        
        // Update user profile with resume link
        currentUser.setResume(fileName);
        userService.updateUserProfile(currentUser);
        
        return ResponseEntity.ok(ResumeResponse.builder()
                .fileName(fileName)
                .fileDownloadUri(fileDownloadUri)
                .fileType(file.getContentType())
                .size(file.getSize())
                .message("Resume uploaded successfully")
                .build());
    }
    
    @GetMapping("/resume")
    public ResponseEntity<Resource> getResume() {
        // Get current authenticated user
        UserDTO currentUser = userService.getCurrentUserProfile();
        
        try {
            Path filePath = fileStorageService.getResumeFilePath(currentUser.getId());
            Resource resource = fileStorageService.loadFileAsResource(filePath);
            
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/resume/{userId}")
    @PreAuthorize("hasAnyAuthority('EMPLOYER', 'ADMIN')")
    public ResponseEntity<Resource> getResumeByUserId(@PathVariable Long userId) {
        try {
            Path filePath = fileStorageService.getResumeFilePath(userId);
            Resource resource = fileStorageService.loadFileAsResource(filePath);
            
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/resume")
    @PreAuthorize("hasAuthority('JOB_SEEKER')")
    public ResponseEntity<ResumeResponse> deleteResume() {
        // Get current authenticated user
        UserDTO currentUser = userService.getCurrentUserProfile();
        
        boolean deleted = fileStorageService.deleteResume(currentUser.getId());
        
        if (deleted) {
            // Update user profile to remove resume link
            currentUser.setResume(null);
            userService.updateUserProfile(currentUser);
            
            return ResponseEntity.ok(ResumeResponse.builder()
                    .message("Resume deleted successfully")
                    .build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") > 0) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
    
    private boolean isValidResumeExtension(String fileExtension) {
        return fileExtension.equals(".pdf") || 
               fileExtension.equals(".doc") || 
               fileExtension.equals(".docx");
    }
}
