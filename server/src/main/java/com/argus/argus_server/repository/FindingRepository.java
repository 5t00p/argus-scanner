
package com.argus.argus_server.repository;

import com.argus.argus_server.model.Finding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FindingRepository extends JpaRepository<Finding, Integer> {
}
