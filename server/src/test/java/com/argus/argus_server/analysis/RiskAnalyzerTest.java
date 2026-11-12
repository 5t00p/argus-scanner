package com.argus.argus_server.analysis;
 
import org.junit.jupiter.api.Test;
 
import java.util.List;
import java.util.Map;
 
import static org.junit.jupiter.api.Assertions.*;
 
class RiskAnalyzerTest {
 
    @Test
    void hiddenAppWithSpyPermissionsIsFlagged() {
        Map<String, Object> app = Map.of(
                "package_name", "com.example.fakestalker",
                "hidden", true,
                "permissions", List.of(
                        "android.permission.ACCESS_FINE_LOCATION",
                        "android.permission.READ_SMS",
                        "android.permission.RECORD_AUDIO"));
 
        RiskAnalyzer.Result result = RiskAnalyzer.analyze(app, null);
 
        assertEquals(50, result.score());
        assertTrue(result.flagged());
        assertEquals(4, result.reasons().size());
    }
 
    @Test
    void normalAppIsNotFlagged() {
        Map<String, Object> app = Map.of(
                "package_name", "com.argus_scanner",
                "hidden", false,
                "permissions", List.of("android.permission.QUERY_ALL_PACKAGES"));
 
        RiskAnalyzer.Result result = RiskAnalyzer.analyze(app, null);
 
        assertEquals(0, result.score());
        assertFalse(result.flagged());
    }
 
    @Test
    void signatureMatchIsAlwaysFlaggedAndCappedAt100() {
        Map<String, Object> app = Map.of("package_name", "com.test.spyapp", "hidden", true);
 
        RiskAnalyzer.Result result = RiskAnalyzer.analyze(app, "TestSpy");
 
        assertEquals(100, result.score());
        assertTrue(result.flagged());
    }
}