
package com.argus.argus_server.service;

import com.argus.argus_server.model.CaseAssignment;
import com.argus.argus_server.model.CaseAssignmentId;
import com.argus.argus_server.repository.CaseAssignmentRepository;
import com.argus.argus_server.repository.AppUserRepository;
import com.argus.argus_server.repository.VictimCaseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CaseAssignmentService {

    private final CaseAssignmentRepository assignmentRepository;
    private final AppUserRepository userRepository;
    private final VictimCaseRepository caseRepository;

    public CaseAssignmentService(CaseAssignmentRepository assignmentRepository, AppUserRepository userRepository, VictimCaseRepository caseRepository) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.caseRepository = caseRepository;
    }

    @Transactional
    public CaseAssignment reassign(Integer oldUserID, Integer oldCaseID, Integer newUserID, Integer newCaseID) {

        CaseAssignmentId oldId = new CaseAssignmentId(oldUserID, oldCaseID);

        if (!assignmentRepository.existsById(oldId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Original assignment not found");
        }

        if (newUserID == null && newCaseID == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No reassignment specified");
        }

        Integer targetUserID = newUserID != null ? newUserID : oldUserID;

        Integer targetCaseID = newCaseID != null ? newCaseID : oldCaseID;

        if (targetUserID.equals(oldUserID) && targetCaseID.equals(oldCaseID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignment is unchanged");
        }

        if (!userRepository.existsById(targetUserID) || !caseRepository.existsById(targetCaseID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target user or case does not exist");
        }

        CaseAssignmentId newId = new CaseAssignmentId(targetUserID, targetCaseID);

        if (assignmentRepository.existsById(newId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Assignment already exists");
        }

        assignmentRepository.deleteById(oldId);
        assignmentRepository.flush();

        CaseAssignment replacement = new CaseAssignment();
        replacement.setUserID(targetUserID);
        replacement.setCaseID(targetCaseID);

        return assignmentRepository.saveAndFlush(replacement);
    }
}
