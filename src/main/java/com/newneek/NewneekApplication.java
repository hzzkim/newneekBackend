package com.newneek;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class NewneekApplication {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(NewneekApplication.class, args);
    }

    @PostConstruct
    public void checkDatabaseConnection() {
        try {
            // 데이터베이스 연결 테스트
            if (dataSource.getConnection().isValid(0)) {
                System.out.println("----데이터베이스 연결 성공: " + dataSource.getConnection().getMetaData().getURL());
            } else {
                System.err.println("----데이터베이스 연결 실패: 연결이 유효하지 않습니다.");
            }
        } catch (Exception e) {
            System.err.println("----데이터베이스 연결 실패: " + e.getMessage());
        }
    }
    
    
}
