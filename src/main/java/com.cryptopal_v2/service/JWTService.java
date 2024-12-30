package com.cryptopal_v2.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JWTService {
    private final SecretKey SECRET_KEY;

    public JWTService(){
        this.SECRET_KEY = generateHMACKey();
    }

    /**
     * Generates a secure HMAC key for signing JWT tokens.
     * @return A SecretKey instance for HMAC SHA-256.
     */
    private SecretKey generateHMACKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256); // 256-bit key
            return keyGen.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC key", e);
        }
    }
    /**
     * Generates a JWT for the given userId.
     * @param userId The ID of the user.
     * @return A signed JWT token.
     */
    public String generateToken(String userId) {
        return JWT.create()
                .withIssuer("CryptoPal")
                .withSubject(userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour expiration
                .sign(Algorithm.HMAC256(SECRET_KEY.getEncoded()));
    }

    /**
     * Validates a JWT and returns the decoded information.
     *
     * @param token The JWT token to validate.
     * @return DecodedJWT if valid.
     * @throws JWTVerificationException if the token is invalid.
     */
    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY.getEncoded()))
                .withIssuer("CryptoPal")
                .build();
        return verifier.verify(token);
    }







}
