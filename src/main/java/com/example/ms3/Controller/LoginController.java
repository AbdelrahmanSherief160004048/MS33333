package com.example.ms3.Controller;

import com.example.ms3.dto.ContractRequestDTO;
import com.example.ms3.dto.RegisterRequestDTO;
import com.example.ms3.exceptions.UserAlreadyExistsException;
import com.example.ms3.repo.ContractRepository;
import com.example.ms3.repo.EmployeeRepository;
import com.example.ms3.services.employeeService;
import com.example.ms3.table.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class LoginController {

    private final employeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final ContractRepository contractRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginController(employeeService employeeService,
                           EmployeeRepository employeeRepository,
                           ContractRepository contractRepository,
                           PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.contractRepository = contractRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================================
    //              1. VIEW ROUTES
    // ==========================================

    @GetMapping("/login") public String showLoginPage() { return "login"; }
    @GetMapping("/employees") public String showEmployeesPage() { return "employees"; }
    @GetMapping("/profile") public String showProfilePage() { return "profile"; }

    // --- VIEW ROUTE FOR MANAGER TEAM ---
    @GetMapping("/manager/team")
    public String showManagerTeamPage() {
        return "my-team"; // This matches the filename 'my-team.html'
    }

    @GetMapping("/hr-dashboard") public String hrAdminDashboard() { return "hr-dashboard"; }
    @GetMapping("/manager-dashboard") public String managerDashboard() { return "manager-dashboard"; }
    @GetMapping("/sysadmin-dashboard") public String sysAdminDashboard() { return "sysadmin-dashboard"; }
    @GetMapping("/payroll-dashboard") public String payrollDashboard() { return "payroll-dashboard"; }
    @GetMapping("/employee-dashboard") public String employeeDashboard() { return "employee-dashboard"; }


    // ==========================================
    //              2. API ENDPOINTS
    // ==========================================

    // --- A. LOGIN ---
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String rawPassword = loginData.get("password");

        Optional<Employee> employeeOpt = employeeRepository.findByEmail(email);
        if (employeeOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");
        Employee employee = employeeOpt.get();

        boolean isMatch = false;
        if (employee.getPassword() != null && passwordEncoder.matches(rawPassword, employee.getPassword())) isMatch = true;
        else if (employee.getPassword() != null && employee.getPassword().equals(rawPassword)) isMatch = true;
        else if (employee.getNationalId() != null && employee.getNationalId().equals(rawPassword)) isMatch = true;

        if (!isMatch) return ResponseEntity.badRequest().body("Invalid credentials");

        Integer roleId = employeeRepository.findRoleIdByEmployeeId(employee.getId());
        String targetUrl = "/employee-dashboard";
        if (roleId != null) {
            switch (roleId) {
                case 7001: targetUrl = "/hr-dashboard"; break;
                case 7002: targetUrl = "/manager-dashboard"; break;
                case 7003: targetUrl = "/sysadmin-dashboard"; break;
                case 7004: targetUrl = "/payroll-dashboard"; break;
                case 7005: targetUrl = "/employee-dashboard"; break;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("redirectUrl", targetUrl);
        response.put("userId", employee.getId());
        return ResponseEntity.ok().body(response);
    }

    // --- B. SIGN UP ---
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<?> signUp(@RequestBody RegisterRequestDTO employee) {
        try {
            employeeService.signUp(employee);
            return ResponseEntity.ok().body(Collections.singletonMap("redirectUrl", "/login"));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("Account already created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // --- C. EMPLOYEE DATA ---
    @GetMapping("/api/employees")
    @ResponseBody
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.findAllEmployeesWithRoles());
    }

    @GetMapping("/api/employees/{id}")
    @ResponseBody
    public ResponseEntity<?> getEmployeeById(@PathVariable Integer id) {
        return employeeRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/api/employees/update")
    @ResponseBody
    public ResponseEntity<?> updateEmployee(@RequestBody Map<String, String> data) {
        try {
            Integer id = Integer.parseInt(data.get("id"));
            Employee emp = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));

            // --- Basic Details ---
            if (data.containsKey("firstName")) emp.setFirstName(data.get("firstName"));
            if (data.containsKey("lastName")) emp.setLastName(data.get("lastName"));
            if (data.containsKey("email")) emp.setEmail(data.get("email"));
            if (data.containsKey("phone")) emp.setPhone(data.get("phone"));
            if (data.containsKey("nationalId")) emp.setNationalId(data.get("nationalId"));

            // --- NEW: Personal & Emergency Details ---
            if (data.containsKey("address")) emp.setAddress(data.get("address"));
            if (data.containsKey("emergencyContactName")) emp.setEmergencyContactName(data.get("emergencyContactName"));
            if (data.containsKey("emergencyContactPhone")) emp.setEmergencyContactPhone(data.get("emergencyContactPhone"));

            employeeRepository.save(emp);

            // --- Role Update (HR Only) ---
            if (data.containsKey("roleId")) {
                employeeRepository.updateEmployeeRole(id, Integer.parseInt(data.get("roleId")));
            }
            return ResponseEntity.ok("Profile updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }

    @PostMapping("/api/employees/upload-photo")
    @ResponseBody
    public ResponseEntity<?> uploadPhoto(@RequestBody Map<String, String> data) {
        try {
            employeeRepository.updateProfileImage(Integer.parseInt(data.get("id")), data.get("image"));
            return ResponseEntity.ok("Photo uploaded");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed");
        }
    }

    // --- D. CONTRACTS (Create & Renew) ---
    @PostMapping("/api/contracts/save")
    @ResponseBody
    public ResponseEntity<?> saveContract(@RequestBody ContractRequestDTO request) {
        try {
            // Check if we are UPDATING (Renewing) based on presence of Contract ID
            if (request.getContractId() != null && request.getContractId() > 0) {

                System.out.println(">>> CALLING PROC: RenewContract (ID: " + request.getContractId() + ")");

                // Calls your Proc --2.2
                contractRepository.renewContractProc(
                        request.getContractId(),
                        request.getEndDate() // Your Renew proc only takes End Date
                );

                return ResponseEntity.ok("Contract renewed successfully!");

            } else {

                System.out.println(">>> CALLING PROC: CreateContract (New)");

                // Calls your Proc --2.1
                contractRepository.createContractProc(
                        request.getEmployeeId(),
                        request.getContractType(),
                        request.getStartDate(),
                        request.getEndDate()
                );

                return ResponseEntity.ok("New Contract created successfully!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // --- E. MANAGER TEAM ---
    @GetMapping("/api/manager/{managerId}/team")
    @ResponseBody
    public ResponseEntity<?> getManagerTeam(@PathVariable Integer managerId) {
        return ResponseEntity.ok(employeeRepository.findByManagerId(managerId));
    }
}