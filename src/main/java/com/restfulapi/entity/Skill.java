package com.restfulapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "skills")
@Setter
@Getter
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Skill name not null")
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "skills")
    private List<Job> jobs;
}
