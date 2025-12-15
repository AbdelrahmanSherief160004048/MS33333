package com.example.ms3.dto;

public interface LeaveRequestDTO {
    Integer getRequestId();
    String getLeaveType();
    Integer getDuration();
    String getStatus();
    String getJustification();
    String getFilePath();
}