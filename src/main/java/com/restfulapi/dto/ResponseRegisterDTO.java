package com.restfulapi.dto;

import com.restfulapi.constant.GenderEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseRegisterDTO {
    private String name;
    private String email;
    private int age;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;
}
