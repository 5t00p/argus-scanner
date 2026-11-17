package com.argus.argus_server.repository;

import com.argus.argus_server.model.VictimCase;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VictimCaseRepository extends JpaRepository<VictimCase, Integer> {

    /** SELECT ... FOR UPDATE: holds the case row until the transaction ends (design doc deadlock rule). */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from VictimCase c where c.caseID = :id")
    Optional<VictimCase> lockById(@Param("id") Integer id);
}