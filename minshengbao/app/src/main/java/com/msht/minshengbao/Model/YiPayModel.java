package com.msht.minshengbao.Model;
/**
 * Demo class
 *  翼支付商户参数实体类
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/10/23 
 */
public class YiPayModel {
    private String SUBJECT;
    private String TRADENO;
    private String SERVICE;
    private String ORDERSEQ;
    private String CURTYPE;
    private String ORDERVALIDITYTIME;
    private String BACKMERCHANTURL;
    private String ORDERREQTRANSEQ;
    private String PRODUCTDESC;
    private String ORDERTIME;
    private String MERCHANTID;
    private String ORDERAMOUNT;
    private String INSTITUTIONCODE;
    private String INSTITUTIONTYPE;
    private String MERCHANT;
    private String SWTICHACC;
    private String SUBMERCHANTID;
    private String SIGN;

    private String PRODUCTAMOUNT=ORDERAMOUNT;
    private String PRODUCTID="04";
    private String ACCOUNTID="";
    private String BUSITYPE="04";
    private String SIGNTYPE="CA";
    private String ATTACHAMOUNT="0.00";
    public String getTRADENO(){
        return TRADENO;
    }
    public void setTRADENO(String TRADENO){
        this.TRADENO = TRADENO;
    }
    public String getMERCHANTID() {
        return MERCHANTID;
    }
    public void setMERCHANTID(String MERCHANTID) {
        this.MERCHANTID = MERCHANTID;
    }

    public String getSUBMERCHANTID() {
        return SUBMERCHANTID;
    }

    public void setSUBMERCHANTID(String SUBMERCHANTID) {
        this.SUBMERCHANTID = SUBMERCHANTID;
    }
    public String getORDERSEQ() {
        return ORDERSEQ;
    }

    public void setORDERSEQ(String ORDERSEQ) {
        this.ORDERSEQ = ORDERSEQ;
    }

    public String getORDERTIME() {
        return ORDERTIME;
    }

    public void setORDERTIME(String ORDERTIME) {
        this.ORDERTIME = ORDERTIME;
    }

    public String getORDERAMOUNT() {
        return ORDERAMOUNT;
    }

    public void setORDERAMOUNT(String ORDERAMOUNT) {
        this.ORDERAMOUNT = ORDERAMOUNT;
    }

    public String getORDERVALIDITYTIME() {
        return ORDERVALIDITYTIME;
    }

    public void setORDERVALIDITYTIME(String ORDERVALIDITYTIME) {
        this.ORDERVALIDITYTIME = ORDERVALIDITYTIME;
    }

    public String getPRODUCTDESC() {
        return PRODUCTDESC;
    }

    public void setPRODUCTDESC(String PRODUCTDESC) {
        this.PRODUCTDESC = PRODUCTDESC;
    }
    public String getCURTYPE() {
        return CURTYPE;
    }
    public void setCURTYPE(String CURTYPE) {
        this.CURTYPE = CURTYPE;
    }

    public String getBACKMERCHANTURL() {
        return BACKMERCHANTURL;
    }

    public void setBACKMERCHANTURL(String BACKMERCHANTURL) {
        this.BACKMERCHANTURL = BACKMERCHANTURL;
    }
    public String getORDERREQTRANSEQ() {
        return ORDERREQTRANSEQ;
    }

    public void setORDERREQTRANSEQ(String ORDERREQTRANSEQ) {
        this.ORDERREQTRANSEQ = ORDERREQTRANSEQ;
    }

    public String getSERVICE() {
        return SERVICE;
    }

    public void setSERVICE(String SERVICE) {
        this.SERVICE = SERVICE;
    }
    public String getSIGN() {
        return SIGN;
    }

    public void setSIGN(String SIGN) {
        this.SIGN = SIGN;
    }
    public String getSUBJECT() {
        return SUBJECT;
    }

    public void setSUBJECT(String SUBJECT) {
        this.SUBJECT = SUBJECT;
    }
    public String getSWTICHACC() {
        return SWTICHACC;
    }

    public void setSWTICHACC(String SWTICHACC) {
        this.SWTICHACC = SWTICHACC;
    }

    public String getINSTITUTIONCODE() {
        return INSTITUTIONCODE;
    }
    public void setINSTITUTIONCODE(String INSTITUTIONCODE){
        this.INSTITUTIONCODE=INSTITUTIONCODE;
    }

    public String getMERCHANT() {
        return MERCHANT;
    }
    public void setMERCHANT(String MERCHANT){
        this.MERCHANT=MERCHANT;
    }
    public String getINSTITUTIONTYPE(){
        return INSTITUTIONTYPE;
    }
    public void setINSTITUTIONTYPE(String  INSTITUTIONTYPE){
        this.INSTITUTIONTYPE=INSTITUTIONTYPE;
    }
    public String getPRODUCTAMOUNT() {
        return ORDERAMOUNT;
    }
    public void setPRODUCTAMOUNT(String PRODUCTAMOUNT) {
        this.ORDERAMOUNT = PRODUCTAMOUNT;
    }
    public String getPRODUCTID() {
        return PRODUCTID;
    }
    public void setPRODUCTID(String PRODUCTID) {
        this.PRODUCTID = PRODUCTID;
    }
    public String getACCOUNTID() {
        return ACCOUNTID;
    }
    public void setACCOUNTID(String ACCOUNTID) {
        this.ACCOUNTID = ACCOUNTID;
    }

    public String getBUSITYPE() {
        return BUSITYPE;
    }
    public void setBUSITYPE(String BUSITYPE) {
        this.BUSITYPE = BUSITYPE;
    }
    public String getSIGNTYPE() {
        return SIGNTYPE;
    }
    public void setSIGNTYPE(String SIGNTYPE) {
        this.SIGNTYPE = SIGNTYPE;
    }
    public String getATTACHAMOUNT() {
        return ATTACHAMOUNT;
    }
    public void setATTACHAMOUNT(String ATTACHAMOUNT) {
        this.ATTACHAMOUNT = ATTACHAMOUNT;
    }
    /*
    *废弃
    public String getMERCHANTPWD() {
        return MERCHANTPWD;
    }
    public void setMERCHANTPWD(String MERCHANTPWD) {
        this.MERCHANTPWD = MERCHANTPWD;
    }
    public String getCUSTOMERID() {
        return CUSTOMERID;
    }
    public void setCUSTOMERID(String CUSTOMERID) {
        this.CUSTOMERID = CUSTOMERID;
    }
     public String getOTHERFLOW() {
        return OTHERFLOW;
    }
    public void setOTHERFLOW(String OTHERFLOW) {
        this.OTHERFLOW = OTHERFLOW;
    }
    public String getUSERIP() {
        return USERIP;
    }
    public void setUSERIP(String USERIP) {
        this.USERIP = USERIP;
    }
    public String getDIVDETAILS() {
        return DIVDETAILS;
    }
    public void setDIVDETAILS(String DIVDETAILS) {
        this.DIVDETAILS = DIVDETAILS;
    }
     public String getATTACH() {
        return ATTACH;
    }
    public void setATTACH(String ATTACH) {
        this.ATTACH = ATTACH;
    }
    */
}
