package com.example.ms3.Controller;

import com.example.ms3.repo.LeaveDocumentRepository;
import com.example.ms3.repo.LeaveRepository;
import com.example.ms3.repo.LeaveRequestRepository;
import com.example.ms3.table.LeaveDocument;
import com.example.ms3.table.LeaveRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveRepository leaveRepo;
    private final LeaveRequestRepository requestRepo;
    private final LeaveDocumentRepository documentRepo;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public LeaveController(LeaveRepository leaveRepo, LeaveRequestRepository requestRepo, LeaveDocumentRepository documentRepo) {
        this.leaveRepo = leaveRepo;
        this.requestRepo = requestRepo;
        this.documentRepo = documentRepo;
    }

    @GetMapping("/types")
    public ResponseEntity<?> getLeaveTypes() {
        return ResponseEntity.ok(leaveRepo.findAll());
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitLeave(
            @RequestParam("employeeId") Integer employeeId,
            @RequestParam("leaveId") Integer leaveId,
            @RequestParam("startDate") String startDateStr, // User sends Date
            @RequestParam("endDate") String endDateStr,     // User sends Date
            @RequestParam("justification") String justification,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            // 1. Logic: Convert Dates to Duration
            LocalDate start = LocalDate.parse(startDateStr);
            LocalDate end = LocalDate.parse(endDateStr);
            long days = ChronoUnit.DAYS.between(start, end) + 1; // +1 includes the start day

            if (days <= 0) return ResponseEntity.badRequest().body("End date must be after start date");

            // 2. Logic: Manual ID Generation (Fixes 'Cannot insert NULL')
            Integer maxId = requestRepo.findMaxRequestId();
            Integer newId = (maxId == null) ? 1 : maxId + 1;

            // 3. Create Object
            LeaveRequest req = new LeaveRequest();
            req.setRequestId(newId);        // Set Manual ID
            req.setEmployeeId(employeeId);
            req.setLeaveId(leaveId);
            req.setDuration((int) days);    // Save Duration (Not Dates)
            req.setJustification(justification);
            req.setStatus("Pending");

            requestRepo.save(req); // Save to DB

            // 4. File Upload (Same as before)
            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, file.getBytes());

                LeaveDocument doc = new LeaveDocument();
                doc.setLeaveRequestId(newId); // Use the manual ID
                doc.setFilePath("/uploads/" + fileName);
                doc.setUploadedAt(LocalDateTime.now());
                documentRepo.save(doc);
            }

            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/my-requests/{empId}")
    public ResponseEntity<?> getMyRequests(@PathVariable Integer empId) {
        return ResponseEntity.ok(requestRepo.findRequestsByEmployee(empId));
    }
}