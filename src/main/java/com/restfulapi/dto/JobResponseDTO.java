package com.restfulapi.dto;

import com.restfulapi.constant.LevelEnum;
import com.restfulapi.entity.Skill;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
@Setter
@Getter
public class JobResponseDTO {
    private long id;
    private String location;
    private String salary;
    private int quantity;
    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private List<String> skillName;
}

