package com.argus.argus_server.controller;

import com.argus.argus_server.dto.ScanUpload;
import com.argus.argus_server.model.Finding;
import com.argus.argus_server.model.Scan;
import com.argus.argus_server.repository.FindingRepository;
import com.argus.argus_server.repository.ScanRepository;
import com.argus.argus_server.service.ScanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ScanController {

    private final ScanRepository scans;
    private final FindingRepository findings;
    private final ScanService scanService;

    public ScanController(ScanRepository scans, FindingRepository findings, ScanService scanService) {
        this.scans = scans;
        this.findings = findings;
        this.scanService = scanService;
    }

    @GetMapping("/api/scans")
    public List<Scan> getScans() {
        return scans.findAllByOrderByScanIDDesc();
    }

    @GetMapping("/api/scans/{scanId}/findings")
    public List<Finding> getFindings(@PathVariable int scanId) {
        return findings.findByScanIDOrderByRiskScoreDesc(scanId);
    }

    @PostMapping("/api/devices/{deviceId}/scans")
    public ResponseEntity<Map<String, Object>> uploadScan(@PathVariable int deviceId, @RequestBody ScanUpload upload) {
        if (!upload.isValid()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "scan_uuid (36 characters) and apps with a package_name are required"));
        }
        ScanService.Result result = scanService.saveScan(deviceId, upload);
        return ResponseEntity.status(result.created() ? HttpStatus.CREATED : HttpStatus.OK)
                .body(Map.of("scan_id", result.scanId()));
    }

    @PostMapping("/api/scans")
    public ResponseEntity<Map<String, Object>> createScan(@RequestParam("deviceId") int deviceId, @RequestBody ScanUpload upload){
    return uploadScan(deviceId, upload);
    }
}
