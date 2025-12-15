package com.example.ms3.table;

import java.io.Serializable;
import java.util.Objects;

public class LeaveEntitlementId implements Serializable {
    private Integer employeeId;
    private Integer leaveTypeId;

    public LeaveEntitlementId() {}

    public LeaveEntitlementId(Integer employeeId, Integer leaveTypeId) {
        this.employeeId = employeeId;
        this.leaveTypeId = leaveTypeId;
    }

    // hashCode and equals are required for Composite Keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaveEntitlementId that = (LeaveEntitlementId) o;
        return Objects.equals(employeeId, that.employeeId) &&
                Objects.equals(leaveTypeId, that.leaveTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, leaveTypeId);
    }
}