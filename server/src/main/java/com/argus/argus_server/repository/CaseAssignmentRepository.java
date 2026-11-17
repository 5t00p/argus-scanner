package com.argus.argus_server.repository;

import com.argus.argus_server.model.CaseAssignment;
import com.argus.argus_server.model.CaseAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseAssignmentRepository extends JpaRepository<CaseAssignment, CaseAssignmentId> {
    List<CaseAssignment> findByUserID(Integer userID);
    List<CaseAssignment> findByCaseID(Integer caseID);
    List<CaseAssignment> findByUserIDAndCaseID(Integer userID, Integer caseID);
    boolean existsByCaseID(Integer caseID);
    boolean existsByUserID(Integer userID);
}