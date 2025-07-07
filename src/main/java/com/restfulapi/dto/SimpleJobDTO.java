package com.restfulapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleJobDTO {
    private long id;
    private String location;
    private String salary;
    private String level;
}
