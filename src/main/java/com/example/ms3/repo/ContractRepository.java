package com.example.ms3.repo;

import com.example.ms3.table.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {

    // Matches your Proc --2.1
    @Modifying
    @Transactional
    @Query(value = "EXEC CreateContract :employeeId, :type, :startDate, :endDate", nativeQuery = true)
    void createContractProc(
            @Param("employeeId") Integer employeeId,
            @Param("type") String type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Matches your Proc --2.2 (ONLY accepts ID and EndDate)
    @Modifying
    @Transactional
    @Query(value = "EXEC RenewContract :id, :endDate", nativeQuery = true)
    void renewContractProc(
            @Param("id") Integer id,
            @Param("endDate") LocalDate endDate
    );
}