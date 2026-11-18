package com.argus.argus_server.controller;

import com.argus.argus_server.model.Device;
import com.argus.argus_server.repository.DeviceRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;


import com.argus.argus_server.repository.VictimCaseRepository;
import org.springframework.http.ResponseEntity;
import com.argus.argus_server.repository.ScanRepository;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    
    private final DeviceRepository deviceRepository;
    private final VictimCaseRepository vCaseRepository;
    private final ScanRepository scanRepository;

    public DeviceController(DeviceRepository deviceRepository, VictimCaseRepository vCaseRepository, ScanRepository scanRepository) {
        this.deviceRepository = deviceRepository;
        this.vCaseRepository = vCaseRepository;
        this.scanRepository = scanRepository;
    }

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable Integer id) {
        return deviceRepository.findById(id).orElse(null);
    }

    @GetMapping("/case/{caseID}")
    public List<Device> getDevicesByCaseID(@PathVariable Integer caseID) {
        return deviceRepository.findAll().stream().filter(device -> device.getCaseID().equals(caseID)).toList();
    }

    @PostMapping
    public Device createDevice(@RequestBody Device device) {
        device.setDeviceID(null); // a create must never overwrite an existing device
        if (device.getCaseID() == null || !vCaseRepository.existsById(device.getCaseID())) {
            throw new IllegalArgumentException("caseID must be an existing case");
        }
        device.validate();
        return deviceRepository.save(device);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Integer id, @RequestBody Device updatedDevice) {

    Device existingDevice = deviceRepository.findById(id).orElse(null);
    if (existingDevice == null) {
        return ResponseEntity.notFound().build();
    }

    if (updatedDevice.getCaseID() != null && !vCaseRepository.existsById(updatedDevice.getCaseID())) {
        return ResponseEntity.badRequest().build();
    }

    if (updatedDevice.getPlatform() != null) {
        existingDevice.setPlatform(updatedDevice.getPlatform());
    }

    if (updatedDevice.getModel() != null) {
        existingDevice.setModel(updatedDevice.getModel());
    }

    if (updatedDevice.getCaseID() != null) {
        existingDevice.setCaseID(updatedDevice.getCaseID());
    }
    existingDevice.validate();
    Device savedDevice = deviceRepository.save(existingDevice);
    return ResponseEntity.ok(savedDevice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Integer id) {

        if (!deviceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (scanRepository.existsByDeviceID(id)) {
            return ResponseEntity.status(409).build();
        }

        deviceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
