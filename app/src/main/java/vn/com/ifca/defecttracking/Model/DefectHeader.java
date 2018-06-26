package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 2/2/2018.
 */

public class DefectHeader {
       String ID,UnitID,UnitNo,CreateDate,RecordStatus,LocationDetail;

    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String unitID) {
        UnitID = unitID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getRecordStatus() {
        return RecordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        RecordStatus = recordStatus;
    }

    public String getLocationDetail() {
        return LocationDetail;
    }

    public void setLocationDetail(String locationDetail) {
        this.LocationDetail = locationDetail;
    }

    public DefectHeader(String ID, String unitID, String unitNo, String createDate, String recordStatus, String locationDetail) {
        this.ID = ID;
        UnitNo = unitNo;
        UnitID = unitID;
        CreateDate = createDate;
        RecordStatus = recordStatus;
        LocationDetail = locationDetail;
    }
}
