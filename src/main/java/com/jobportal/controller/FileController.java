package com.jobportal.controller;

import com.jobportal.dto.ResumeResponse;
import com.jobportal.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/resumes")
    public ResponseEntity<List<ResumeResponse>> getAllResumes() {
        List<ResumeResponse> resumeResponses = fileStorageService.loadAll()
                .map(path -> {
                    String fileName = path.getFileName().toString();
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/users/resume/")
                            .path(path.getParent().getFileName().toString())
                            .toUriString();
                    try {
                        return ResumeResponse.builder()
                                .fileName(fileName)
                                .fileDownloadUri(fileDownloadUri)
                                .fileType(Files.probeContentType(path))
                                .size(Files.size(path))
                                .build();
                    } catch (Exception e) {
                        return ResumeResponse.builder()
                                .fileName(fileName)
                                .fileDownloadUri(fileDownloadUri)
                                .message("Error retrieving file details")
                                .build();
                    }
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(resumeResponses);
    }
}