package vn.com.ifca.defecttracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.ifca.defecttracking.Model.TradeType;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 1/18/2018.
 */

public class TradeTypeSpinAdapter extends BaseAdapter {
    Context context;
    ArrayList<TradeType> listTradeType;
    LayoutInflater inflter;

    public TradeTypeSpinAdapter(Context context, ArrayList<TradeType> listTradeType) {
        this.context = context;
        this.listTradeType = listTradeType;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listTradeType.size();
    }

    @Override
    public Object getItem(int position) {
        return listTradeType.get(position);
    }

    @Override
    public long getItemId(int position) {
        TradeType tradeType= listTradeType.get(position);
        long a = Integer.parseInt(tradeType.getTradeTypeID());
        return  a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflter.inflate(R.layout.one_row_spiner,null,true);
        TextView name = (TextView) view.findViewById(R.id.tvSpinner);
        name.setText(listTradeType.get(position).getTradeTypeName());
        return view;
    }
}
