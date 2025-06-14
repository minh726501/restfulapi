package com.restfulapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDTO {
    private String token;
    private UserLogin userLogin;
    @Setter
    @Getter
    @AllArgsConstructor
    public static class UserLogin{
        private long id;
        private String email;
        private String name;
    }
}
