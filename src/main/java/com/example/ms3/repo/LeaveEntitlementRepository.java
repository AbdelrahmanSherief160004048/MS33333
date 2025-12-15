package com.example.ms3.repo;

import com.example.ms3.table.LeaveEntitlement;
import com.example.ms3.table.LeaveEntitlementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveEntitlementRepository extends JpaRepository<LeaveEntitlement, LeaveEntitlementId> {
    // JpaRepository handles save (insert/update) automatically based on the composite ID
}