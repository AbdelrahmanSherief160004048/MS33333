package com.example.ms3.repo;
import com.example.ms3.table.AttendanceException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceExceptionRepository extends JpaRepository<AttendanceException, Integer> {}