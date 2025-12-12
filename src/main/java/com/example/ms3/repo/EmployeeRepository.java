package com.example.ms3.repo;

import com.example.ms3.table.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // --- 1. LOGIN & VALIDATION ---
    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    // Fetch Role ID directly (Native Query for speed)
    @Query(value = "SELECT TOP 1 role_id FROM dbo.Employee_Role WHERE employee_id = :employeeId", nativeQuery = true)
    Integer findRoleIdByEmployeeId(Integer employeeId);

    // --- 2. SIGN UP (Stored Procedure) ---
    @Transactional
    @Procedure(procedureName = "CreateEmployeeProfile")
    void createEmployeeProfile(
            @Param("FirstName") String firstName,
            @Param("LastName") String lastName,
            @Param("DepartmentID") Integer departmentId,
            @Param("RoleID") Integer roleId,
            @Param("HireDate") LocalDate hireDate,
            @Param("Email") String email,
            @Param("Phone") String phone,
            @Param("NationalID") String nationalId,
            @Param("DateOfBirth") LocalDate dateOfBirth,
            @Param("CountryOfBirth") String countryOfBirth,
            @Param("Password") String password
    );

    // --- 3. FETCH DATA ---
    // Get all employees with their Role ID joined
    @Query(value = """
            SELECT e.employee_id AS id, 
                   e.first_name AS firstName, 
                   e.last_name AS lastName, 
                   e.email AS email, 
                   e.national_id AS nationalId, 
                   r.role_id AS roleId, 
                   e.password AS password, 
                   e.profile_image AS profileImage, 
                   e.phone AS phone,
                   -- NEW COLUMNS FOR CONTRACT STATUS --
                   c.contract_id AS contractId,
                   c.end_date AS contractEndDate,
                   p.position_title AS positionTitle
            FROM dbo.Employee e 
            LEFT JOIN dbo.Employee_Role r ON e.employee_id = r.employee_id
            LEFT JOIN dbo.Contract c ON e.contract_id = c.contract_id
            LEFT JOIN dbo.Position p ON e.position_id = p.position_id
            """, nativeQuery = true)
    List<Map<String, Object>> findAllEmployeesWithRoles();

    // Get employees for a specific manager
    @Query(value = "SELECT * FROM dbo.Employee WHERE manager_id = :managerId", nativeQuery = true)
    List<Employee> findByManagerId(Integer managerId);

    // --- 4. UPDATES ---

    // Update Role
    @Modifying
    @Transactional
    @Query(value = "UPDATE dbo.Employee_Role SET role_id = :roleId WHERE employee_id = :employeeId", nativeQuery = true)
    void updateEmployeeRole(Integer employeeId, Integer roleId);

    // Update Profile Image
    @Modifying
    @Transactional
    @Query(value = "UPDATE dbo.Employee SET profile_image = :imageData WHERE employee_id = :id", nativeQuery = true)
    void updateProfileImage(Integer id, String imageData);

    // --- 5. CONTRACT & JOB DETAILS (CRITICAL FIX) ---
    // This updates the Foreign Keys in the Employee table to link to the new Contract/Position
    @Modifying
    @Transactional
    @Query(value = "UPDATE dbo.Employee " +
            "SET contract_id = :contractId, " +
            "    position_id = :positionId, " +
            "    salary_type_id = :salaryTypeId " +
            "WHERE employee_id = :employeeId", nativeQuery = true)
    void updateEmployeeJobDetails(Integer employeeId, Integer contractId, Integer positionId, Integer salaryTypeId);
}