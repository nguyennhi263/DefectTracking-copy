package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 1/18/2018.
 */

public class Unit {
    String UnitID, BlockID, UnitNo, Floor;

    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String unitID) {
        UnitID = unitID;
    }

    public String getBlockID() {
        return BlockID;
    }

    public void setBlockID(String blockID) {
        BlockID = blockID;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public Unit(String unitID, String unitNo, String floor) {
        UnitID = unitID;
        UnitNo = unitNo;
        Floor = floor;
    }
}
