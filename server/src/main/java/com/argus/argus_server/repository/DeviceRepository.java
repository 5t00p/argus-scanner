package com.argus.argus_server.repository;

import com.argus.argus_server.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
    
}
