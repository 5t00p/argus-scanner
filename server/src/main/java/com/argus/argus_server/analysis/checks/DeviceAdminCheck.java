package com.argus.argus_server.analysis.checks;

import com.argus.argus_server.analysis.RiskCheck;
import com.argus.argus_server.analysis.RiskFactor;
import com.argus.argus_server.dto.ScannedApp;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(4)
public class DeviceAdminCheck implements RiskCheck {

    @Override
    public List<RiskFactor> evaluate(ScannedApp app) {
        return app.isDeviceAdmin() ? List.of(new RiskFactor(20, "Device admin access")) : List.of();
    }
}
