package com.argus.argus_server.model;
import jakarta.persistence.*;

@Entity
@Table(name = "CaseAssignment")
@IdClass(CaseAssignmentId.class)          // 
public class CaseAssignment {

    @Id
    @Column (name = "userID")             // ← removed @GeneratedValue
    private Integer userID;

    @Id
    @Column (name = "caseID")             // ← was (case = ...), removed @GeneratedValue
    private Integer caseID;

    public CaseAssignment() {
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



