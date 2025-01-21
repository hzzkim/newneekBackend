package com.newneek.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newneek.user.UserRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private MonthlyAttendanceRepository monthlyAttendanceRepository;

    public void checkAttendance(String userId, LocalDate date) {
        // 1. 사용자 존재 여부 확인 (userId로 확인)
        if (!userRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("Invalid userId.");
        }

        // 2. 출석 중복 여부 확인
        if (attendanceRepository.existsByUserIdAndAttendanceDate(userId, date)) {
            throw new DuplicateAttendanceException();
        }

        // 3. Attendance 테이블에 출석 기록 추가
        Attendance attendance = new Attendance(userId, date);
        attendanceRepository.save(attendance);

        // 4. MonthlyAttendance 테이블 업데이트
        YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonthValue());
        MonthlyAttendance monthlyAttendance = monthlyAttendanceRepository.findByUserIdAndYearAndMonth(
                userId, yearMonth.getYear(), yearMonth.getMonthValue()
        ).orElseGet(() -> new MonthlyAttendance(userId, yearMonth.getYear(), yearMonth.getMonthValue()));

        monthlyAttendance.incrementAttendanceCount();
        monthlyAttendanceRepository.save(monthlyAttendance);
    }
    
    public List<Attendance> getAttendanceByUserId(String userId) {
        // 특정 사용자의 출석 데이터 가져오기
        return attendanceRepository.findByUserId(userId);
    }
}
