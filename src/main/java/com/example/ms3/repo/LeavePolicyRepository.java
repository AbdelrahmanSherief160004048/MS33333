package com.example.ms3.repo;

import com.example.ms3.table.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, Integer> {

    // Fetch only policies that have a special leave type defined
    @Query("SELECT l FROM LeavePolicy l WHERE l.specialLeaveType IS NOT NULL AND l.specialLeaveType <> ''")
    List<LeavePolicy> findSpecialPolicies();
}