
package com.argus.argus_server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/findings")
public class FindingController {
    private final FindingRepository findingRepository;

    public FindingController(FindingRepository findingRepository) {
        this.findingRepository = findingRepository;
    }

    @GetMapping
    public List<Finding> getAllFindings() {
        return findingRepository.findAll();
    }

    @GetMapping("/{findingID}")
    public ResponseEntity<Finding> getFindingByID(@PathVariable Integer findingID) {
        Optional<Finding> finding = findingRepository.findById(findingID);
        return finding.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/scan/{scanID}")
    public List<Finding> getFindingsByScanID(@PathVariable Integer scanID) {
        return findingRepository.findByScanID(scanID);
    }

    @GetMapping("/signature/{signaturesID}")
    public List<Finding> getFindingsBySignaturesID(@PathVariable Integer signaturesID) {
        return findingRepository.findBySignaturesID(signaturesID);
    }

    @PostMapping
    public Finding createFinding(@RequestBody Finding finding) {
        return findingRepository.save(finding);
    }
}
