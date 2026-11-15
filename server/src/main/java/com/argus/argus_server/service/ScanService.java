package com.argus.argus_server.service;

import com.argus.argus_server.analysis.RiskAnalyzer;
import com.argus.argus_server.dto.ScanUpload;
import com.argus.argus_server.model.Device;
import com.argus.argus_server.model.Scan;
import com.argus.argus_server.repository.DeviceRepository;
import com.argus.argus_server.repository.FindingRepository;
import com.argus.argus_server.repository.ScanRepository;
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

    public ScanService(ScanRepository scans, FindingRepository findings, DeviceRepository devices, RiskAnalyzer analyzer) {
        this.scans = scans;
        this.findings = findings;
        this.devices = devices;
        this.analyzer = analyzer;
    }

    /** Saves the scan plus a Finding for every flagged app. All or nothing: any error rolls back the whole scan. */
    @Transactional
    public Result saveScan(int deviceId, ScanUpload upload) {
        Device device = devices.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "device " + deviceId + " not found"));

        Optional<Scan> existing = scans.findByScanUuid(upload.scanUuid());
        if (existing.isPresent()) {
            return new Result(existing.get().getScanID(), false); // same scan uploaded again
        }

        Scan scan = scans.save(new Scan(upload.scanUuid(), device.getDeviceID()));
        findings.saveAll(scan.runScan(upload.apps(), analyzer));
        return new Result(scan.getScanID(), true);
    }
}
