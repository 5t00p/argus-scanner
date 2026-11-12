package main.java.ArgusScanner.argusScanner.Model;


public class Finding{

    private Integer findingID;

    private String packageName;

    private String reason;

    private Integer riskScore;

    private Scan scan;

    private Signatures signatures;

    public Finding(){}

    public Finding(String packageName, String reason, Integer riskScore, Integer findingID, Scan scan, Signatures signatures){
        this.packageName = packageName;
        this.reason = reason;
        this.riskScore = riskScore;
        this.findingID = findingID;
        this.scan = scan;
        this.signatures = signatures;

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
        return scan;
    }
    public void setFindingScan(Scan scan){
        this.scan = scan;
    }
    public Integer getFindingSignatures(){
        return signatures;
    }
    public void setFindingSignatures(Signatures signatures){
        this.signatures = signatures;
}
}
