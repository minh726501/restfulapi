package com.restfulapi.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.MACVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Collections;

public class JwtFilter extends OncePerRequestFilter {

    private final String secret;

    public JwtFilter(String secret) {
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // Lấy token từ header Authorization (dạng: Bearer <token>)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                // Parse token
                JWSObject jwsObject = JWSObject.parse(token);
                // Verify token signature
                MACVerifier verifier = new MACVerifier(secret.getBytes(StandardCharsets.UTF_8));
                if (jwsObject.verify(verifier)) {
                    // Lấy username từ payload
                    String username = jwsObject.getPayload().toJSONObject().get("sub").toString();
                    // Tạo Authentication token (có thể add roles sau nếu cần)
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                    // Set vào SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ParseException | JOSEException e) {
                // Có lỗi parse hoặc verify token thì bỏ qua, không set authentication
                // Có thể log lỗi nếu muốn
            }
        }
        filterChain.doFilter(request, response);
    }
}
