package main.java.ArgusScanner.argusScanner.Model;
import java.time.LocalDateTime;


public class Report{
    private Integer reportID;
    private LocalDateTime generated_at;
    private Integer scanID;

public Report(){
}  

public Report(Integer reportID, LocalDateTime generated_at, Integer scanID){
    this.reportID = reportID;
    this.generated_at = generated_at;
    this.scanID = scanID;
}
public Integer getReportID(){
    return reportID;
}
public void setReportID(Integer reportID){
    this.reportID = reportID;
}
public LocalDateTime getDateTime(){
    return generated_at;
}
public void setDateTime(){
    this.generated_at = generated_at;
}
public Integer getScanID(){
    return scanID;
}
public void setScanID(Integer scanID){
    this.scanID = scanID;
}
}
