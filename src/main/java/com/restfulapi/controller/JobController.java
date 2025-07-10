package com.restfulapi.controller;

import com.restfulapi.dto.JobResponseDTO;
import com.restfulapi.entity.Job;
import com.restfulapi.service.JobService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/jobs")
    public ResponseEntity<JobResponseDTO> createJob(@RequestBody Job job){
        Job createJob=jobService.createJob(job);
        return ResponseEntity.ok(jobService.convertToJobResponseDTO(createJob));
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable long id){
        Optional<Job> getJob=jobService.getJobById(id);
        if (getJob.isEmpty()){
            throw new RuntimeException("Không tìm thấy Job với ID: " + id);
        }
        return ResponseEntity.ok(jobService.convertToJobResponseDTO(getJob.get()));
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponseDTO>>getAllJob(@RequestParam(value = "page",required = false)int page,@RequestParam(value = "size",required = false)int size){
        Pageable pageable= PageRequest.of(page-1,size);
        List<Job>jobs=jobService.getListJob(pageable);
        List<JobResponseDTO>jobResponseDTOList=new ArrayList<>();
        for (Job job:jobs){
            JobResponseDTO dto=jobService.convertToJobResponseDTO(job);
            jobResponseDTOList.add(dto);
        }
        return ResponseEntity.ok(jobResponseDTOList);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/jobs")
    public ResponseEntity<JobResponseDTO>updateJob(@RequestBody Job job){
        Optional<Job>getJob=jobService.getJobById(job.getId());
        if (getJob.isEmpty()){
            throw new RuntimeException("Không tìm thấy Job với ID: " + job.getId());
        }
        return ResponseEntity.ok(jobService.updateJob(job));

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void>deleteJobById(@PathVariable long id){
        Optional<Job> getJob=jobService.getJobById(id);
        if (getJob.isEmpty()){
            throw new RuntimeException("Không tìm thấy Job với ID: " + id);
        }
        jobService.deleteJobById(getJob.get().getId());
        return ResponseEntity.noContent().build();
    }
}
