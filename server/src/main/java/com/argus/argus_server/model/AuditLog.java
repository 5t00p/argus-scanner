package com.argus.argus_server.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AuditLog")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "auditID")
    private Integer auditID;

    @Column (name = "action", length = 255)
    private String action;

    @Column (name = "'timestamp'")
    private LocalDateTime timestamp;

    @Column (name = "userID")
    private Integer userID;

    @Column (name = "caseID")
    private Integer caseID;

    public AuditLog() {
    }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
    
    public Integer getAuditID() {
        return auditID;
    }

    public void setAuditID(Integer auditID) {
        this.auditID = auditID;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }
}
