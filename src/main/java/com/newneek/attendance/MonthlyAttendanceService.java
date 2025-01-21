package com.newneek.attendance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonthlyAttendanceService {

    @Autowired
    private MonthlyAttendanceRepository monthlyAttendanceRepository;

    // 특정 사용자의 특정 연도의 월별 통계를 가져오는 메서드
    public List<MonthlyAttendance> getMonthlyStatistics(String userId) {
        return monthlyAttendanceRepository.findAllByUserId(userId);
    }
}
