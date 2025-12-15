package com.example.ms3.table;

import jakarta.persistence.*;

@Entity
@Table(name = "LeaveEntitlement")
@IdClass(LeaveEntitlementId.class) // Links to the ID class above
public class LeaveEntitlement {

    @Id
    @Column(name = "employee_id")
    private Integer employeeId;

    @Id
    @Column(name = "leave_type_id")
    private Integer leaveTypeId;

    @Column(name = "entitlement")
    private Integer entitlement;

    // Constructors
    public LeaveEntitlement() {}

    public LeaveEntitlement(Integer employeeId, Integer leaveTypeId, Integer entitlement) {
        this.employeeId = employeeId;
        this.leaveTypeId = leaveTypeId;
        this.entitlement = entitlement;
    }

    // Getters and Setters
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public Integer getLeaveTypeId() { return leaveTypeId; }
    public void setLeaveTypeId(Integer leaveTypeId) { this.leaveTypeId = leaveTypeId; }

    public Integer getEntitlement() { return entitlement; }
    public void setEntitlement(Integer entitlement) { this.entitlement = entitlement; }
}