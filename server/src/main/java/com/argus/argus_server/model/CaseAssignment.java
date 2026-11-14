package com.argus.argus_server.model;
import jakarta.persistence.*;

@Entity
@Table(name = "CaseAssignment")
public class CaseAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "userID")
    private Integer userID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (case = "caseID")
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



