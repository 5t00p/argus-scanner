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
        // build a fresh case so the request can't choose its own ID or opened date
        VictimCase newCase = new VictimCase();
        newCase.changeSurvivorRef(victimCase.getSurvivorRef());
        newCase.updateStatus(victimCase.getStatus() == null ? "open" : victimCase.getStatus());
        newCase.setOpenedAt(java.time.LocalDateTime.now());
        return vCaseRepository.save(newCase);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VictimCase> updateCase(@PathVariable Integer id, @RequestBody VictimCase updatedCase) {
        return vCaseRepository.findById(id)
        .map(existingCase -> {
            if (updatedCase.getStatus() != null) {
                existingCase.updateStatus(updatedCase.getStatus());
            }

            if (updatedCase.getSurvivorRef() != null) {
                existingCase.changeSurvivorRef(updatedCase.getSurvivorRef());
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
