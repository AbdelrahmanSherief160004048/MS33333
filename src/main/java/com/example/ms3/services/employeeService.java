package com.example.ms3.services;

import com.example.ms3.dto.RegisterRequestDTO;
import com.example.ms3.exceptions.UserAlreadyExistsException;
import com.example.ms3.repo.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // Import this
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class employeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder; // Inject Password Encoder

    public employeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(RegisterRequestDTO employeeSignupDTO) {

        // 1. REMOVE THE ROLE CHECK HERE
        // (We removed the "if role == 7001 throw exception" logic so you can add Admins)

        // 2. Check if email exists
        if(employeeRepository.existsByEmail(employeeSignupDTO.getEmail())){
            throw new UserAlreadyExistsException("Account already created");
        }

        // 3. Hash the password before sending to DB
        String encodedPassword = passwordEncoder.encode(employeeSignupDTO.getPassword());

        // 4. Call Repository
        employeeRepository.createEmployeeProfile(
                employeeSignupDTO.getFirstName(),
                employeeSignupDTO.getLastName(),
                employeeSignupDTO.getDepartmentId(),
                employeeSignupDTO.getRoleId(),
                LocalDate.now(), // Hire Date
                employeeSignupDTO.getEmail(),
                employeeSignupDTO.getPhone(),
                employeeSignupDTO.getNationalId(),
                employeeSignupDTO.getDateOfBirth(),
                employeeSignupDTO.getCountryOfBirth(),
                encodedPassword // Send Hashed Password
        );
    }
}