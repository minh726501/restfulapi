package com.restfulapi.dto;

import com.restfulapi.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDTO {

    private UserLogin userLogin;
    @Setter
    @Getter
    @AllArgsConstructor
    public static class UserLogin{
        private long id;
        private String email;
        private String name;
        private Role role;
    }

    private String token;
}
