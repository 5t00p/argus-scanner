package main.java.ArgusScanner.argusScanner.Model;


public class Device{

private Integer deviceID;

private String platform;

private String model;

Integer caseID;

public Device(){}

public Device(Integer deviceID, String platform, String model, Integer caseID){

    this.deviceID = deviceID;
    this.platform = platform;
    this.model = model;
    this.caseID = caseID;
}
public Integer getdeviceID(){
    return deviceID;
}
public void setDeviceID(Integer deviceID){
    this.deviceID = deviceID;
}
public String getPlatform(){
    return platform;
}
public void setPlatform(String platform){
    this.platform = platform;
}
public String getModel(){
    return model;
}
public void setModel(String model){
    this.model = model;
}
public Integer getCaseID(){
    return caseID;
}
public void setCaseID(Integer caseID){
    this.caseID = caseID;
}
}
