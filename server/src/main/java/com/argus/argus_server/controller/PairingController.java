package com.argus.argus_server.controller;

import com.argus.argus_server.dto.ScanUpload;
import com.argus.argus_server.repository.DeviceRepository;
import com.argus.argus_server.security.PairingCodes;
import com.argus.argus_server.service.ScanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class PairingController {

    private final PairingCodes pairingCodes;
    private final DeviceRepository devices;
    private final ScanService scanService;

    public PairingController(PairingCodes pairingCodes, DeviceRepository devices, ScanService scanService) {
        this.pairingCodes = pairingCodes;
        this.devices = devices;
        this.scanService = scanService;
    }

    /** Dashboard side: makes a code for the survivor to type into the scanner app. */
    @PostMapping("/api/devices/{deviceId}/pairing-code")
    public ResponseEntity<Map<String, Object>> createCode(@PathVariable int deviceId) {
        if (!devices.existsById(deviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "device " + deviceId + " not found"));
        }
        return ResponseEntity.ok(Map.of(
                "code", pairingCodes.create(deviceId),
                "expires_in_minutes", PairingCodes.LIFETIME.toMinutes()));
    }

    /** Scanner app side (no login): uploads a scan with the pairing code instead of a device ID. */
    @PostMapping("/api/pairing/{code}/scans")
    public ResponseEntity<Map<String, Object>> uploadWithCode(@PathVariable String code, @RequestBody ScanUpload upload) {
        Optional<Integer> deviceId = pairingCodes.deviceFor(code);
        if (deviceId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "pairing code is wrong or expired"));
        }
        if (!upload.isValid()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "scan_uuid (36 characters) and apps with a package_name are required"));
        }
        ScanService.Result result = scanService.saveScan(deviceId.get(), upload);
        pairingCodes.use(code);
        return ResponseEntity.status(result.created() ? HttpStatus.CREATED : HttpStatus.OK)
                .body(Map.of("scan_id", result.scanId()));
    }
}
