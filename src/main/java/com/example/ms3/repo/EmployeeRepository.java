package com.example.ms3.repo;

import com.example.ms3.table.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // --- Basic Finders ---
    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    // --- Native Queries ---

    @Query(value = "SELECT role_id FROM Employee_Role WHERE employee_id = :id", nativeQuery = true)
    Integer findRoleIdByEmployeeId(@Param("id") Integer id);

    @Query(value = "SELECT * FROM Employee WHERE manager_id = :managerId", nativeQuery = true)
    List<Employee> findByManagerId(@Param("managerId") Integer managerId);

    // Fetches all employees with their role/contract info (Used in HR dashboard)
    @Query(value = """
        SELECT 
            e.employee_id AS id, 
            e.first_name AS firstName, 
            e.last_name AS lastName, 
            e.email AS email, 
            e.national_id AS nationalId, 
            e.phone AS phone,
            e.profile_image AS profileImage,
            r.role_id AS roleId,
            c.contract_id AS contractId,
            c.end_date AS contractEndDate,
            p.position_title AS positionTitle
        FROM Employee e
        LEFT JOIN Employee_Role r ON e.employee_id = r.employee_id
        LEFT JOIN Contract c ON e.contract_id = c.contract_id
        LEFT JOIN Position p ON e.position_id = p.position_id
    """, nativeQuery = true)
    List<Map<String, Object>> findAllEmployeesWithRoles();

    // --- Modifications (Updates/Inserts) ---

    // 1. Create Profile (Used in SignUp Service)
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Employee (first_name, last_name, department_id, hire_date, email, phone, national_id, date_of_birth, country_of_birth, password) " +
            "VALUES (:fname, :lname, :deptId, :hireDate, :email, :phone, :nid, :dob, :country, :pass); " +
            "INSERT INTO Employee_Role (employee_id, role_id) VALUES ((SELECT MAX(employee_id) FROM Employee), :roleId)", nativeQuery = true)
    void createEmployeeProfile(
            @Param("fname") String fname,
            @Param("lname") String lname,
            @Param("deptId") Integer deptId,
            @Param("roleId") Integer roleId,
            @Param("hireDate") LocalDate hireDate,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("nid") String nid,
            @Param("dob") LocalDate dob,
            @Param("country") String country,
            @Param("pass") String pass
    );

    // 2. Update Role
    @Modifying
    @Transactional
    @Query(value = "UPDATE Employee_Role SET role_id = :roleId WHERE employee_id = :empId", nativeQuery = true)
    void updateEmployeeRole(@Param("empId") Integer empId, @Param("roleId") Integer roleId);

    // 3. Update Profile Image
    @Modifying
    @Transactional
    @Query(value = "UPDATE Employee SET profile_image = :image WHERE employee_id = :id", nativeQuery = true)
    void updateProfileImage(@Param("id") Integer id, @Param("image") String image);

    // 4. NEW: Add Employee To Manager's Team
    @Modifying
    @Transactional
    @Query(value = "UPDATE Employee SET manager_id = :managerId WHERE employee_id = :employeeId", nativeQuery = true)
    void addEmployeeToTeam(@Param("managerId") Integer managerId, @Param("employeeId") Integer employeeId);
}