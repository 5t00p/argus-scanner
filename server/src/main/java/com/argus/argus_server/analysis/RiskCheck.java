package com.argus.argus_server.analysis;

import com.argus.argus_server.dto.ScannedApp;

import java.util.List;

/** One kind of warning sign. Add a new check by writing a new class; the analyzer never changes. */
@FunctionalInterface
public interface RiskCheck {
    List<RiskFactor> evaluate(ScannedApp app);
}
