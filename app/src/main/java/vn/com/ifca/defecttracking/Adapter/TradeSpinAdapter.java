package vn.com.ifca.defecttracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.ifca.defecttracking.Model.Trade;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 1/18/2018.
 */

public class TradeSpinAdapter extends BaseAdapter {
    Context context;
    ArrayList<Trade> listTrade;
    LayoutInflater inflter;

    public TradeSpinAdapter(Context context, ArrayList<Trade> listTrade) {
        this.context = context;
        this.listTrade = listTrade;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listTrade.size();
    }

    @Override
    public Object getItem(int position) {
        return listTrade.get(position);
    }

    @Override
    public long getItemId(int position) {
        Trade  trade= listTrade.get(position);
        long a = Integer.parseInt(trade.getTradeID());
        return  a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflter.inflate(R.layout.one_row_spiner,null,true);
        TextView name = (TextView) view.findViewById(R.id.tvSpinner);
        name.setText(listTrade.get(position).getTradeName());
        return view;
    }
}
