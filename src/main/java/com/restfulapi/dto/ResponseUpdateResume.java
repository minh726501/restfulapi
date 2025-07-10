package com.restfulapi.dto;

import com.restfulapi.constant.StatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUpdateResume {
    private long id;
    private StatusEnum status;
}
