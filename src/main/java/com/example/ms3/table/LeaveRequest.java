package com.example.ms3.table;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LeaveRequest")
public class LeaveRequest {

    @Id
    // No @GeneratedValue because your DB doesn't have Auto-Increment turned on
    @Column(name = "request_id")
    private Integer requestId;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "leave_id")
    private Integer leaveId;

    private String justification;

    // This is the ONLY time-related column your table has
    private Integer duration;

    private String status = "Pending";

    @Column(name = "approval_timing")
    private LocalDateTime approvalTiming;

    // --- GETTERS & SETTERS ---
    public Integer getRequestId() { return requestId; }
    public void setRequestId(Integer requestId) { this.requestId = requestId; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public Integer getLeaveId() { return leaveId; }
    public void setLeaveId(Integer leaveId) { this.leaveId = leaveId; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}