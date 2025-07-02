package com.restfulapi.service;

import com.restfulapi.dto.JobResponseDTO;
import com.restfulapi.dto.ResponseUserDTO;
import com.restfulapi.entity.Job;
import com.restfulapi.entity.Skill;
import com.restfulapi.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillService skillService;

    public JobService(SkillService skillService,JobRepository jobRepository) {
        this.skillService = skillService;
        this.jobRepository=jobRepository;
    }

    public Job createJob(Job job) {
        if (job.getSkills() != null) {
            List<Skill> validSkill = new ArrayList<>();
            for (Skill s : job.getSkills()) {
                Long skillId = s.getId();
                Optional<Skill> existingSkill = skillService.getSkillById(skillId);
                if (existingSkill.isEmpty()) {
                    throw new RuntimeException("Skill với ID " + skillId + " không tồn tại");
                }
                validSkill.add(existingSkill.get());
            }
            job.setSkills(validSkill);
        }
        return jobRepository.save(job);
    }
    public JobResponseDTO convertToJobResponseDTO(Job job){
        JobResponseDTO dto=new JobResponseDTO();
        dto.setId(job.getId());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setQuantity(job.getQuantity());
        dto.setLevel(job.getLevel());
        dto.setDescription(job.getDescription());
        dto.setActive(job.isActive());
        dto.setStartDate(job.getStartDate());
        dto.setEndDate(job.getEndDate());
        if (job.getSkills()!=null){
            List<String>skillName=job.getSkills()
                    .stream()               // ① chuyển List<Skill> thành dòng dữ liệu để xử lý
                    .map(Skill::getName)    // ② lấy ra mỗi cái "name" của skill
                    .collect(Collectors.toList()); // ③ gom tất cả tên skill lại thành List<String>
            dto.setSkillName(skillName);// ④ gán List tên skill vào dto
        }
        return dto;
    }
    public JobResponseDTO updateJob(Job job){
        return convertToJobResponseDTO(job);
    }
    public Optional<Job> getJobById(long id){
        return jobRepository.findById(id);
    }
    public List<Job>getListJob(){
        return jobRepository.findAll();
    }
    public void deleteJobById(long id){
        jobRepository.deleteById(id);
    }
}
