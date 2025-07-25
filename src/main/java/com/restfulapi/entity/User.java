package com.restfulapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restfulapi.constant.GenderEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private String refreshToken;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Resume>resumes;

}


