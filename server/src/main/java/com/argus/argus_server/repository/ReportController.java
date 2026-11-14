package com.argus.argus_server.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.com.argus.argus_server.model.Report;
import main.java.com.argus.argus_server.repository.ReportRepository;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportRepository reportRepository;

    public ReportController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @GetMapping("/{reportID}")
    public ResponseEntity<Report> getReportByID(@PathVariable Integer reportID) {
        Optional<Report> report = reportRepository.findById(reportID);
        return report.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/scan/{scanID}")
    public ResponseEntity<Report> getReportByScanID(@PathVariable Integer scanID) {
        Optional<Report> report = reportRepository.findByScanID(scanID);
        return report.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Report createReport(@RequestBody Report report) {
        return reportRepository.save(report);
    }
}
    

