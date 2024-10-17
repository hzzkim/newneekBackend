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
    private String user_id;

    @Column(length = 50,unique = true)
    private String email;

    @Column(length = 255)
    private String pw;

    @Column(length = 255)
    private String token;

    public User(UserDTO userDto) {
    	this.email=userDto.getEmail();
    	this.pw=userDto.getPw();
    	this.token=null;
    }
}
