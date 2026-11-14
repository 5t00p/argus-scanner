package com.argus.argus_server.repository;

import com.argus.argus_server.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditlogRepository extends JpaRepository<AuditLog, Integer> {
    
}