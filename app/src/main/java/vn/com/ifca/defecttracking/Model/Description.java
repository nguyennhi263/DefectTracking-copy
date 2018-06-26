package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 1/18/2018.
 */

public class Description {
    private String DescriptionID,Detail;

    public Description(String descriptionID, String detail) {
        DescriptionID = descriptionID;
        Detail = detail;
    }

    public String getDescriptionID() {
        return DescriptionID;
    }

    public void setDescriptionID(String descriptionID) {
        DescriptionID = descriptionID;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }
}
