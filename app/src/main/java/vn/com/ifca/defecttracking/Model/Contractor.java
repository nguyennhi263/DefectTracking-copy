package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 2/1/2018.
 */

public class Contractor {
    String ID, ContractorName,DefectOpen,DefectClose,ResponeTime;

    public Contractor(String ID, String contractorName) {
        this.ID = ID;
        ContractorName = contractorName;
    }

    public Contractor(String contractorName, String defectOpen, String defectClose) {
        ContractorName = contractorName;
        DefectOpen = defectOpen;
        DefectClose = defectClose;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getContractorName() {
        return ContractorName;
    }

    public void setContractorName(String contractorName) {
        ContractorName = contractorName;
    }

    public String getDefectOpen() {
        return DefectOpen;
    }

    public void setDefectOpen(String defectOpen) {
        DefectOpen = defectOpen;
    }

    public String getDefectClose() {
        return DefectClose;
    }

    public void setDefectClose(String defectClose) {
        DefectClose = defectClose;
    }

    public String getResponeTime() {
        return ResponeTime;
    }

    public void setResponeTime(String responeTime) {
        ResponeTime = responeTime;
    }
}
