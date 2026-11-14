package com.argus.argus_server.controller;

import com.argus.argus_server.model.AuditLog;
import com.argus.argus_server.repository.AuditlogRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auditlogs")
public class AuditlogController {
    
    private final AuditlogRepository auditlogRepository;

    public AuditlogController(AuditlogRepository auditlogRepository) {
        this.auditlogRepository = auditlogRepository;
    }

    @GetMapping
    public List<AuditLog> getAllAuditlogs() {
        return auditlogRepository.findAll();
    }

    @GetMapping("/{id}")
    public AuditLog getAuditlogById(@PathVariable Integer id) {
        return auditlogRepository.findById(id).orElse(null);
    }

    @PostMapping
    public AuditLog createAuditlog(@RequestBody AuditLog auditlog) {
        return auditlogRepository.save(auditlog);
    }
}
