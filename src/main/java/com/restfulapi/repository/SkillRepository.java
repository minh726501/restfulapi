package com.restfulapi.repository;

import com.restfulapi.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill,Long> {
    boolean existsByName(String name);
}
