package com.restfulapi.dto;

import com.restfulapi.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
public class ResponseUpdateUserDTO {
    private long id;
    private String name;
    private int age;
    private String address;
    private GenderEnum gender;
    private Instant updatedAt;
}
