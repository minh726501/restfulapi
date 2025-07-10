package com.restfulapi.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.restfulapi.dto.JwtResponseDTO;
import com.restfulapi.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-token}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh-token}")
    private long refreshTokenExpiration;
    public String createAccessToken(User user) {
        try {

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + accessTokenExpiration))
                    .claim("id",user.getId())
                    .claim("email",user.getEmail())
                    .claim("name",user.getName())
                    .claim("role",user.getRole().getName())
                    .build();
            Payload payload = new Payload(claimsSet.toJSONObject());
            JWSObject object = new JWSObject(header, payload);
            MACSigner signer = new MACSigner(secret.getBytes(StandardCharsets.UTF_8));
            object.sign(signer);
            return object.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Không thể tạo JWT", e);
        }
    }
    public String createRefreshToken(User user ){
        try {

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                    .build();
            Payload payload = new Payload(claimsSet.toJSONObject());
            JWSObject object = new JWSObject(header, payload);
            MACSigner signer = new MACSigner(secret.getBytes(StandardCharsets.UTF_8));
            object.sign(signer);
            return object.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Không thể tạo JWT", e);
        }
    }
    public boolean validateRefreshToken(String token) {
        try {
            // Parse token
            JWSObject jwsObject = JWSObject.parse(token);

            // Verify signature
            MACVerifier verifier = new MACVerifier(secret.getBytes(StandardCharsets.UTF_8));
            if (!jwsObject.verify(verifier)) {
                return false;
            }

            // Get claims
            JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());

            // Check expiration
            Date expirationTime = claimsSet.getExpirationTime();
            return expirationTime != null && expirationTime.after(new Date());

        } catch (Exception e) {
            return false; // Token không hợp lệ (lỗi ký hoặc parse)
        }
    }
    // Nếu cần: lấy claims sau khi xác thực
    public JWTClaimsSet getClaims(String token) throws ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        return JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
    }

}
