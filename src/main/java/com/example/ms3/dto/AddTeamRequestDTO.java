package com.example.ms3.dto;

public class AddTeamRequestDTO {
    private Integer managerId;
    private Integer employeeId;

    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
}