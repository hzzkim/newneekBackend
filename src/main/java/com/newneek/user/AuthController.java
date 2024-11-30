package com.newneek.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final FileService fileService;

    // 생성자 주입
    @Autowired
    public AuthController(UserService userService, TokenProvider tokenProvider, UserRepository userRepository, FileService fileService) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.fileService = fileService;
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

        // 토큰 유효성 검사
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization 헤더가 비어있거나 올바르지 않습니다.");
        }

        String actualToken = token.replace("Bearer ", "");
        System.out.println("실제 토큰 값: " + actualToken);  // 디버깅용 로그

        // validateJwt 메서드를 호출해 user_id 또는 email 추출
        String userIdOrEmail = tokenProvider.validateJwt(actualToken);
        if (userIdOrEmail == null) {
            System.out.println("토큰 검증 실패 또는 토큰 만료. 토큰 값: " + actualToken);  // 디버깅용 로그
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        // user_id 또는 email로 사용자 정보 검색
        Optional<User> userOptional = userRepository.findById(userIdOrEmail); // user_id 기준
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByEmail(userIdOrEmail); // email 기준
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }
        }

        // 사용자 정보 가져오기
        User user = userOptional.get();

        // 비밀번호 필드 제거
        user.setPw(null);

        // 사용자 정보 반환
        System.out.println("사용자 정보 반환: " + user.toString());
        return ResponseEntity.ok(user);
    }//me

    
//    @GetMapping("/profile_edit")
//    public ResponseEntity<String> getProfileEditPage() {
//        // 클라이언트에 반환할 데이터를 처리
//        return ResponseEntity.ok("프로필 편집 페이지에 대한 응답입니다.");
//    }
    
    @PostMapping(value = "/profileEdit", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateUserProfile(
            @ModelAttribute UserDTO updateUser,
            @RequestParam(value = "profile", required = false) MultipartFile profileImage,
            HttpServletRequest request) {

        System.out.println("요청 수신: /profileEdit");

        // Authorization 헤더 확인
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
        }

        // 토큰 검증
        String userId = tokenProvider.getUserIdFromToken(token.substring(7));
        System.out.println("추출된 userId: " + userId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();

        // MultipartFile 처리
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String savedFileName = fileService.saveProfileImage(profileImage);
                user.setProfile(savedFileName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("프로필 이미지를 저장하는 데 실패했습니다.");
            }
        }

        // 사용자 정보 업데이트
        user.setNickname(updateUser.getNickname());
        user.setAbout(updateUser.getAbout());
        user.setGender(updateUser.getGender());
        user.setBirthyear(updateUser.getBirthyear());
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
    }//MyMy
    
    

}
