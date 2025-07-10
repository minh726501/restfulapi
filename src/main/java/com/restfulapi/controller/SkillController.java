package com.restfulapi.controller;

import com.restfulapi.entity.Skill;
import com.restfulapi.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    public ResponseEntity<Skill> createSkill(@RequestBody @Valid Skill skill){
        if (skillService.existsSkillName(skill.getName())){
            throw new RuntimeException("Skill đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.saveSkill(skill));
    }
    @GetMapping("/skills/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable long id){
        Optional<Skill>getSkill=skillService.getSkillById(id);
        if (getSkill.isEmpty()){
            throw new RuntimeException("Skill không tồn tại với ID: " + id);
        }
        return ResponseEntity.ok(getSkill.get());
    }
    @GetMapping("/skills")
    public ResponseEntity<List<Skill>> getAllSkill(@RequestParam(value = "page",required = false) int page,@RequestParam(value = "size",required = false)int size){
        Pageable pageable= PageRequest.of(page-1,size);
        return ResponseEntity.ok(skillService.getAllSkill(pageable));
    }
    @PutMapping("/skills")
    public ResponseEntity<Skill>updateSkill(@RequestBody Skill skill){
        return ResponseEntity.ok(skillService.updateSkill(skill));
    }
    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void>deleteSkillById(@PathVariable long id){
        Optional<Skill> existsSkill=skillService.getSkillById(id);
        if (existsSkill.isEmpty()){
            throw new RuntimeException("Skill không tồn tại với ID: " + id);
        }
        skillService.deleteSkillById(existsSkill.get().getId());
        return ResponseEntity.noContent().build();
    }
}
