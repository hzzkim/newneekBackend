package com.newneek.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    } //signup
    
    public ResponseDTO<LoginResponseDTO> login(LoginDTO loginDto){
    	String email=loginDto.getEmail();
    	String pw=loginDto.getPw();
    	
    	try {
    		//사용자 id/pw 일치하는지 확인
    		boolean existed=userRepository.existsByEmailAndPw(email, pw);
    		if(!existed) {
    			return ResponseDTO.setFailed("입력한 로그인 정보 존재하지 않습니다");
    		}
    	}catch (Exception e) {
			return ResponseDTO.setFailed("데이터베이스 연결 실패");
		}
    	
    	User user=null;
    	try {
			//값이 존재하는 경우 사용자 정보 불러옴(기준 email)
    		user = userRepository.findById(email).get();
			
		}catch (Exception e) {
			return ResponseDTO.setFailed("여기가 문제다");
		}
    	
    	user.setPw("");
    	
    	TokenProvider tokenProvider = new TokenProvider();
    	int exprTime=3600;
    	String token=tokenProvider.createJwt(email,exprTime);
    	
    	if(token == null) {
    		return ResponseDTO.setFailed("토큰 생성 실패");
    	}

    	
    	LoginResponseDTO loginResponseDto = new LoginResponseDTO(token,exprTime,user);
    	
    	return ResponseDTO.setSuccessData("로그인 성공", loginResponseDto);
    }//login
}
