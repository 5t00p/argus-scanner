package com.argus.argus_server.controller;

import com.argus.argus_server.model.Device;
import com.argus.argus_server.repository.DeviceRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    
    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
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
        return deviceRepository.save(device);
    }
}
