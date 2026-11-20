package com.argus.argus_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** One app from scan.json. Using Boolean instead of boolean so a missing field doesn't break parsing. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScannedApp(
        @JsonProperty("package_name") String packageName,
        @JsonProperty("cert_hash") String certHash,
        @JsonProperty("hidden") Boolean hidden,
        @JsonProperty("accessibility_enabled") Boolean accessibilityEnabled,
        @JsonProperty("device_admin") Boolean deviceAdmin,
        @JsonProperty("permissions") List<String> permissions) {

    public boolean isHidden() {
        return Boolean.TRUE.equals(hidden);
    }

    public boolean hasAccessibilityEnabled() {
        return Boolean.TRUE.equals(accessibilityEnabled);
    }

    public boolean isDeviceAdmin() {
        return Boolean.TRUE.equals(deviceAdmin);
    }

    @Override
    public List<String> permissions() {
        return permissions == null ? List.of() : permissions;
    }
}
