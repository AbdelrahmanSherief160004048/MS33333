package com.example.ms3.table;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "ShiftSchedule") // Matches your existing SQL Schema
public class ShiftSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id") // Matches schema: shift_id
    private Integer shiftId;

    @Column(name = "name") // Matches schema: name
    private String name;

    @Column(name = "start_time") // Matches schema: start_time
    private LocalTime startTime;

    @Column(name = "end_time") // Matches schema: end_time
    private LocalTime endTime;

    // Optional: Set default values for other schema columns not used in this form
    @Column(name = "type")
    private String type = "Standard";

    @Column(name = "status")
    private String status = "Active";

    public ShiftSchedule() {}

    public ShiftSchedule(String name, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public Integer getShiftId() { return shiftId; }
    public void setShiftId(Integer shiftId) { this.shiftId = shiftId; }

    // We map 'typeName' from the frontend to 'name' in the database
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}