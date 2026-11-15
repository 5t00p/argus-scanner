package com.argus.argus_server.analysis.checks;

import com.argus.argus_server.analysis.RiskCheck;
import com.argus.argus_server.analysis.RiskFactor;
import com.argus.argus_server.dto.ScannedApp;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/** +10 for each permission stalkerware typically abuses. */
@Component
@Order(5)
public class SensitivePermissionCheck implements RiskCheck {

    private static final String PREFIX = "android.permission.";
    private static final Set<String> SENSITIVE = Set.of(
            PREFIX + "ACCESS_FINE_LOCATION",
            PREFIX + "ACCESS_COARSE_LOCATION",
            PREFIX + "ACCESS_BACKGROUND_LOCATION",
            PREFIX + "READ_SMS",
            PREFIX + "RECEIVE_SMS",
            PREFIX + "READ_CALL_LOG",
            PREFIX + "RECORD_AUDIO",
            PREFIX + "CAMERA",
            PREFIX + "READ_CONTACTS");

    @Override
    public List<RiskFactor> evaluate(ScannedApp app) {
        return app.permissions().stream()
                .filter(SENSITIVE::contains)
                .map(permission -> new RiskFactor(10, "Permission: " + permission.substring(PREFIX.length())))
                .toList();
    }
}
