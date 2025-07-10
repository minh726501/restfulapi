package com.restfulapi.dto;

import com.restfulapi.constant.GenderEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {
    private long id;
    @NotBlank(message = "Name not null")
    private String name;
    @NotBlank(message = "Email not null")
    private String email;
    @NotBlank(message = "Password not null")
    private String password;
    private int age;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;

}
