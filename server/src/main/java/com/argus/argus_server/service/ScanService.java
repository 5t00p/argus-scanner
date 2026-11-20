package com.argus.argus_server.service;

import com.argus.argus_server.analysis.RiskAnalyzer;
import com.argus.argus_server.dto.ScanUpload;
import com.argus.argus_server.model.Device;
import com.argus.argus_server.model.Scan;
import com.argus.argus_server.repository.DeviceRepository;
import com.argus.argus_server.repository.FindingRepository;
import com.argus.argus_server.repository.ScanRepository;
import com.argus.argus_server.repository.VictimCaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ScanService {

    public record Result(int scanId, boolean created) {
    }

    private final ScanRepository scans;
    private final FindingRepository findings;
    private final DeviceRepository devices;
    private final RiskAnalyzer analyzer;
    private final VictimCaseRepository cases;

    public ScanService(ScanRepository scans, FindingRepository findings, DeviceRepository devices, RiskAnalyzer analyzer, VictimCaseRepository cases) {
        this.scans = scans;
        this.findings = findings;
        this.devices = devices;
        this.analyzer = analyzer;
        this.cases = cases;
    }

    /** Saves the scan and a Finding for each flagged app. If anything fails the whole scan gets rolled back. */
    @Transactional
    public Result saveScan(int deviceId, ScanUpload upload) {
        Device device = devices.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "device " + deviceId + " not found"));

        // deadlock rule: always lock the case first so everything locks in the same order
        if (device.getCaseID() != null) {
            cases.lockById(device.getCaseID());
        }

        Optional<Scan> existing = scans.findByScanUuid(upload.scanUuid());
        if (existing.isPresent()) {
            return new Result(existing.get().getScanID(), false); // same scan uploaded again
        }

        Scan scan = scans.save(new Scan(upload.scanUuid(), device.getDeviceID()));
        findings.saveAll(scan.runScan(upload.apps(), analyzer));
        return new Result(scan.getScanID(), true);
    }
}
