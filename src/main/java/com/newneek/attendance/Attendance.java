package com.newneek.attendance;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDate attendanceDate;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt = LocalDate.now();

    // 생성자 추가
    public Attendance(String userId, LocalDate attendanceDate) {
        this.userId = userId;
        this.attendanceDate = attendanceDate;
    }
}
