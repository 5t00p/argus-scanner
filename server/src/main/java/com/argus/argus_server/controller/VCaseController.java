package com.argus.argus_server.controller;

import com.argus.argus_server.model.VictimCase;
import com.argus.argus_server.repository.VictimCaseRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.argus.argus_server.repository.DeviceRepository;
import com.argus.argus_server.repository.CaseAssignmentRepository;

import com.argus.argus_server.repository.AuditlogRepository;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/cases")
public class VCaseController {
    
    private final VictimCaseRepository vCaseRepository;
    private final DeviceRepository deviceRepository;
    private final CaseAssignmentRepository caseAssignmentRepository;
    private final AuditlogRepository auditlogRepository;

    public VCaseController(VictimCaseRepository vCaseRepository, DeviceRepository deviceRepository, CaseAssignmentRepository caseAssignmentRepository, AuditlogRepository auditlogRepository) {
        this.vCaseRepository = vCaseRepository;
        this.deviceRepository = deviceRepository;
        this.caseAssignmentRepository = caseAssignmentRepository;
        this.auditlogRepository = auditlogRepository;
    }

    @GetMapping
    public List<VictimCase> getCases() {
        return vCaseRepository.findAll();
    }

    @GetMapping("/{id}")
    public VictimCase getCaseById(@PathVariable Integer id) {
        return vCaseRepository.findById(id).orElse(null);
    }

    @PostMapping
    public VictimCase createCase(@RequestBody VictimCase victimCase) {
        victimCase.setCaseID(null); // Ensure the ID is null so that a new case is created
        return vCaseRepository.save(victimCase);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VictimCase> updateCase(@PathVariable Integer id, @RequestBody VictimCase updatedCase) {
        return vCaseRepository.findById(id)
        .map(existingCase -> {
            if (updatedCase.getStatus() != null) {
                existingCase.setStatus(updatedCase.getStatus());
            }

            if (updatedCase.getSurvivorRef() != null) {
                existingCase.setSurvivorRef(updatedCase.getSurvivorRef());
            }

            VictimCase savedCase = vCaseRepository.save(existingCase);
            return ResponseEntity.ok(savedCase);

        })
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable Integer id) {

    if (!vCaseRepository.existsById(id)) {
        return ResponseEntity.notFound().build();
    }

    if (deviceRepository.existsByCaseID(id)) {
        return ResponseEntity.status(409).build();
    }

    if (caseAssignmentRepository.existsByCaseID(id)) {
        return ResponseEntity.status(409).build();
    }

    if (auditlogRepository.existsByCaseID(id)) {
        return ResponseEntity.status(409).build();
    }

    vCaseRepository.deleteById(id);
    return ResponseEntity.noContent().build();

    }

}
