package com.argus.argus_server.service;
 
import com.argus.argus_server.analysis.RiskAnalyzer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;
import java.util.Map;
import java.util.Objects;
 
@Service
public class ScanService {
 
    private final JdbcTemplate db;
 
    public ScanService(JdbcTemplate db) {
        this.db = db;
    }
 
    /** Saves the scan plus a Finding for every flagged app. All or nothing: any error rolls back the whole scan. */
    @Transactional
    public int saveScan(int deviceId, String uuid, List<?> apps) {
        // shortcut: started_at is when the server received the scan, use the scan's scanned_at once offline sync exists
        db.update("INSERT INTO Scan (scan_uuid, scan_status, started_at, deviceID) VALUES (?, 'analyzed', NOW(), ?)",
                uuid, deviceId);
        int scanId = db.queryForObject("SELECT scanID FROM Scan WHERE scan_uuid = ?", Integer.class, uuid);
 
        for (Object item : apps) {
            if (!(item instanceof Map<?, ?> app) || !(app.get("package_name") instanceof String pkg)) {
                throw new IllegalArgumentException("every app needs a package_name");
            }
            String certHash = app.get("cert_hash") instanceof String h ? h : null;
            List<Map<String, Object>> match = db.queryForList(
                    "SELECT signaturesID, app_family FROM Signatures WHERE package_name = ? OR cert_hash = ? LIMIT 1",
                    pkg, certHash);
 
            Object signatureId = match.isEmpty() ? null : match.get(0).get("signaturesID");
            String family = match.isEmpty() ? null : Objects.toString(match.get(0).get("app_family"), "unknown family");
            RiskAnalyzer.Result result = RiskAnalyzer.analyze(app, family);
 
            if (result.flagged()) {
                db.update("INSERT INTO Finding (package_name, reason, risk_score, scanID, signaturesID) VALUES (?, ?, ?, ?, ?)",
                        pkg, String.join("; ", result.reasons()), result.score(), scanId, signatureId);
            }
        }
        return scanId;
    }
}