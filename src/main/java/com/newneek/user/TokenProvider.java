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
	    public String createJwt(String email, int duration) {
	        try {
	            // 현재 시간 기준으로 만료 시간을 설정
	            Instant now = Instant.now();
	            Instant exprTime = now.plusSeconds(duration);

	            // JWT Claim 설정 (페이로드 설정)
	            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
	                    .subject(email)
	                    .issueTime(Date.from(now))
	                    .expirationTime(Date.from(exprTime))
	                    .build();

	            // JWT 서명 설정 (헤더와 페이로드 포함)
	            SignedJWT signedJWT = new SignedJWT(
	                    new JWSHeader(JWSAlgorithm.HS256),
	                    claimsSet
	            );

	            // HMAC 서명을 사용하여 JWT 서명
	            JWSSigner signer = new MACSigner(SECURITY_KEY.getBytes());
	            signedJWT.sign(signer);

	            // JWT 직렬화 및 반환
	            String jwt = signedJWT.serialize();
	            System.out.println("생성된 JWT: " + jwt);  // 생성된 JWT 확인용 로그
	            return jwt;
	            
	        } catch (JOSEException e) {
	            logger.error("JWT 생성 중 오류 발생", e);
	            return null;
	        }
	    }


	    public static String validateJwt(String token) {
	        try {
	            // 검증할 토큰을 로그에 출력하여 확인
	            System.out.println("검증할 토큰: " + token);
	            
	            // JWT 파싱 및 검증
	            SignedJWT signedJWT = SignedJWT.parse(token);
	            JWSVerifier verifier = new MACVerifier(SECURITY_KEY.getBytes());
	            
	            // 서명이 유효한지 확인
	            if (signedJWT.verify(verifier)) {
	                // 서명이 유효하면 주체(subject)를 반환
	                return signedJWT.getJWTClaimsSet().getSubject();
	            } else {
	                // 서명이 유효하지 않음
	                logger.warn("JWT 서명이 유효하지 않습니다.");
	                return null;
	            }
	            
	        } catch (Exception e) {
	            // 예외 발생 시 오류 로그 출력
	            logger.error("JWT 검증 중 오류 발생", e);
	            return null;
	        }
	    }

	    
}
