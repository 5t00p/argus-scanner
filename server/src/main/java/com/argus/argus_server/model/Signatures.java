package com.argus.argus_server.model;

import com.argus.argus_server.dto.ScannedApp;
import jakarta.persistence.*;

@Entity
@Table(name = "Signatures")
public class Signatures {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "signaturesID")
    private Integer signaturesID;

    @Column(name = "cert_hash", length = 100)
    private String certHash;

    @Column(name = "package_name", length = 100)
    private String packageName;

    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "app_family", length = 100)
    private String appFamily;

    // Getters and Setters
    public Signatures() {
    }

    public boolean matchesApp(ScannedApp app) {
        return (packageName != null && packageName.equalsIgnoreCase(app.packageName()))
                || (certHash != null && certHash.equalsIgnoreCase(app.certHash()));
    }

    public Integer getSignaturesID() {
        return signaturesID;
    }

    public void setSignaturesID(Integer signaturesID) {
        this.signaturesID = signaturesID;
    }

    public String getCertHash() {
        return certHash;
    }

    public void setCertHash(String certHash) {
        this.certHash = certHash;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppFamily() {
        return appFamily;
    }

    public void setAppFamily(String appFamily) {
        this.appFamily = appFamily;
    }

}
