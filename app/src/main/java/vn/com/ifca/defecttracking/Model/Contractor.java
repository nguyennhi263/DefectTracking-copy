package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 2/1/2018.
 */

public class Contractor {
    String ID, ContractorName;

    public Contractor(String ID, String contractorName) {
        this.ID = ID;
        ContractorName = contractorName;
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
}
