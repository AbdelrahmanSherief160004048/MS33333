package com.example.ms3.table;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LeaveDocument")
public class LeaveDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Integer documentId;

    @Column(name = "leave_request_id")
    private Integer leaveRequestId;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    // Constructors, Getters, Setters
    public LeaveDocument() {}
    public LeaveDocument(Integer leaveRequestId, String filePath) {
        this.leaveRequestId = leaveRequestId;
        this.filePath = filePath;
        this.uploadedAt = LocalDateTime.now();
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public Integer getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(Integer leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}