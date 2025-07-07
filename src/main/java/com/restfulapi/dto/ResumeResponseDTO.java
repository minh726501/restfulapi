package com.restfulapi.dto;

import com.restfulapi.entity.Job;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
public class ResumeResponseDTO {
    private long id;
    private String email;
    private String status;
    private String url;
    private Instant createdAt;
    private String createdBy;
    private SimpleUserDTO user;
    private SimpleJobDTO job;
}
