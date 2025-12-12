package com.example.ms3.table;

import jakarta.persistence.*;

@Entity
@Table(name = "SalaryType")
public class SalaryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_type_id")
    private Integer salaryTypeId;

    // --- REVERTED BACK TO 'type' ---
    // If you get errors, try changing this to @Column(name = "[type]") to escape it
    @Column(name = "type")
    private String type;

    @Column(name = "currency")
    private String currency;

    public SalaryType() {}

    public SalaryType(String type, String currency) {
        this.type = type;
        this.currency = currency;
    }

    public Integer getSalaryTypeId() { return salaryTypeId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}