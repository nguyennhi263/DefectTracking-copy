package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 1/30/2018.
 */

public class DefectPlace {
    String ID, DefectPlaceName;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDefectPlaceName() {
        return DefectPlaceName;
    }

    public void setDefectPlaceName(String defectPlaceName) {
        DefectPlaceName = defectPlaceName;
    }

    public DefectPlace(String ID, String defectPlaceName) {
        this.ID = ID;
        DefectPlaceName = defectPlaceName;
    }
}
