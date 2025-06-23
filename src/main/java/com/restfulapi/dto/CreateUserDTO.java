package com.restfulapi.dto;

import com.restfulapi.constant.GenderEnum;
import com.restfulapi.entity.Company;
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
    private GenderEnum gender;
    private Instant createdAt;
}
