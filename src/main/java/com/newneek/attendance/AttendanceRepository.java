package com.newneek.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByUserIdAndAttendanceDate(String userId, LocalDate attendanceDate);
    List<Attendance> findByUserId(String userId);
}