package com.argus.argus_server.controller;

import com.argus.argus_server.model.VictimCase;
import com.argus.argus_server.repository.VictimCaseRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class VCaseController {
    
    private final VictimCaseRepository vCaseRepository;

    public VCaseController(VictimCaseRepository vCaseRepository) {
        this.vCaseRepository = vCaseRepository;
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
}
