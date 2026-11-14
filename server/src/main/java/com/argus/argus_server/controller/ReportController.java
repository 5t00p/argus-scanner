package com.argus.argus_server.controller;
import java.util.List;
import java.util.Optional;

import com.argus.argus_server.model.Report;
import com.argus.argus_server.repository.ReportRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    

