package vn.com.ifca.defecttracking.Model;

/**
 * Created by Nhi on 1/18/2018.
 */

public class Trade {
    private  String TradeID,TradeName;

    public Trade(String tradeID, String tradeName) {
        TradeID = tradeID;
        TradeName = tradeName;
    }

    public String getTradeID() {
        return TradeID;
    }

    public void setTradeID(String tradeID) {
        TradeID = tradeID;
    }

    public String getTradeName() {
        return TradeName;
    }

    public void setTradeName(String tradeName) {
        TradeName = tradeName;
    }
}
