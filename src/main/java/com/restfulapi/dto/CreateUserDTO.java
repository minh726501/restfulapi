package com.restfulapi.dto;

import com.restfulapi.constant.GenderEnum;
import com.restfulapi.entity.Company;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CreateUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private String address;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private Instant createdAt;
    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

}
