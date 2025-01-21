package com.newneek.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyAttendanceRepository extends JpaRepository<MonthlyAttendance, Long> {
    Optional<MonthlyAttendance> findByUserIdAndYearAndMonth(String userId, int year, int month);
    
 // 특정 사용자의 특정 연도의 모든 월별 데이터 가져오기
    List<MonthlyAttendance> findAllByUserId(String userId);
}
