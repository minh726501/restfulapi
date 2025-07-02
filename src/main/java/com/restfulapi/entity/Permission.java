package com.restfulapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @NotBlank(message = "API Path not null")
    private String apiPath;
    @NotBlank(message = "Method not null")
    private String method;
    @NotBlank(message = "Module Path not null")
    private String module;
    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private List<Role>roles;

}
