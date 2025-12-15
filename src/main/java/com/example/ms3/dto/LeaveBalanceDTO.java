package com.example.ms3.dto;

public interface LeaveBalanceDTO {
    String getLeaveType();
    Integer getTotalEntitlement();
    Integer getUsedDays();
    Integer getRemainingDays();
}