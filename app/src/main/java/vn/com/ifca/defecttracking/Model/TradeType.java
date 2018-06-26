package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 1/18/2018.
 */

public class TradeType {
    String TradeTypeID,TradeTypeName;

    public String getTradeTypeID() {
        return TradeTypeID;
    }

    public void setTradeTypeID(String tradeTypeID) {
        TradeTypeID = tradeTypeID;
    }

    public String getTradeTypeName() {
        return TradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        TradeTypeName = tradeTypeName;
    }

    public TradeType(String tradeTypeID, String tradeTypeName) {
        TradeTypeID = tradeTypeID;
        TradeTypeName = tradeTypeName;
    }
}
