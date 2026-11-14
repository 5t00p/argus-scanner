package com.argus.argus_server.model;
import jakarta.persistence.*;

@Entity
@Table(name = "Finding")
public class Finding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "findingID")
    private Integer findingID;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "risk_score")
    private Integer riskScore;

    @Column(name = "scanID")
    private Integer scanID;

    @Column(name = "signaturesID")
    private Integer signaturesID;

    public Finding() {
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
