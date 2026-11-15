package com.argus.argus_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** The scan.json body a scanner (or the dashboard upload) sends to the server. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScanUpload(
        @JsonProperty("scan_uuid") String scanUuid,
        @JsonProperty("apps") List<ScannedApp> apps) {

    /** The scan comes from a phone that may be compromised, so check its shape before trusting it. */
    public boolean isValid() {
        return scanUuid != null && scanUuid.length() == 36 && apps != null
                && apps.stream().allMatch(app -> app != null && app.packageName() != null);
    }
}
