package com.example.ms3.table;

import jakarta.persistence.*;

@Entity
@Table(name = "LeavePolicy")
public class LeavePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Integer policyId;

    @Column(name = "name")
    private String name;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "eligibility_rules")
    private String eligibilityRules;

    @Column(name = "notice_period")
    private Integer noticePeriod;

    @Column(name = "special_leave_type")
    private String specialLeaveType;

    @Column(name = "reset_on_new_year")
    private Boolean resetOnNewYear;

    // --- Getters and Setters ---
    public Integer getPolicyId() { return policyId; }
    public void setPolicyId(Integer policyId) { this.policyId = policyId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getEligibilityRules() { return eligibilityRules; }
    public void setEligibilityRules(String eligibilityRules) { this.eligibilityRules = eligibilityRules; }

    public Integer getNoticePeriod() { return noticePeriod; }
    public void setNoticePeriod(Integer noticePeriod) { this.noticePeriod = noticePeriod; }

    public String getSpecialLeaveType() { return specialLeaveType; }
    public void setSpecialLeaveType(String specialLeaveType) { this.specialLeaveType = specialLeaveType; }

    public Boolean getResetOnNewYear() { return resetOnNewYear; }
    public void setResetOnNewYear(Boolean resetOnNewYear) { this.resetOnNewYear = resetOnNewYear; }
}