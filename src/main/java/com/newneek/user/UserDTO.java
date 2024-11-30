package com.newneek.user;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	
	private String user_id;
	private String email;
	private String pw;
	private String nickname;
	private String about;
	private String gender; // 'M', 'F', 'UN'
	private Integer birthyear;
	private String profile; // 데이터베이스에 저장될 경로
	private transient MultipartFile profileFile; // 요청으로 받는 파일
	
	public String getGender() {
        return gender != null ? gender : "UN"; // 기본값 'UN' 처리
    }
}