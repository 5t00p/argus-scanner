package com.argus.argus_server.analysis;

/** One warning sign found by a RiskCheck. signatureId is set only by a known-stalkerware match. */
public record RiskFactor(int points, String reason, Integer signatureId) {

    public RiskFactor(int points, String reason) {
        this(points, reason, null);
    }
}
