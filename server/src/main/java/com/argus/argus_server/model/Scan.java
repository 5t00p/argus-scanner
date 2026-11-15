package com.argus.argus_server.model;

import com.argus.argus_server.analysis.RiskAnalyzer;
import com.argus.argus_server.analysis.RiskAssessment;
import com.argus.argus_server.dto.ScannedApp;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Scan")
public class Scan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scanID")
    private Integer scanID;

    @JsonProperty("scan_uuid")
    @Column(name = "scan_uuid", length = 36, nullable = false, unique = true)
    private String scanUuid;

    @JsonProperty("scan_status")
    @Column(name = "scan_status", length = 50)
    private String status;

    @JsonProperty("started_at")
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "deviceID")
    private Integer deviceID;

    protected Scan() {
    }

    public Scan(String scanUuid, Integer deviceID) {
        this.scanUuid = scanUuid;
        this.deviceID = deviceID;
        this.status = "analyzed";
    }

    @PrePersist
    protected void onCreate() {
        // shortcut: started_at is when the server received the scan, use the scan's scanned_at once offline sync exists
        startedAt = LocalDateTime.now();
    }

    /** Runs every app through the analyzer and returns a Finding for each flagged one. */
    public List<Finding> runScan(List<ScannedApp> apps, RiskAnalyzer analyzer) {
        List<Finding> findings = new ArrayList<>();
        for (ScannedApp app : apps) {
            RiskAssessment risk = analyzer.assess(app);
            if (risk.flagged()) {
                findings.add(Finding.calculateRisk(scanID, app.packageName(), risk));
            }
        }
        return findings;
    }

    public Integer getScanID() {
        return scanID;
    }

    public String getScanUuid() {
        return scanUuid;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public Integer getDeviceID() {
        return deviceID;
    }
}
