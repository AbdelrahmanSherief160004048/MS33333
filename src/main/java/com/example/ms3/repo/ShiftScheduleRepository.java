package com.example.ms3.repo;

import com.example.ms3.table.ShiftSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftScheduleRepository extends JpaRepository<ShiftSchedule, Integer> {
}