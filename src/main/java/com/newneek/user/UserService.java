package com.newneek.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newneek.user.DTO.UserDTO;
import com.newneek.user.DTO.ResponseDTO;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseDTO<?> signup(UserDTO userDto) {
        String email = userDto.getEmail();
        String pw = userDto.getPw();

//        // 이메일이 이미 존재하는지 확인
//        if (memberRepository.findByEmail(email).isPresent()) {
//            return ResponseDTO.setFailed("이미 존재하는 이메일입니다."); // 실패 시 메시지 반환
//        }
        

        // 비밀번호 유효성 검사 (예: 최소 8자)
        if (pw.length() < 8) {
            return ResponseDTO.setFailed("비밀번호는 8자 이상이어야 합니다."); // 실패 시 메시지 반환
        }

        // 새로운 회원 생성
        try {
            User newUser = User.builder()
                .email(email)
                .pw(pw)
                .build();
            userRepository.save(newUser);
            return ResponseDTO.setSuccess("회원 생성 성공"); // 성공 시 메시지 반환
        } catch (Exception e) {
            // 예외 발생 시 실패 응답
            return ResponseDTO.setFailed("회원 생성 중 오류가 발생했습니다."); // 예외 처리
        }
    }
}
