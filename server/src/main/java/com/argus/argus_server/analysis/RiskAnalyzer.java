package com.argus.argus_server.analysis;

import com.argus.argus_server.dto.ScannedApp;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/** Runs every RiskCheck on an app and adds up the result. Spring passes in all RiskCheck classes. */
@Component
public class RiskAnalyzer {

    private final List<RiskCheck> checks;

    public RiskAnalyzer(List<RiskCheck> checks) {
        this.checks = checks;
    }

    public RiskAssessment assess(ScannedApp app) {
        List<RiskFactor> factors = checks.stream()
                .flatMap(check -> check.evaluate(app).stream())
                .toList();

        int score = Math.min(100, factors.stream().mapToInt(RiskFactor::points).sum());
        List<String> reasons = factors.stream().map(RiskFactor::reason).toList();
        Integer signatureId = factors.stream()
                .map(RiskFactor::signatureId)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        return new RiskAssessment(score, reasons, signatureId);
    }
}
