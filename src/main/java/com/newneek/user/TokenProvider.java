package com.newneek.user;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String SECURITY_KEY = "ThisIsA32ByteLengthSecurityKey!!";

    // JWT 생성 메서드
    public String createJwt(String userId, String email, int duration) {
        try {
            Instant now = Instant.now();
            Instant exprTime = now.plusSeconds(duration);

            // email과 user_id를 클레임으로 추가
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userId) // 기본적으로 subject에는 userId를 저장
                    .claim("email", email) // 추가 클레임으로 email 저장
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(exprTime))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );

            JWSSigner signer = new MACSigner(SECURITY_KEY.getBytes());
            signedJWT.sign(signer);

            String jwt = signedJWT.serialize();
            System.out.println("생성된 JWT: " + jwt);
            return jwt;

        } catch (JOSEException e) {
            logger.error("JWT 생성 중 오류 발생", e);
            return null;
        }
    }

    // JWT 검증 및 subject 추출
    public String validateJwt(String token) {
        try {
            System.out.println("검증할 토큰: " + token);
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECURITY_KEY.getBytes());

            if (signedJWT.verify(verifier)) {
                return signedJWT.getJWTClaimsSet().getSubject();
            } else {
                logger.warn("JWT 서명이 유효하지 않습니다.");
                return null;
            }

        } catch (Exception e) {
            logger.error("JWT 검증 중 오류 발생", e);
            return null;
        }
    }

    // 토큰에서 user_id(subject)를 추출하는 메서드 추가
    public String getUserIdFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECURITY_KEY.getBytes());

            if (signedJWT.verify(verifier)) {
                return signedJWT.getJWTClaimsSet().getSubject();
            } else {
                logger.warn("JWT 서명이 유효하지 않습니다.");
                return null;
            }
        } catch (Exception e) {
            logger.error("JWT 토큰 검증 중 오류 발생", e);
            return null;
        }
    }
}
