package com.argus.argus_server.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/case-assignments")
public class CaseAssignmentController {
    private final CaseAssignmentRepository caseAssignmentRepository;

    public CaseAssignmentController(CaseAssignmentRepository caseAssignmentRepository) {
        this.caseAssignmentRepository = caseAssignmentRepository;
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
}
