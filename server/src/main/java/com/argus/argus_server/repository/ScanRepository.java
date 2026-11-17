package com.argus.argus_server.repository;

import com.argus.argus_server.model.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

public interface ScanRepository extends JpaRepository<Scan, Integer> {
    Optional<Scan> findByScanUuid(String scanUuid);
    List<Scan> findAllByOrderByScanIDDesc();
    boolean existsByDeviceID(Integer deviceID);
}
