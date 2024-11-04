package com.newneek.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    // 생성자 주입
    @Autowired
    public AuthController(UserService userService, TokenProvider tokenProvider, UserRepository userRepository) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/Signupform")
    public ResponseDTO<?> signup(@RequestBody UserDTO requestBody) {
        ResponseDTO<?> result = userService.signup(requestBody);  // 회원가입 서비스 호출
        System.out.println(requestBody);
        System.out.println(result);
        return result;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO requestBody) {
        // userService의 로그인 메서드를 호출하여 토큰을 생성
        String token = userService.login(requestBody);
        
        // 토큰이 null이면 인증 실패
        if (token == null) {
            return ResponseEntity.status(401).body("로그인 실패: 이메일 또는 비밀번호를 확인해 주세요.");
        }

        // 토큰을 JSON 형태로 응답
        Map<String, String> response = new HashMap<>();
        response.put("token", token); // "token" 키로 JWT 반환

        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        System.out.println("받은 Authorization 헤더: " + token);  // 디버깅용 로그

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body("Authorization 헤더가 올바르지 않습니다.");
        }

        String actualToken = token.replace("Bearer ", "");
        System.out.println("실제 토큰 값: " + actualToken);  // 디버깅용 로그

        String email = tokenProvider.validateJwt(actualToken);
        if (email == null) {
            System.out.println("토큰 검증 실패 또는 토큰 만료");  // 디버깅용 로그
            return ResponseEntity.status(401).body("Invalid Token");
        }

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        user.setPw(null);
        return ResponseEntity.ok(user);
    } //me

}
