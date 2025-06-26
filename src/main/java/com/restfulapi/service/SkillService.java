package com.restfulapi.service;

import com.restfulapi.entity.Skill;
import com.restfulapi.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill saveSkill(Skill skill){
        return skillRepository.save(skill);
    }
    public boolean existsSkillName(String name){
        return skillRepository.existsByName(name);
    }
    public Optional<Skill> getSkillById(long id){
        return skillRepository.findById(id);
    }
    public List<Skill>getAllSkill(){
        return skillRepository.findAll();
    }
    public Skill updateSkill(Skill skill){
        Optional<Skill>getSkillById=getSkillById(skill.getId());
        if (getSkillById.isEmpty()){
            throw new RuntimeException("Skill không tồn tại với ID: " + skill.getId());
        }
        Skill updateSkill=getSkillById.get();
        updateSkill.setName(skill.getName());
        return skillRepository.save(updateSkill);
    }
    public void deleteSkillById(Long id){
        skillRepository.deleteById(id);
    }
}
