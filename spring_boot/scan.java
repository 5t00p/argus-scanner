package main.java.ArgusScanner.argusScanner.Model;
import java.time.LocalDateTime;

public class Scan{
    private Integer reportID;
    private LocalDateTime generated_at;
    private Integer scanID;

public Scan(){}

public Scan(Integer reportID, LocalDateTime generated_at, Integer scanID){
    this.reportID = reportID;
    this.generated_at = generated_at;
    this.scanID = scanID;
}
public Integer getReportID(Integer reportID){
    return reportID;
}
public void setReportID(Integer reportID){
    this.reportID = reportID;
}
public LocalDateTime getDateTime(){
    return generated_at;
}
public void setDateTime(LocalDateTime generated_at){
    this.generated_at = generated_at;
}
public Integer getScanID(){
    return scanID;
}
public void setScanID(Integer scanID){
    this.scanID = scanID;
}
}
