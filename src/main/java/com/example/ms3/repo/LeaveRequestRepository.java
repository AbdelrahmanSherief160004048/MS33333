package com.example.ms3.repo;

import com.example.ms3.dto.LeaveBalanceDTO;
import com.example.ms3.dto.LeaveRequestDTO;
import com.example.ms3.dto.ManagerLeaveRequestDTO;
import com.example.ms3.table.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

    // 1. Employee: View History
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

    // 2. Employee: View Balance
    @Query(value = """
        SELECT 
            l.leave_type AS leaveType,
            le.entitlement AS totalEntitlement,
            ISNULL((SELECT SUM(lr.duration) 
                    FROM LeaveRequest lr 
                    WHERE lr.employee_id = le.employee_id 
                    AND lr.leave_id = l.leave_id 
                    AND lr.status = 'Approved'), 0) AS usedDays,
            (le.entitlement - ISNULL((SELECT SUM(lr.duration) 
                                      FROM LeaveRequest lr 
                                      WHERE lr.employee_id = le.employee_id 
                                      AND lr.leave_id = l.leave_id 
                                      AND lr.status = 'Approved'), 0)) AS remainingDays
        FROM LeaveEntitlement le
        JOIN Leave l ON le.leave_type_id = l.leave_id
        WHERE le.employee_id = :empId
    """, nativeQuery = true)
    List<LeaveBalanceDTO> findLeaveBalances(Integer empId);

    // 3. Manager: View Pending Requests from Team
    @Query(value = """
        SELECT 
            lr.request_id AS requestId,
            (e.first_name + ' ' + e.last_name) AS employeeName,
            l.leave_type AS leaveType,
            lr.duration AS duration,
            lr.justification AS justification,
            lr.status AS status,
            lr.is_flagged AS isFlagged
        FROM LeaveRequest lr
        JOIN Employee e ON lr.employee_id = e.employee_id
        JOIN Leave l ON lr.leave_id = l.leave_id
        WHERE e.manager_id = :managerId 
          AND lr.status = 'Pending'
    """, nativeQuery = true)
    List<ManagerLeaveRequestDTO> findPendingRequestsForManager(Integer managerId);

    // 4. Update Status (Used by Manager & HR Override)
    @Modifying
    @Transactional
    @Query(value = "UPDATE LeaveRequest SET status = :status WHERE request_id = :requestId", nativeQuery = true)
    void updateRequestStatus(Integer requestId, String status);

    // 5. Manager: Flag Request
    @Modifying
    @Transactional
    @Query(value = "UPDATE LeaveRequest SET is_flagged = 1 WHERE request_id = :requestId", nativeQuery = true)
    void flagRequest(Integer requestId);

    // 6. HR Admin: View ALL Requests (For Override)
    @Query(value = """
        SELECT TOP 50 
            lr.request_id AS requestId,
            (e.first_name + ' ' + e.last_name) AS employeeName,
            l.leave_type AS leaveType,
            lr.duration AS duration,
            lr.justification AS justification,
            lr.status AS status,
            lr.is_flagged AS isFlagged
        FROM LeaveRequest lr
        JOIN Employee e ON lr.employee_id = e.employee_id
        JOIN Leave l ON lr.leave_id = l.leave_id
        ORDER BY lr.request_id DESC
    """, nativeQuery = true)
    List<ManagerLeaveRequestDTO> findAllRequestsForHR();
}