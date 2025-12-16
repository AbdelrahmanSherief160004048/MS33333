package com.example.ms3.dto;

import java.time.LocalTime;

public class ShiftTypeDTO {
    private String typeName;
    private LocalTime startTime;
    private LocalTime endTime;

    // Getters and Setters
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}