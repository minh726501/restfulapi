package com.restfulapi.controller;

import com.restfulapi.dto.LoginDTO;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody @Valid LoginDTO loginDTO){
        // Nạp input username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword());
        //Xác thực người dùng => Viết hàm loadUserByUsername
        Authentication authentication=authenticationManager.authenticate(authenticationToken);
        return ResponseEntity.ok().body(loginDTO);
    }
}
