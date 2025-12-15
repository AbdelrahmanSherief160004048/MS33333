package com.example.ms3.dto;

public interface ManagerLeaveRequestDTO {
    Integer getRequestId();
    String getEmployeeName();
    String getLeaveType();
    Integer getDuration();
    String getJustification();
    String getStatus();
    Boolean getIsFlagged(); // Ensure this is present
}