package com.argus.argus_server.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VCase")
public class VictimCase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "caseID")
    private Integer caseID;

    @Column (name = "survivor_ref", length = 100)
    private String survivorRef;

    @Column (name = "opened_at")
    private LocalDateTime openedAt;

    @Column (name = "status", length = 50)
    private String status;

    public VictimCase() {
    }

    public Integer getCaseID() {
        return caseID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public String getSurvivorRef() {
        return survivorRef;
    }

    public void setSurvivorRef(String survivorRef) {
        this.survivorRef = survivorRef;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}