package com.argus.argus_server.model;

public class Scan {

    private Long id;
    private String deviceId;
    private String scanDate;

    public Scan(Long id,
                String deviceId,
                String scanDate) {
        this.id = id;
        this.deviceId = deviceId;
        this.scanDate = scanDate;
    }

    public Long getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getScanDate() {
        return scanDate;
    }
}
