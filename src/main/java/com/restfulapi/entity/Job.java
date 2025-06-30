package com.restfulapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restfulapi.constant.LevelEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jobs")
@Setter
@Getter
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String location;
    private String salary;
    private int quantity;
    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToMany
    @JoinTable(
            name = "job_skill", // tên bảng trung gian
            joinColumns = @JoinColumn(name = "job_id"), // khóa ngoại đến Job
            inverseJoinColumns = @JoinColumn(name = "skill_id") // khóa ngoại đến Skill
    )

    private List<Skill> skills;

}
