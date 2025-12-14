package com.example.ms3.repo;

import com.example.ms3.table.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Integer> {
    // Standard CRUD to fetch all Leave Types
}