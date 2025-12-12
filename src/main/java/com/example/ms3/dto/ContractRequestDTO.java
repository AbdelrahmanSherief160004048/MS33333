package com.example.ms3.dto;

import java.time.LocalDate;

public class ContractRequestDTO {

    // This field is required for Renewals/Updates
    private Integer contractId;

    private Integer employeeId;
    private String position;
    private Double salary;
    private LocalDate startDate;
    private LocalDate endDate;
    private String contractType;

    // --- GETTERS ---
    public Integer getContractId() { return contractId; }
    public Integer getEmployeeId() { return employeeId; }
    public String getPosition() { return position; }
    public Double getSalary() { return salary; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getContractType() { return contractType; }

    // --- SETTERS ---
    public void setContractId(Integer contractId) { this.contractId = contractId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    public void setPosition(String position) { this.position = position; }
    public void setSalary(Double salary) { this.salary = salary; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setContractType(String contractType) { this.contractType = contractType; }
}