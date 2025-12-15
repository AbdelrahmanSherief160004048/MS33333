package com.example.ms3.table;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Exception") // Maps to your SQL 'Exception' table
public class AttendanceException {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exception_id")
    private Integer exceptionId;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "status")
    private String status;

    // Constructors
    public AttendanceException() {}

    public AttendanceException(String name, String category, LocalDate date, String status) {
        this.name = name;
        this.category = category;
        this.date = date;
        this.status = status;
    }

    // Getters and Setters
    public Integer getExceptionId() { return exceptionId; }
    public void setExceptionId(Integer exceptionId) { this.exceptionId = exceptionId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}