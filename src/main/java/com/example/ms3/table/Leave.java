package com.example.ms3.table;

import jakarta.persistence.*;

@Entity
@Table(name = "Leave")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id") // Must match your SQL column
    private Integer leaveId;

    @Column(name = "leave_type")
    private String leaveType;

    @Column(name = "leave_description")
    private String leaveDescription;

    // Getters and Setters
    public Integer getLeaveId() { return leaveId; }
    public void setLeaveId(Integer leaveId) { this.leaveId = leaveId; }

    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    public String getLeaveDescription() { return leaveDescription; }
    public void setLeaveDescription(String leaveDescription) { this.leaveDescription = leaveDescription; }
}