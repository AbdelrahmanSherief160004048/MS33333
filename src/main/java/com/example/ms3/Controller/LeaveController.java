package com.example.ms3.Controller;

import com.example.ms3.repo.*;
import com.example.ms3.table.*;
import com.example.ms3.table.AttendanceException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveRepository leaveRepo;
    private final LeaveRequestRepository requestRepo;
    private final LeaveDocumentRepository documentRepo;
    private final LeavePolicyRepository policyRepo;
    private final LeaveEntitlementRepository entitlementRepo;

    // NEW REPOS FOR SYNC
    private final AttendanceRepository attendanceRepo;
    private final AttendanceExceptionRepository exceptionRepo;

    // Directory where uploaded files are stored
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public LeaveController(LeaveRepository leaveRepo,
                           LeaveRequestRepository requestRepo,
                           LeaveDocumentRepository documentRepo,
                           LeavePolicyRepository policyRepo,
                           LeaveEntitlementRepository entitlementRepo,
                           AttendanceRepository attendanceRepo,
                           AttendanceExceptionRepository exceptionRepo) {
        this.leaveRepo = leaveRepo;
        this.requestRepo = requestRepo;
        this.documentRepo = documentRepo;
        this.policyRepo = policyRepo;
        this.entitlementRepo = entitlementRepo;
        this.attendanceRepo = attendanceRepo;
        this.exceptionRepo = exceptionRepo;
    }

    // =========================================================
    // 1. GENERAL & HR CONFIGURATION
    // =========================================================

    @GetMapping("/types")
    public ResponseEntity<?> getLeaveTypes() {
        return ResponseEntity.ok(leaveRepo.findAll());
    }

    @PostMapping("/types/save")
    public ResponseEntity<?> saveLeaveType(@RequestBody Leave leave) {
        try {
            leaveRepo.save(leave);
            return ResponseEntity.ok("Leave Type saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/policies")
    public ResponseEntity<?> getPolicies() {
        return ResponseEntity.ok(policyRepo.findAll());
    }

    @PostMapping("/policies/save")
    public ResponseEntity<?> savePolicy(@RequestBody LeavePolicy policy) {
        try {
            policyRepo.save(policy);
            return ResponseEntity.ok("Leave Policy saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/entitlements/save")
    public ResponseEntity<?> saveEntitlement(@RequestBody Map<String, Integer> data) {
        try {
            Integer empId = data.get("employeeId");
            Integer leaveId = data.get("leaveId");
            Integer days = data.get("days");

            if (empId == null || leaveId == null || days == null) {
                return ResponseEntity.badRequest().body("Missing required fields");
            }
            LeaveEntitlement ent = new LeaveEntitlement(empId, leaveId, days);
            entitlementRepo.save(ent);
            return ResponseEntity.ok("Entitlement updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // =========================================================
    // 1.5 HR ADMIN - SPECIAL LEAVE MANAGEMENT
    // =========================================================

    @GetMapping("/policies/special")
    public ResponseEntity<?> getSpecialPolicies() {
        return ResponseEntity.ok(policyRepo.findSpecialPolicies());
    }

    @PostMapping("/policies/special/configure")
    public ResponseEntity<?> configureSpecialLeave(@RequestBody Map<String, Object> payload) {
        try {
            Integer policyId = Integer.parseInt(payload.get("policyId").toString());
            String specialType = (String) payload.get("specialType"); // e.g., "Maternity"
            String rules = (String) payload.get("rules"); // e.g., "Female only"

            LeavePolicy policy = policyRepo.findById(policyId)
                    .orElseThrow(() -> new RuntimeException("Policy not found"));

            policy.setSpecialLeaveType(specialType);
            policy.setEligibilityRules(rules);

            policyRepo.save(policy);
            return ResponseEntity.ok("Special leave configured successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // =========================================================
    // 1.6 HR ADMIN - OVERRIDE REQUESTS
    // =========================================================

    @GetMapping("/hr/all-requests")
    public ResponseEntity<?> getAllRequestsForHR() {
        return ResponseEntity.ok(requestRepo.findAllRequestsForHR());
    }

    // =========================================================
    // 2. EMPLOYEE FEATURES (SUBMIT & VIEW)
    // =========================================================

    @PostMapping("/submit")
    @Transactional
    public ResponseEntity<?> submitLeave(
            @RequestParam("employeeId") Integer employeeId,
            @RequestParam("leaveId") Integer leaveId,
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr,
            @RequestParam("justification") String justification,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            LocalDate start = LocalDate.parse(startDateStr);
            LocalDate end = LocalDate.parse(endDateStr);
            long days = ChronoUnit.DAYS.between(start, end) + 1;

            if (days <= 0) return ResponseEntity.badRequest().body("End date must be after start date");

            LeaveRequest req = new LeaveRequest();
            req.setEmployeeId(employeeId);
            req.setLeaveId(leaveId);
            req.setDuration((int) days);
            req.setJustification(justification);
            req.setStatus("Pending");
            req.setIsFlagged(false);

            // IMPORTANT: Save dates so we can sync later
            req.setStartDate(start);
            req.setEndDate(end);

            LeaveRequest savedReq = requestRepo.save(req);

            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, file.getBytes());

                LeaveDocument doc = new LeaveDocument();
                doc.setLeaveRequestId(savedReq.getRequestId());
                doc.setFilePath("/uploads/" + fileName);
                doc.setUploadedAt(LocalDateTime.now());
                documentRepo.save(doc);
            }
            return ResponseEntity.ok("Leave request submitted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/my-requests/{empId}")
    public ResponseEntity<?> getMyRequests(@PathVariable Integer empId) {
        return ResponseEntity.ok(requestRepo.findRequestsByEmployee(empId));
    }

    @GetMapping("/balance/{empId}")
    public ResponseEntity<?> getLeaveBalance(@PathVariable Integer empId) {
        return ResponseEntity.ok(requestRepo.findLeaveBalances(empId));
    }

    // =========================================================
    // 3. MANAGER FEATURES (REVIEW & FLAG) & SYNC LOGIC
    // =========================================================

    @GetMapping("/manager/{managerId}/pending")
    public ResponseEntity<?> getPendingApprovals(@PathVariable Integer managerId) {
        return ResponseEntity.ok(requestRepo.findPendingRequestsForManager(managerId));
    }

    @PostMapping("/review")
    @Transactional // Required because we save to multiple tables
    public ResponseEntity<?> reviewLeaveRequest(
            @RequestParam("requestId") Integer requestId,
            @RequestParam("action") String action
    ) {
        try {
            String newStatus;
            if (action.equalsIgnoreCase("Approve")) newStatus = "Approved";
            else if (action.equalsIgnoreCase("Reject")) newStatus = "Rejected";
            else return ResponseEntity.badRequest().body("Invalid action.");

            // 1. Find and Update Request
            LeaveRequest request = requestRepo.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Request not found"));

            request.setStatus(newStatus);
            requestRepo.save(request);

            // 2. IF APPROVED -> SYNC WITH ATTENDANCE
            if ("Approved".equals(newStatus)) {
                syncLeaveToAttendance(request);
            }

            return ResponseEntity.ok("Request marked as " + newStatus + (newStatus.equals("Approved") ? " and synced." : ""));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/flag")
    public ResponseEntity<?> flagLeaveRequest(@RequestParam("requestId") Integer requestId) {
        try {
            requestRepo.flagRequest(requestId);
            return ResponseEntity.ok("Request flagged as irregular.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // =========================================================
    // 4. PRIVATE HELPER METHODS
    // =========================================================

    private void syncLeaveToAttendance(LeaveRequest request) {
        // Validation
        if (request.getStartDate() == null || request.getEndDate() == null) {
            System.err.println("Skipping sync: Start/End date missing for Request ID " + request.getRequestId());
            return;
        }

        // 1. Create AttendanceException record (To track why they are absent/on leave)
        AttendanceException ex = new AttendanceException(
                "Approved Leave (ID: " + request.getRequestId() + ")",
                "Leave",
                LocalDate.now(), // Date created
                "Active"
        );
        AttendanceException savedEx = exceptionRepo.save(ex);

        // 2. Loop through dates and insert Attendance records
        long daysBetween = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());

        for (int i = 0; i <= daysBetween; i++) {
            LocalDate currentDate = request.getStartDate().plusDays(i);

            // We default "entry time" to 9AM so the system sees a record exists
            LocalDateTime entryTime = currentDate.atTime(9, 0);

            Attendance attendance = new Attendance(
                    request.getEmployeeId(),
                    entryTime,
                    "Leave System", // Indicates automatic creation
                    savedEx.getExceptionId()
            );

            attendanceRepo.save(attendance);
        }
    }
}