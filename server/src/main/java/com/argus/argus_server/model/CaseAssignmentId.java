package com.argus.argus_server.model;

import java.io.Serializable;
import java.util.Objects;

public class CaseAssignmentId implements Serializable {

    private Integer userID;
    private Integer caseID;

    public CaseAssignmentId() {
    }

    public CaseAssignmentId(Integer userID, Integer caseID) {
        this.userID = userID;
        this.caseID = caseID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof CaseAssignmentId)) {
            return false;
        }

        CaseAssignmentId other = (CaseAssignmentId) obj;

        return Objects.equals(userID, other.userID)
                && Objects.equals(caseID, other.caseID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, caseID);
    }
}
