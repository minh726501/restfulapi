package com.restfulapi.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;
    public String generateToken(String username) {
        try {

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expiration))
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

}
