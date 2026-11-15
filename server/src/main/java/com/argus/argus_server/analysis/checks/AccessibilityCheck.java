package com.argus.argus_server.analysis.checks;

import com.argus.argus_server.analysis.RiskCheck;
import com.argus.argus_server.analysis.RiskFactor;
import com.argus.argus_server.dto.ScannedApp;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(3)
public class AccessibilityCheck implements RiskCheck {

    @Override
    public List<RiskFactor> evaluate(ScannedApp app) {
        return app.hasAccessibilityEnabled() ? List.of(new RiskFactor(25, "Accessibility service enabled")) : List.of();
    }
}
