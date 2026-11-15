
package com.argus.argus_server.repository;

import com.argus.argus_server.model.Finding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FindingRepository extends JpaRepository<Finding, Integer> {
    List<Finding> findByScanID(Integer scanID);
    List<Finding> findByScanIDOrderByRiskScoreDesc(Integer scanID);
    List<Finding> findBySignaturesID(Integer signaturesID);
}
