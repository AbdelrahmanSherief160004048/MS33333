package com.example.ms3.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;


public class RegisterRequestDTO {

    // Matches HTML: <input name="firstName">
    @NotBlank(message = "First name is required")
    private String firstName;

    // Matches HTML: <input name="lastName">
    @NotBlank(message = "Last name is required")
    private String lastName;

    // Matches HTML: <input name="email">
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")

    private String email;

    // Matches HTML: <input name="password">
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    // Matches HTML: <input name="phone">
    @NotBlank(message = "Phone number is required")
    private String phone;

    // Matches HTML: <input name="nationalId">
    @NotBlank(message = "National ID is required")
    private String nationalId;

    // Matches HTML: <input name="dateOfBirth">
    // Spring Boot automatically converts "YYYY-MM-DD" string from HTML to LocalDate
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // Matches HTML: <input name="countryOfBirth">
    @NotBlank(message = "Country of birth is required")
    private String countryOfBirth;

    // Matches HTML: <select name="departmentId">
    @NotNull(message = "Department is required")
    private Integer departmentId;

    // Matches HTML: <select name="roleId">
    @NotNull(message = "Role is required")
    private Integer roleId;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}