package com.argus.argus_server.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportID")
    private Integer reportID;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "scanID", unique = true)
    private Integer scanID;

    public Report() {
    }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
    public Integer getReportID() {
        return reportID;
    }
    public void setReportID(Integer reportID) {
        this.reportID = reportID;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public Integer getScanID() {
        return scanID;
    }
    public void setScanID(Integer scanID) {
        this.scanID = scanID;
    }
    
}
