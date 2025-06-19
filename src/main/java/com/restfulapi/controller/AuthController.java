package com.restfulapi.controller;

import com.restfulapi.annotation.ApiMessage;
import com.restfulapi.dto.ApiResponse;
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
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        // Nạp input username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        //Xác thực người dùng => Viết hàm loadUserByUsername
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // Lấy thông tin user
        User user = userService.findByUsername(loginDTO.getUsername());
        // Tạo JWT
        String createAccessToken = jwtUtil.createAccessToken(user);


        // Gói vào response DTO
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();

        jwtResponseDTO.setToken(createAccessToken);
        jwtResponseDTO.setUserLogin(new JwtResponseDTO.UserLogin(user.getId(), user.getEmail(), user.getName()));
        //create refresh token
        String createRefreshToken = jwtUtil.createRefreshToken(user);
        userService.updateToken(createRefreshToken, loginDTO.getUsername());
        //set cookies
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", createRefreshToken)
                .httpOnly(true) // Ngăn JS đọc token, tránh XSS
                .secure(true)   // Chỉ gửi qua HTTPS
                .path("/")      // Toàn bộ API đều có thể access cookie này
                .maxAge(7 * 24 * 60 * 60) // 7 ngày
                .sameSite("Strict") // Tránh gửi kèm ngoài domain
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(jwtResponseDTO);
    }

    @GetMapping("/refresh-token")
    @ApiMessage("Refresh Token")
    public ResponseEntity<JwtResponseDTO> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh token không hợp lệ hoặc đã hết hạn");
        }

        try {
            // Lấy username từ refresh token
            String username = jwtUtil.getClaims(refreshToken).getSubject();

            // Lấy user từ database
            User user = userService.findByUsername(username);

            // Tạo lại access token mới từ user
            String newAccessToken = jwtUtil.createAccessToken(user);

            // Trả về access token mới
            JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
            jwtResponseDTO.setToken(newAccessToken);
            return ResponseEntity.ok(jwtResponseDTO);

        } catch (ParseException e) {
            throw new RuntimeException("Token không hợp lệ", e);
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
            try {
                String username = jwtUtil.getClaims(refreshToken).getSubject();
                userService.updateToken(null, username); // Xóa refresh token trong DB
            } catch (Exception e) {
                // Nếu lỗi vẫn tiếp tục xóa cookie để client đăng xuất
            }
        }

        // Xóa cookie bằng cách tạo cookie rỗng với maxAge = 0
        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity
                .noContent() // trả về status 204 No Content
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }

}


