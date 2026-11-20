package com.argus.argus_server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Device")
public class Device {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deviceID")
    private Integer deviceID;

    @Column(name = "platform", length = 100)
    private String platform;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "caseID")
    private Integer caseID;

    // Getters and Setters
    public Device() {

    }

    /** Checks fields before saving (platform has to be android or ios, model has to fit the column). */
    public void validate() {
        if (platform == null || !(platform.equalsIgnoreCase("android") || platform.equalsIgnoreCase("ios"))) {
            throw new IllegalArgumentException("Platform must be \"android\" or \"ios\"");
        }
        platform = platform.toLowerCase();
        if (model != null && model.length() > 100) {
            throw new IllegalArgumentException("Model must be 100 characters or less");
        }
    }

    public Integer getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Integer deviceID) {
        this.deviceID = deviceID;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }
}
