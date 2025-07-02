package com.restfulapi.dto;

import com.restfulapi.entity.Role;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PermissionResponseDTO {
    private String name;
    private String apiPath;
    private String method;
    private String module;
    private List<Role> roles;
}
