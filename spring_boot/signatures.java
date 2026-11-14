package main.java.ArgusScanner.argusScanner.Model;

public class Signatures{
    private Integer signaturesID;
    private String cert_hash;
    private String package_name;
    private String type;
    private String app_family;

public Signatures(){}

public Signatures(Integer signaturesID, String cert_hash, String package_name, String type, String app_family){
    this.signaturesID = signaturesID;
    this.cert_hash = cert_hash;
    this.package_name = package_name;
    this.type = type;
    this.app_family = app_family;

}
public Integer getSignatureID(){
    return signaturesID;
}
public void setSignatureID(Integer signaturesID){
    this.signaturesID = signaturesID;
}
public String getCert_hash(){
    return cert_hash;
}
public void setCert_hash(String cert_hash){
    this.cert_hash = cert_hash;
}
public String getPackage_Name(){
    return package_name;
}
public void setPackageName(String package_name){
    this.package_name = package_name;
}
public String getType(){
    return type;
}
public void setType(String type){
    this.type = type;
}
public String getApp_family(){
    return app_family;
}
public void app_family(String app_family){
    this.app_family = app_family;
}
}


