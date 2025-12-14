package com.example.ms3.repo;

import com.example.ms3.table.LeaveDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveDocumentRepository extends JpaRepository<LeaveDocument, Integer> {
    // This interface allows us to save the file paths to the database
}