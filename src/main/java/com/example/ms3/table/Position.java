package com.example.ms3.table;

import jakarta.persistence.*;

@Entity
@Table(name = "Position")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Integer positionId;

    @Column(name = "position_title")
    private String positionTitle;

    @Column(name = "status")
    private String status;

    public Position() {}
    public Position(String positionTitle, String status) {
        this.positionTitle = positionTitle;
        this.status = status;
    }

    public Integer getPositionId() { return positionId; }
    public String getPositionTitle() { return positionTitle; }
}