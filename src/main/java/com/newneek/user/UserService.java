package com.newneek.user;

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
        // System.out.println("암호화된 비밀번호 길이: " + hashedPassword.length());

        // UserRepository를 이용하여 DB에 사용자 정보 저장
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseDTO.setFailed("데이터베이스 연결 실패.");
        }

        return ResponseDTO.setSuccess("회원 생성에 성공했습니다.");
    }//signup

    // 로그인 로직
    public String login(LoginDTO loginDto) {
        String email = loginDto.getEmail();
        String pw = loginDto.getPw();
        User user;

        try {
            // 이메일로 사용자 조회 및 비밀번호 검증
            user = userRepository.findByEmail(email).orElse(null);
            if (user == null || !passwordEncoder.matches(pw, user.getPw())) {
                return null;  // 인증 실패 시 null 반환
            }

            // 토큰 생성 후 반환
            int exprTime = 3600; // 만료 시간 설정
            return tokenProvider.createJwt(user.getUserId(),email, exprTime);

        } catch (Exception e) {
            e.printStackTrace();
            return null;  // 오류 발생 시 null 반환
        }
    }//login

    public User getUserByEmail(String email) {
    	return userRepository.findByEmail(email).orElse(null);
    }

}
