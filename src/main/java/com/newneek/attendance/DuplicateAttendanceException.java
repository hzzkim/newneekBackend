package com.newneek.attendance;

public class DuplicateAttendanceException extends RuntimeException {
    public DuplicateAttendanceException() {
        super("Attendance record already exists for the specified date.");
    }
}
