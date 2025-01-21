package com.newneek.attendance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
public class MonthlyAttendanceController {

    @Autowired
    private MonthlyAttendanceService monthlyAttendanceService;

    @GetMapping("/report/{userId}")
    public ResponseEntity<List<MonthlyAttendance>> getMonthlyStatistics(
        @PathVariable("userId") String userId) {
        List<MonthlyAttendance> statistics = monthlyAttendanceService.getMonthlyStatistics(userId);
        return ResponseEntity.ok(statistics);
    }
}
