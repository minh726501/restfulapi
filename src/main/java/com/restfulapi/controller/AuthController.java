package com.restfulapi.controller;

import com.restfulapi.dto.JwtResponseDTO;
import com.restfulapi.dto.LoginDTO;

import com.restfulapi.entity.User;
import com.restfulapi.service.UserService;
import com.restfulapi.util.JwtFilter;
import com.restfulapi.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
        String createAccessToken = jwtUtil.createAccessToken(loginDTO.getUsername());

        // Lấy thông tin user
        User user = userService.findByUsername(loginDTO.getUsername());

        // Gói vào response DTO
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setToken(createAccessToken);
        jwtResponseDTO.setUserLogin(new JwtResponseDTO.UserLogin(user.getId(), user.getEmail(), user.getName()));
        //create refresh token
        String createRefreshToken= jwtUtil.createRefreshToken(loginDTO.getUsername());
        userService.updateToken(createRefreshToken, loginDTO.getUsername());
        //set cookies
        ResponseCookie responseCookie=ResponseCookie.from("refresh_token",createRefreshToken)
                .httpOnly(true) // Ngăn JS đọc token, tránh XSS
                .secure(true)   // Chỉ gửi qua HTTPS
                .path("/")      // Toàn bộ API đều có thể access cookie này
                .maxAge(7 * 24 * 60 * 60) // 7 ngày
                .sameSite("Strict") // Tránh gửi kèm ngoài domain
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE,responseCookie.toString())
                .body(jwtResponseDTO);
    }
}
