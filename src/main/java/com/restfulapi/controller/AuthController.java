package com.restfulapi.controller;

import com.restfulapi.dto.JwtResponseDTO;
import com.restfulapi.dto.LoginDTO;

import com.restfulapi.entity.User;
import com.restfulapi.service.UserService;
import com.restfulapi.util.JwtFilter;
import com.restfulapi.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    public AuthController(AuthenticationManager authenticationManager,JwtUtil jwtUtil,UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil=jwtUtil;
        this.userService=userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO){
        // Nạp input username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword());
        //Xác thực người dùng => Viết hàm loadUserByUsername
        Authentication authentication=authenticationManager.authenticate(authenticationToken);
        // Tạo JWT
        String token = jwtUtil.generateToken(loginDTO.getUsername());

        // Lấy thông tin user
        User user = userService.findByUsername(loginDTO.getUsername());

        // Gói vào response DTO
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setToken(token);
        jwtResponseDTO.setUserLogin(new JwtResponseDTO.UserLogin(user.getId(), user.getEmail(), user.getName()));

        return ResponseEntity.ok(jwtResponseDTO);
    }
}
