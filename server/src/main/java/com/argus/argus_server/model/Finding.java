package com.argus.argus_server.model;

import com.argus.argus_server.analysis.RiskAssessment;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "Finding")
public class Finding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "findingID")
    private Integer findingID;

    @JsonProperty("package_name")
    @Column(name = "package_name")
    private String packageName;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @JsonProperty("risk_score")
    @Column(name = "risk_score")
    private Integer riskScore;

    @Column(name = "scanID")
    private Integer scanID;

    @Column(name = "signaturesID")
    private Integer signaturesID;

    public Finding() {
    }

    /** Builds the Finding for one flagged app from its risk assessment. */
    public static Finding calculateRisk(Integer scanID, String packageName, RiskAssessment risk) {
        Finding finding = new Finding();
        finding.scanID = scanID;
        finding.packageName = packageName;
        finding.riskScore = risk.score();
        finding.reason = String.join("; ", risk.reasons());
        finding.signaturesID = risk.signatureId();
        return finding;
    }

    /** Label shown on the dashboard. Signature match means known stalkerware, otherwise it's based on the score. */
    public String getClassification() {
        if (signaturesID != null) {
            return "Known stalkerware";
        }
        return riskScore != null && riskScore >= 75 ? "High risk" : "Suspicious";
    }

    public Integer getFindingID() {
        return findingID;
    }

    public void setFindingID(Integer findingID) {
        this.findingID = findingID;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public Integer getScanID() {
        return scanID;
    }

    public void setScanID(Integer scanID) {
        this.scanID = scanID;
    }

    public Integer getSignaturesID() {
        return signaturesID;
    }

    public void setSignaturesID(Integer signaturesID) {
        this.signaturesID = signaturesID;
    }
}
