package com.restfulapi.controller;

import com.restfulapi.dto.ResumeResponseDTO;
import com.restfulapi.entity.Resume;
import com.restfulapi.service.ResumeService;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/resumes")
    public ResponseEntity<ResumeResponseDTO> createResume(@RequestBody Resume resume){
        Resume createResume=resumeService.createResume(resume);
        return ResponseEntity.ok(resumeService.convertToResumeDTO(createResume));
    }
    @GetMapping("/resumes")
    public ResponseEntity<List<ResumeResponseDTO>>getAllResume(){
        return ResponseEntity.ok(resumeService.getListResume());
    }
    @GetMapping("/resumes/{id}")
    public ResponseEntity<ResumeResponseDTO> getResumeById(@PathVariable long id){
        Optional<Resume>getResume=resumeService.getResumeById(id);
        if (getResume.isEmpty()){
            throw new RuntimeException("Không tìm thấy Resume với ID: " + id);
        }
        ResumeResponseDTO resumeResponseDTO=resumeService.convertToResumeDTO(getResume.get());
        return ResponseEntity.ok(resumeResponseDTO);
    }
}
