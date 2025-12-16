package com.example.ms3.Controller;

import com.example.ms3.repo.ShiftScheduleRepository;
import com.example.ms3.table.ShiftSchedule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;
import java.util.Map;

@RestController
public class ShiftController {

    private final ShiftScheduleRepository shiftRepository;

    public ShiftController(ShiftScheduleRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    // GET: Returns list of shifts from ShiftSchedule table
    @GetMapping("/api/shifts/types")
    public ResponseEntity<?> getAllShiftTypes() {
        return ResponseEntity.ok(shiftRepository.findAll());
    }

    // POST: Saves new shift into ShiftSchedule table
    @PostMapping("/api/shifts/types/create")
    public ResponseEntity<?> createShiftType(@RequestBody Map<String, String> payload) {
        try {
            // Map frontend "typeName" -> database "name"
            ShiftSchedule shift = new ShiftSchedule(
                    payload.get("typeName"),
                    LocalTime.parse(payload.get("startTime")),
                    LocalTime.parse(payload.get("endTime"))
            );
            shiftRepository.save(shift);
            return ResponseEntity.ok("Shift created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}