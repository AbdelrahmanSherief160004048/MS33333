package com.example.ms3.table;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Integer attendanceId;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "entry_time")
    private LocalDateTime entryTime;

    @Column(name = "login_method")
    private String loginMethod;

    @Column(name = "exception_id")
    private Integer exceptionId;

    // Constructors
    public Attendance() {}

    public Attendance(Integer employeeId, LocalDateTime entryTime, String loginMethod, Integer exceptionId) {
        this.employeeId = employeeId;
        this.entryTime = entryTime;
        this.loginMethod = loginMethod;
        this.exceptionId = exceptionId;
    }

    // Getters and Setters
    public Integer getAttendanceId() { return attendanceId; }
    public void setAttendanceId(Integer attendanceId) { this.attendanceId = attendanceId; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }

    public String getLoginMethod() { return loginMethod; }
    public void setLoginMethod(String loginMethod) { this.loginMethod = loginMethod; }

    public Integer getExceptionId() { return exceptionId; }
    public void setExceptionId(Integer exceptionId) { this.exceptionId = exceptionId; }
}