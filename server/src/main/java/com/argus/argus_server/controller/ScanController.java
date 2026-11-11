package com.argus.argus_server.controller;
 
import com.argus.argus_server.service.ScanService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Map;
 
@RestController
public class ScanController {
 
    private final JdbcTemplate db;
    private final ScanService scans;
 
    public ScanController(JdbcTemplate db, ScanService scans) {
        this.db = db;
        this.scans = scans;
    }
 
    @GetMapping("/api/scans")
    public List<Map<String, Object>> getScans() {
        return db.queryForList(
                "SELECT scanID, scan_uuid, scan_status, started_at, deviceID FROM Scan ORDER BY scanID DESC");
    }
 
    @GetMapping("/api/scans/{scanId}/findings")
    public List<Map<String, Object>> getFindings(@PathVariable int scanId) {
        return db.queryForList(
                "SELECT findingID, package_name, reason, risk_score, signaturesID FROM Finding "
                        + "WHERE scanID = ? ORDER BY risk_score DESC", scanId);
    }
 
    @PostMapping("/api/devices/{deviceId}/scans")
    public ResponseEntity<Map<String, Object>> uploadScan(@PathVariable int deviceId,
                                                          @RequestBody Map<String, Object> scan) {
        // the scan comes from a phone that may be compromised, so check its shape before saving
        if (!(scan.get("scan_uuid") instanceof String uuid) || uuid.length() != 36
                || !(scan.get("apps") instanceof List<?> apps)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "scan_uuid (36 characters) and apps are required"));
        }
 
        try {
            int scanId = scans.saveScan(deviceId, uuid, apps);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("scan_id", scanId));
        } catch (DuplicateKeyException e) {
            // same scan uploaded again: return the one already saved
            Integer existing = db.queryForObject("SELECT scanID FROM Scan WHERE scan_uuid = ?", Integer.class, uuid);
            return ResponseEntity.ok(Map.of("scan_id", existing));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "device " + deviceId + " not found"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}