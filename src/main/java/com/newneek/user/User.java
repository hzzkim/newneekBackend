package com.newneek.user;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Data
@Table( name = "user")

public class User {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "user_id")
    private String userId;

    @Column(length = 50,unique = true)
    private String email;

    @Column(length = 255)
    private String pw;

    @Column(length = 255)
    private String token;

    @Column(length = 20)
    private String nickname;

    @Column(length = 500)
    private String about;

    @Column(columnDefinition = "ENUM('M', 'F', 'UN') DEFAULT 'UN'")
    private String gender; // ENUM 값: 'M', 'F', 'UN'

    @Column
    private Integer birthyear;

    @Column(name = "profile", length = 200, nullable = true)
    private String profile; // 프로필 이미지 파일 경로 저장




    public User(UserDTO userDto) {
    	this.email=userDto.getEmail();
    	this.pw=userDto.getPw();
    	this.token=null;
    	this.nickname =userDto.getNickname();
    	this.about = userDto.getAbout();
    	this.gender = userDto.getGender() != null ? userDto.getGender() : "UN";
        this.birthyear = userDto.getBirthyear();
        this.profile = userDto.getProfile();
    }
}
