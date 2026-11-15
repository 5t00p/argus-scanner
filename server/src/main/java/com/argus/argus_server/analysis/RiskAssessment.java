package com.argus.argus_server.analysis;

import java.util.List;

/** The total result for one app: score 0-100 (% likelihood of stalkerware) and the reasons. */
public record RiskAssessment(int score, List<String> reasons, Integer signatureId) {

    public static final int FLAG_THRESHOLD = 50;

    public boolean flagged() {
        return score >= FLAG_THRESHOLD;
    }
}
