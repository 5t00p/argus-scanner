package main.java.ArgusScanner.argusScanner.Model;
import java.time.LocalDateTime;


public class AuditLog{
    private Integer auditID;
    private String action;
    private LocalDateTime timeStamp;
    private Integer userID;
    private Integer caseID;

public AuditLog(){}

public AuditLog(Integer auditID, String action, LocalDateTime timeStamp, Integer userID, Integer caseID){
this.auditID = auditID;
this.action = action;
this.timeStamp = timeStamp;
this.userID = userID;
this.caseID = caseID;
}

public Integer getAuditID(){
    return auditID;
}
public void setAuditID(Integer auditID){
    this.auditID = auditID;
}
public String getAction(){
    return action;
}
public void setAction(String action){
    this.action = action;
}
public LocalDateTime getDateTime(){
    return timeStamp;
}
public void setDateTime(LocalDateTime timeStamp){
    this.timeStamp = timeStamp;
}
public Integer getUserID(){
    return userID;
}
public void setUserID(Integer userID){
    this.userID = userID;
}
public Integer getCaseID(){
    return caseID;
}
public void setCaseID(Integer caseID){
    this.caseID = caseID;
}
}
