package com.argus.argus_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** The scan.json that the scanner app (or the dashboard upload) sends. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScanUpload(
        @JsonProperty("scan_uuid") String scanUuid,
        @JsonProperty("apps") List<ScannedApp> apps) {

    /** Check the scan before using it since it comes from a phone that might be compromised. */
    public boolean isValid() {
        return scanUuid != null && scanUuid.length() == 36 && apps != null
                && apps.stream().allMatch(app -> app != null && app.packageName() != null);
    }
}
