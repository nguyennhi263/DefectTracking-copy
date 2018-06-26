package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 1/18/2018.
 */

public class Defect {
    String ID,
            DefectPlaceID,DefectPlaceName,
            TradeName,TradeID,
            DescriptionDetail,DescriptionID,
            DefectStatus,CloseDate,
            ImageFileNameBefore,ImageFileNameAfter, Note, ContractorName;

    public Defect() {
    }
    public Defect(String ID, String descriptionDetail, String defectStatus, String tradeName,
                  String imageFileNameBefore, String imageFileNameAfter, String note, String defectPlaceName,
                  String contractorName, String tradeID, String descriptionID) {
        this.ID = ID;
        DescriptionDetail = descriptionDetail;
        DefectStatus = defectStatus;
        TradeName = tradeName;
        ImageFileNameBefore = imageFileNameBefore;
        ImageFileNameAfter = imageFileNameAfter;
        Note = note;
        DefectPlaceName = defectPlaceName;
        ContractorName = contractorName;
        TradeID = tradeID;
        DescriptionID = descriptionID;
    }
    public Defect(String ID, String defectPlaceID, String defectPlaceName, String tradeID,
                  String tradeName, String descriptionID, String descriptionDetail,
                  String imageFileNameBefore) {
        this.ID = ID;
        DefectPlaceID = defectPlaceID;
        DefectPlaceName = defectPlaceName;
        TradeName = tradeName;
        TradeID = tradeID;
        DescriptionDetail = descriptionDetail;
        DescriptionID = descriptionID;
        ImageFileNameBefore = imageFileNameBefore;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescriptionDetail() {
        return DescriptionDetail;
    }

    public void setDescriptionDetail(String descriptionDetail) {
        DescriptionDetail = descriptionDetail;
    }

    public String getDefectStatus() {
        return DefectStatus;
    }

    public void setDefectStatus(String defectStatus) {
        DefectStatus = defectStatus;
    }

    public String getCloseDate() {
        return CloseDate;
    }

    public void setCloseDate(String closeDate) {
        CloseDate = closeDate;
    }

    public String getTradeName() {
        return TradeName;
    }

    public void setTradeName(String tradeName) {
        TradeName = tradeName;
    }

    public String getImageFileNameBefore() {
        return ImageFileNameBefore;
    }

    public void setImageFileNameBefore(String imageFileNameBefore) {
        ImageFileNameBefore = imageFileNameBefore;
    }

    public String getImageFileNameAfter() {
        return ImageFileNameAfter;
    }

    public void setImageFileNameAfter(String imageFileNameAfter) {
        ImageFileNameAfter = imageFileNameAfter;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getDefectPlaceName() {
        return DefectPlaceName;
    }

    public void setDefectPlaceName(String defectPlaceName) {
        DefectPlaceName = defectPlaceName;
    }

    public String getContractorName() {
        return ContractorName;
    }

    public void setContractorName(String contractorName) {
        ContractorName = contractorName;
    }

    public String getTradeID() {
        return TradeID;
    }

    public void setTradeID(String tradeID) {
        TradeID = tradeID;
    }

    public String getDescriptionID() {
        return DescriptionID;
    }

    public String getDefectPlaceID() {
        return DefectPlaceID;
    }

    public void setDefectPlaceID(String defectPlaceID) {
        DefectPlaceID = defectPlaceID;
    }

    public void setDescriptionID(String descriptionID) {
        DescriptionID = descriptionID;
    }

}
