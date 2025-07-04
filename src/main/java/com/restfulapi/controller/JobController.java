package com.restfulapi.controller;

import com.restfulapi.dto.JobResponseDTO;
import com.restfulapi.entity.Job;
import com.restfulapi.service.JobService;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/jobs")
    public ResponseEntity<JobResponseDTO> createJob(@RequestBody Job job){
        Job createJob=jobService.createJob(job);
        return ResponseEntity.ok(jobService.convertToJobResponseDTO(createJob));
    }
    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable long id){
        Optional<Job> getJob=jobService.getJobById(id);
        if (getJob.isEmpty()){
            throw new RuntimeException("Không tìm thấy Job với ID: " + id);
        }
        return ResponseEntity.ok(jobService.convertToJobResponseDTO(getJob.get()));
    }
    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponseDTO>>getAllJob(){
        List<Job>jobs=jobService.getListJob();
        List<JobResponseDTO>jobResponseDTOList=new ArrayList<>();
        for (Job job:jobs){
            JobResponseDTO dto=jobService.convertToJobResponseDTO(job);
            jobResponseDTOList.add(dto);
        }
        return ResponseEntity.ok(jobResponseDTOList);
    }
    @PutMapping("/jobs")
    public ResponseEntity<JobResponseDTO>updateJob(@RequestBody Job job){
        Optional<Job>getJob=jobService.getJobById(job.getId());
        if (getJob.isEmpty()){
            throw new RuntimeException("Không tìm thấy Job với ID: " + getJob.get().getId());
        }
        return ResponseEntity.ok(jobService.updateJob(job));

    }
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
