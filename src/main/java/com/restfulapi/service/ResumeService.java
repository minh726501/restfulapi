package com.restfulapi.service;
import com.restfulapi.dto.ResumeResponseDTO;
import com.restfulapi.dto.SimpleJobDTO;
import com.restfulapi.dto.SimpleUserDTO;
import com.restfulapi.entity.Job;
import com.restfulapi.entity.Resume;
import com.restfulapi.entity.User;
import com.restfulapi.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserService userService;
    private final JobService jobService;

    public ResumeService(ResumeRepository resumeRepository, UserService userService, JobService jobService) {
        this.resumeRepository = resumeRepository;
        this.userService = userService;
        this.jobService = jobService;
    }

    public Resume createResume(Resume resume){
        if(resume.getUser()!=null){
            Long userId=resume.getUser().getId();
            User getUser=userService.fetchUserById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy User với ID: " + userId));
            resume.setUser(getUser);
        }
        if (resume.getJob()!=null){
            Long jobId=resume.getJob().getId();
            Job getJob=jobService.getJobById(jobId).orElseThrow(() -> new RuntimeException("Không tìm thấy User với ID: " + jobId));
            resume.setJob(getJob);

        }
        return resumeRepository.save(resume);


    }
    public ResumeResponseDTO convertToResumeDTO(Resume resume){
        ResumeResponseDTO resumeResponseDTO=new ResumeResponseDTO();
        resumeResponseDTO.setId(resume.getId());
        resumeResponseDTO.setUrl(resume.getUrl());
        resumeResponseDTO.setStatus(resume.getStatus().name());
        resumeResponseDTO.setEmail(resume.getEmail());
        resumeResponseDTO.setCreatedBy(resume.getCreatedBy());
        resumeResponseDTO.setCreatedAt(resume.getCreatedAt());
        if (resume.getUser()!=null) {
            SimpleUserDTO simpleUserDTO = new SimpleUserDTO();
            simpleUserDTO.setId(resume.getUser().getId());
            simpleUserDTO.setName(resume.getUser().getName());
            simpleUserDTO.setEmail(resume.getUser().getEmail());
            resumeResponseDTO.setUser(simpleUserDTO);
        }
        if (resume.getJob()!=null){
            SimpleJobDTO simpleJobDTO=new SimpleJobDTO();
            simpleJobDTO.setId(resume.getJob().getId());
            simpleJobDTO.setSalary(resume.getJob().getSalary());
            simpleJobDTO.setLocation(resume.getJob().getLocation());
            simpleJobDTO.setLevel(resume.getJob().getLevel().name());
            resumeResponseDTO.setJob(simpleJobDTO);
        }
        return resumeResponseDTO;
    }
    public List<ResumeResponseDTO>getListResume(){
        List<Resume>getListResume=resumeRepository.findAll();
        List<ResumeResponseDTO>resumeResponseDTOS=new ArrayList<>();
        for (Resume resume:getListResume){
            ResumeResponseDTO dto=convertToResumeDTO(resume);
            resumeResponseDTOS.add(dto);
        }
        return resumeResponseDTOS;
    }
    public Optional<Resume> getResumeById(long id){
        return resumeRepository.findById(id);
    }
}
