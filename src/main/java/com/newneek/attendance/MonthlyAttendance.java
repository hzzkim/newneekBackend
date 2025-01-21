package com.newneek.attendance;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MonthlyAttendance {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(name = "attendance_count")
    private int attendanceCount = 0;

    // 기본 생성자 (JPA용)
    public MonthlyAttendance() {}

    // 사용자 정의 생성자
    public MonthlyAttendance(String userId, int year, int month) {
        this.userId = userId;
        this.year = year;
        this.month = month;
        this.attendanceCount = 0; // 기본값 설정
    }

    public void incrementAttendanceCount() {
        this.attendanceCount++;
    }
}

