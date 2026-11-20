package com.argus.argus_server.analysis;

/** One warning sign from a RiskCheck. signatureId is only set if it matched known stalkerware. */
public record RiskFactor(int points, String reason, Integer signatureId) {

    public RiskFactor(int points, String reason) {
        this(points, reason, null);
    }
}
