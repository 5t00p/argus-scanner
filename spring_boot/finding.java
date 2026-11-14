package main.java.ArgusScanner.argusScanner.Model;


public class Finding{

    private Integer findingID;

    private String packageName;

    private String reason;

    private Integer riskScore;

    private Integer scanID;

    private Integer signaturesID;

    public Finding(){}

    public Finding(String packageName, String reason, Integer riskScore, Integer findingID, Integer scan, Integer signatures){
        this.packageName = packageName;
        this.reason = reason;
        this.riskScore = riskScore;
        this.findingID = findingID;
        this.scanID = scan;
        this.signaturesID = signatures;

    }
    public Integer getFindingID(){
        return findingID;
    }
    public void setFindingID(Integer findingID){
        this.findingID = findingID;
    }
    public String getPackageName(){
        return packageName;
    }
    public void setPackageName(String packageName){
        this.packageName = packageName;
    }
    public String getReason(){
        return reason;
    }
    public void setReason(String reason){
        this.reason = reason;
    }
    public Integer getRiskScore(){
        return riskScore;
    }
    public void setRiskScore(Integer riskScore){
        this.riskScore = riskScore;
    }
    public Integer getFindingScan(){
        return scanID;
    }
    public void setFindingScan(Integer scanID){
        this.scanID = scanID;
    }
    public Integer getFindingSignatures(){
        return signaturesID;
    }
    public void setFindingSignatures(Integer signaturesID){
        this.signaturesID = signaturesID;
}
}
