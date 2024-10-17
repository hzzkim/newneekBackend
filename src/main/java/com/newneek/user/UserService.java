package com.newneek.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder; // BCryptPasswordEncoder를 필드로 사용

    public UserService(UserRepository userRepository, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = new BCryptPasswordEncoder();  // BCryptPasswordEncoder 인스턴스 생성
    }

    // 회원가입 로직
    public ResponseDTO<?> signup(UserDTO userDto) {
        String email = userDto.getEmail();
        String pw = userDto.getPw();

        // 이메일 중복 확인
        try {
            if (userRepository.existsById(email)) {
                return ResponseDTO.setFailed("중복된 Email 입니다.");
            }
        } catch (Exception e) {
        }

        // 비밀번호 유효성 검사 (예: 최소 8자)
        if (pw.length() < 8) {
            return ResponseDTO.setFailed("비밀번호는 8자 이상이어야 합니다.");
        }

        // 비밀번호 암호화
        String hashedPassword = passwordEncoder.encode(pw);

        // User 객체 생성
        User user = new User(userDto);
        user.setPw(hashedPassword);  // 암호화된 비밀번호 저장

        // UserRepository를 이용하여 DB에 사용자 정보 저장
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseDTO.setFailed("데이터베이스 연결 실패.");
        }

        return ResponseDTO.setSuccess("회원 생성에 성공했습니다.");
    }//signup

    // 로그인 로직
    public ResponseEntity<?> login(LoginDTO loginDto) {
        String email = loginDto.getEmail();
        String pw = loginDto.getPw();
        User user;

        // 이메일로 사용자 조회
        try {
            user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(ResponseDTO.setFailed("입력하신 이메일로 등록된 계정이 존재하지 않습니다."));
            }

            // 저장된 암호화된 비밀번호와 입력된 비밀번호 비교
            if (!passwordEncoder.matches(pw, user.getPw())) {
                return ResponseEntity.badRequest().body(ResponseDTO.setFailed("비밀번호가 일치하지 않습니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseDTO.setFailed("데이터베이스 연결에 실패하였습니다."));
        }

        // 비밀번호 숨기기
        user.setPw("");  // 클라이언트로 비밀번호를 제공하지 않음

        // 토큰 생성
        int exprTime = 3600;
        String token = tokenProvider.createJwt(email, exprTime);

        if (token == null) {
            return ResponseEntity.status(500).body(ResponseDTO.setFailed("토큰 생성 실패"));
        }

        // 토큰을 헤더에 넣어 반환
        LoginResponseDTO loginResponseDto = new LoginResponseDTO(token, exprTime, user);
        return ResponseEntity.ok()
            .header("Authorization", "Bearer " + token)  // 토큰을 Authorization 헤더에 추가
            .body(ResponseDTO.setSuccessData("로그인 성공", loginResponseDto));  // 응답 바디에 결과 반환
    }//login

}
