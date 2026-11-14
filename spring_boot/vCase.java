package main.java.ArgusScanner.argusScanner.Model;
import java.time.LocalDateTime;

public class vCase{
    private Integer caseID;
    private String survivorRef;
    private LocalDateTime openedAt;
    private String status;

public vCase(){}
public vCase(Integer caseID, String survivorRef, LocalDateTime openedAt, String status){
    this.caseID = caseID;
    this.survivorRef = survivorRef;
    this.openedAt = openedAt;
    this.status = status;
}
public Integer getCaseID(){
    return caseID;
}
public void setCaseID(Integer caseID){
    this.caseID = caseID;
}
public String getSurvivorRef(){
    return survivorRef;
}
public void setSurvivorRef(String survivorRef){
    this.survivorRef = survivorRef;
}
public LocalDateTime getOpenedAt(){
    return openedAt;
}
public void setOpenedAt(LocalDateTime openedAt){
    this.openedAt = openedAt;
}
public String getStatus(){
    return status;
}
public void setStatus(String status){
    this.status = status;
}
}
