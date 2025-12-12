package com.example.ms3.table;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Nationalized;
import java.time.LocalDate;

@Entity
@Table(name = "Employee", schema = "dbo")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Handles the Identity column
    @Column(name = "employee_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Nationalized
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Size(max = 100)
    @Nationalized
    @Column(name = "full_name", length = 100)
    private String fullName;

    @Size(max = 50)
    @Nationalized
    @Column(name = "national_id", length = 50)
    private String nationalId;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 50)
    @Nationalized
    @Column(name = "country_of_birth", length = 50)
    private String countryOfBirth;

    @Size(max = 50)
    @Nationalized
    @Column(name = "phone", length = 50)
    private String phone;

    @Size(max = 100)
    @Nationalized
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Nationalized
    @Lob
    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "manager_id")
    private Integer managerId;

    @Column(name = "password")
    private String password;

    // --- GETTERS AND SETTERS (THIS IS WHAT WAS MISSING) ---

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getCountryOfBirth() { return countryOfBirth; }
    public void setCountryOfBirth(String countryOfBirth) { this.countryOfBirth = countryOfBirth; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    @Nationalized
    @Lob
    @Column(name = "address")
    private String address;

    @Size(max = 100)
    @Nationalized
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Size(max = 50)
    @Nationalized
    @Column(name = "emergency_contact_phone", length = 50)
    private String emergencyContactPhone;

    // --- Getters and Setters ---
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }

    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
}