package com.argus.argus_server.repository;

import com.argus.argus_server.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    Optional<Report> findByScanID(Integer scanID);
}