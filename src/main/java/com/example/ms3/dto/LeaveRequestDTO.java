package com.example.ms3.dto;

public interface LeaveRequestDTO {
    Integer getRequestId();
    String getLeaveType();

    // Make sure this is getDuration(), NOT getStartDate()/getEndDate()
    Integer getDuration();

    String getStatus();
    String getJustification();
    String getFilePath();
}