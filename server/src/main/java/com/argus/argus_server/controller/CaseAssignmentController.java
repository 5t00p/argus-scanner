package com.argus.argus_server.controller;

import com.argus.argus_server.model.CaseAssignment;
import com.argus.argus_server.repository.CaseAssignmentRepository;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import com.argus.argus_server.service.CaseAssignmentService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/case-assignments")
public class CaseAssignmentController {

    private final CaseAssignmentRepository caseAssignmentRepository;
    private final CaseAssignmentService caseAssignmentService;

    public CaseAssignmentController(CaseAssignmentRepository caseAssignmentRepository, CaseAssignmentService caseAssignmentService) {
        this.caseAssignmentRepository = caseAssignmentRepository;
        this.caseAssignmentService = caseAssignmentService;
    }

    @GetMapping
    public List<CaseAssignment> getAllCaseAssignments() {
        return caseAssignmentRepository.findAll();
    }

    @GetMapping("/user/{userID}")
    public List<CaseAssignment> getCaseAssignmentsByUserID(@PathVariable Integer userID) {
        return caseAssignmentRepository.findByUserID(userID);
    }

    @GetMapping("/case/{caseID}")
    public List<CaseAssignment> getCaseAssignmentsByCaseID(@PathVariable Integer caseID) {
        return caseAssignmentRepository.findByCaseID(caseID);
    }

    @GetMapping("/user/{userID}/case/{caseID}")
    public List<CaseAssignment> getCaseAssignmentsByUserIDAndCaseID(@PathVariable Integer userID, @PathVariable Integer caseID) {
        return caseAssignmentRepository.findByUserIDAndCaseID(userID, caseID);
    }

    @PostMapping
    public CaseAssignment createCaseAssignment(@RequestBody CaseAssignment caseAssignment) {
        return caseAssignmentRepository.save(caseAssignment);
    }


    public record ReassignmentRequest(Integer newUserID, Integer newCaseID) {}

    @PutMapping("/user/{userID}/case/{caseID}")
    public ResponseEntity<CaseAssignment> reassignCase(@PathVariable Integer userID, @PathVariable Integer caseID, @RequestBody ReassignmentRequest request) {

        CaseAssignment reassigned = caseAssignmentService.reassign(userID, caseID, request.newUserID(), request.newCaseID());
        return ResponseEntity.ok(reassigned);
    }

}
