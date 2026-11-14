package main.java.ArgusScanner.argusScanner.Model;


public class CaseAssignment{
    private Integer userID;
    private Integer caseID;

public CaseAssignment(){}
public CaseAssignment(Integer userID, Integer caseID){
    this.userID = userID;
    this.caseID = caseID;
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
