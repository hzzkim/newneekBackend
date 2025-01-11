package com.newneek.user;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetToken;


    // 생성자 주입
    @Autowired
    public AuthController(UserService userService, TokenProvider tokenProvider, UserRepository userRepository, FileService fileService, JavaMailSender mailSender, PasswordEncoder passwordEncoder, PasswordResetTokenRepository passwordResetToken) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetToken = passwordResetToken;
    }

    @PostMapping("/Signupform")
    public ResponseDTO<?> signup(@RequestBody UserDTO requestBody) {
        ResponseDTO<?> result = userService.signup(requestBody);
        System.out.println(requestBody);
        System.out.println(result);
        return result;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO requestBody) {
        String token = userService.login(requestBody);
        if (token == null) {
            return ResponseEntity.status(401).body("로그인 실패: 이메일 또는 비밀번호를 확인해 주세요.");
        }
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
    



    @PostMapping("/forgotPassword")
    public ResponseEntity<?> sendResetLink(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");

        // 사용자 조회
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("등록되지 않은 이메일입니다.");
        }

        User user = userOptional.get();

        // 토큰 생성 및 저장
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(1)) // 1시간 후 만료
                .build();

        passwordResetToken.save(resetToken);

        // 이메일 발송
        String resetLink = "http://localhost:3000/restart?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("비밀번호 재설정 링크");
        message.setText("다음 링크를 클릭하여 비밀번호를 재설정하세요: " + resetLink);

        mailSender.send(message);

        return ResponseEntity.ok("비밀번호 재설정 링크가 이메일로 전송되었습니다.");
    }


    @PostMapping("/passwordRestart")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        String newPassword = requestBody.get("newPassword");

        // 토큰 검증
        Optional<PasswordResetToken> resetTokenOptional =passwordResetToken.findByToken(token);
        if (resetTokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 토큰입니다.");
        }

        PasswordResetToken resetToken = resetTokenOptional.get();
        if (resetToken.isExpired()) { // 토큰 만료 검사
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 만료되었습니다.");
        }

        // 사용자 정보 업데이트
        User user = resetToken.getUser();
        user.setPw(passwordEncoder.encode(newPassword)); // 비밀번호 암호화
        userRepository.save(user);

        // 토큰 삭제
        passwordResetToken.delete(resetToken);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        System.out.println("받은 Authorization 헤더: " + token);
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization 헤더가 비어있거나 올바르지 않습니다.");
        }
        String actualToken = token.replace("Bearer ", "");
        String userIdOrEmail = tokenProvider.validateJwt(actualToken);
        if (userIdOrEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }
        Optional<User> userOptional = userRepository.findById(userIdOrEmail);
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByEmail(userIdOrEmail);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }
        }
        User user = userOptional.get();
        user.setPw(null);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/profileEdit", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateUserProfile(
            @RequestParam("user_id") String userId,
            @RequestParam("nickname") String nickname,
            @RequestParam("about") String about,
            @RequestParam("gender") String gender,
            @RequestParam("birthyear") Integer birthyear,
            @RequestParam(value = "profile", required = false) MultipartFile profileImage,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String extractedUserId = tokenProvider.getUserIdFromToken(token);
        if (!userId.equals(extractedUserId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자 ID입니다.");
        }
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
        User user = userOptional.get();
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String savedFileName = fileService.saveProfileImage(profileImage);
                user.setProfile(savedFileName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 저장 실패");
            }
        }
        user.setNickname(nickname);
        user.setAbout(about);
        user.setGender(gender);
        user.setBirthyear(birthyear);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable("user_id")  String user_id) {
        Optional<User> userOptional = userRepository.findById(user_id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = userOptional.get();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User oAuth2User) {
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        return "Welcome " + name + " (" + email + ")";
    }
}
