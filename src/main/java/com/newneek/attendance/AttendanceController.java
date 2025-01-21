package com.newneek.attendance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/check")
    public ResponseEntity<String> checkAttendance(@RequestBody AttendanceRequest request) {
        try {
            attendanceService.checkAttendance(request.getUserId(), request.getDate());
            return ResponseEntity.ok("출석 체크가 완료되었습니다!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DuplicateAttendanceException e) {
            return ResponseEntity.badRequest().body("오늘 출석 체크를 이미 완료하였습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
    
    @GetMapping("/{userId}")
    public List<Attendance> getAttendance(@PathVariable("userId") String userId) {
        return attendanceService.getAttendanceByUserId(userId); // 해당 userId의 출석 데이터를 반환
    }
} 
