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

    /** updateStatus() from the class diagram. Status can only be "open" or "closed". */
    public void updateStatus(String newStatus) {
        if (!"open".equals(newStatus) && !"closed".equals(newStatus)) {
            throw new IllegalArgumentException("Status must be \"open\" or \"closed\"");
        }
        this.status = newStatus;
    }

    public void changeSurvivorRef(String reference) {
        if (reference == null || reference.isBlank()) {
            throw new IllegalArgumentException("Survivor reference is required");
        }
        if (reference.trim().length() > 100) {
            throw new IllegalArgumentException("Survivor reference must be 100 characters or less");
        }
        this.survivorRef = reference.trim();
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