package com.example.ms3.repo;

import com.example.ms3.dto.LeaveRequestDTO;
import com.example.ms3.table.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

    @Query("SELECT COALESCE(MAX(l.requestId), 0) FROM LeaveRequest l")
    Integer findMaxRequestId();

    // ensure 'lr.duration as duration' is present
    @Query(value = "SELECT lr.request_id as requestId, " +
            "l.leave_type as leaveType, " +
            "lr.duration as duration, " +
            "lr.status as status, " +
            "lr.justification as justification, " +
            "ld.file_path as filePath " +
            "FROM LeaveRequest lr " +
            "JOIN Leave l ON lr.leave_id = l.leave_id " +
            "LEFT JOIN LeaveDocument ld ON lr.request_id = ld.leave_request_id " +
            "WHERE lr.employee_id = :empId " +
            "ORDER BY lr.request_id DESC", nativeQuery = true)
    List<LeaveRequestDTO> findRequestsByEmployee(Integer empId);
}