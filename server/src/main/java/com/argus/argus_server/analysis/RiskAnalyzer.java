package com.argus.argus_server.analysis;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
/** Scores one scanned app. No database access, so it can be unit tested on its own. */
public class RiskAnalyzer {
 
    public static final int FLAG_THRESHOLD = 50;
 
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
 
    /** score is 0-100, the % likelihood the app is stalkerware. */
    public record Result(int score, List<String> reasons) {
        public boolean flagged() {
            return score >= FLAG_THRESHOLD;
        }
    }
 
    /** @param signatureFamily app family of a matching known-stalkerware signature, or null if none matched */
    public static Result analyze(Map<?, ?> app, String signatureFamily) {
        int score = 0;
        List<String> reasons = new ArrayList<>();
 
        if (signatureFamily != null) {
            score += 100;
            reasons.add("Matches known stalkerware (" + signatureFamily + ")");
        }
        if (Boolean.TRUE.equals(app.get("hidden"))) {
            score += 20;
            reasons.add("Hidden from the app launcher");
        }
        if (Boolean.TRUE.equals(app.get("accessibility_enabled"))) {
            score += 25;
            reasons.add("Accessibility service enabled");
        }
        if (Boolean.TRUE.equals(app.get("device_admin"))) {
            score += 20;
            reasons.add("Device admin access");
        }
        if (app.get("permissions") instanceof List<?> permissions) {
            for (Object p : permissions) {
                if (SENSITIVE.contains(p)) {
                    score += 10;
                    reasons.add("Permission: " + p.toString().substring(PREFIX.length()));
                }
            }
        }
        return new Result(Math.min(score, 100), reasons);
    }
}
