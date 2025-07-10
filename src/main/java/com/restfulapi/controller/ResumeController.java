package com.restfulapi.controller;

import com.restfulapi.dto.ResponseUpdateResume;
import com.restfulapi.dto.ResumeResponseDTO;
import com.restfulapi.entity.Resume;
import com.restfulapi.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/resumes")
    public ResponseEntity<ResumeResponseDTO> createResume(@RequestBody Resume resume){
        Resume createResume=resumeService.createResume(resume);
        return ResponseEntity.ok(resumeService.convertToResumeDTO(createResume));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/resumes")
    public ResponseEntity<List<ResumeResponseDTO>>getAllResume(){
        return ResponseEntity.ok(resumeService.getListResume());
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/resumes/{id}")
    public ResponseEntity<ResumeResponseDTO> getResumeById(@PathVariable long id){
        Optional<Resume>getResume=resumeService.getResumeById(id);
        if (getResume.isEmpty()){
            throw new RuntimeException("Không tìm thấy Resume với ID: " + id);
        }
        ResumeResponseDTO resumeResponseDTO=resumeService.convertToResumeDTO(getResume.get());
        return ResponseEntity.ok(resumeResponseDTO);
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/resumes")
    public ResponseEntity<ResponseUpdateResume>updateResume(@RequestBody Resume resume){
        Resume updateResume=resumeService.updateResume(resume);
        return ResponseEntity.ok(resumeService.convertToUpdateResume(updateResume));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<Void>deleteResume(@PathVariable long id){
        Optional<Resume>getResume=resumeService.getResumeById(id);
        if (getResume.isEmpty()){
            throw new RuntimeException("Không tìm thấy Resume với ID: " + id);
        }
        resumeService.deleteResume(id);
        return ResponseEntity.noContent().build();
    }
}
