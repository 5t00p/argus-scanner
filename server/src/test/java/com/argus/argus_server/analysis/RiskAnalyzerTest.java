package com.argus.argus_server.analysis;

import com.argus.argus_server.analysis.checks.AccessibilityCheck;
import com.argus.argus_server.analysis.checks.DeviceAdminCheck;
import com.argus.argus_server.analysis.checks.HiddenAppCheck;
import com.argus.argus_server.analysis.checks.SensitivePermissionCheck;
import com.argus.argus_server.dto.ScannedApp;
import com.argus.argus_server.model.Finding;
import com.argus.argus_server.model.Scan;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RiskAnalyzerTest {

    // stands in for SignatureMatchCheck so the test needs no database
    private final RiskCheck fakeSignatureMatch = app -> "com.test.spyapp".equals(app.packageName())
            ? List.of(new RiskFactor(100, "Matches known stalkerware (TestSpy)", 7))
            : List.of();

    private final RiskAnalyzer analyzer = new RiskAnalyzer(List.of(
            fakeSignatureMatch, new HiddenAppCheck(), new AccessibilityCheck(),
            new DeviceAdminCheck(), new SensitivePermissionCheck()));

    private static ScannedApp app(String packageName, boolean hidden, String... permissions) {
        return new ScannedApp(packageName, null, hidden, false, false, List.of(permissions));
    }

    @Test
    void hiddenAppWithSpyPermissionsIsFlagged() {
        RiskAssessment risk = analyzer.assess(app("com.example.fakestalker", true,
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.READ_SMS",
                "android.permission.RECORD_AUDIO"));

        assertEquals(50, risk.score());
        assertTrue(risk.flagged());
        assertEquals(List.of("Hidden from the app launcher", "Permission: ACCESS_FINE_LOCATION",
                "Permission: READ_SMS", "Permission: RECORD_AUDIO"), risk.reasons());
        assertNull(risk.signatureId());
    }

    @Test
    void normalAppIsNotFlagged() {
        RiskAssessment risk = analyzer.assess(app("com.argus_scanner", false, "android.permission.QUERY_ALL_PACKAGES"));

        assertEquals(0, risk.score());
        assertFalse(risk.flagged());
    }

    @Test
    void signatureMatchIsAlwaysFlaggedAndCappedAt100() {
        RiskAssessment risk = analyzer.assess(app("com.test.spyapp", true));

        assertEquals(100, risk.score());
        assertEquals(7, risk.signatureId());
    }

    @Test
    void missingFieldsCountAsFalse() {
        RiskAssessment risk = analyzer.assess(new ScannedApp("com.example.app", null, null, null, null, null));

        assertEquals(0, risk.score());
    }

    @Test
    void scanOnlyCreatesFindingsForFlaggedApps() {
        Scan scan = new Scan("11111111-1111-1111-1111-111111111111", 1);

        List<Finding> findings = scan.runScan(List.of(
                app("com.test.spyapp", false),
                app("com.argus_scanner", false)), analyzer);

        assertEquals(1, findings.size());
        assertEquals("com.test.spyapp", findings.get(0).getPackageName());
        assertEquals("Known stalkerware", findings.get(0).getClassification());
    }
}
