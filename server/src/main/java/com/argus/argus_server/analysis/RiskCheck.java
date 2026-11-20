package com.argus.argus_server.analysis;

import com.argus.argus_server.dto.ScannedApp;

import java.util.List;

/** One type of warning sign. To add a check just make a new class, RiskAnalyzer picks it up automatically. */
@FunctionalInterface
public interface RiskCheck {
    List<RiskFactor> evaluate(ScannedApp app);
}
