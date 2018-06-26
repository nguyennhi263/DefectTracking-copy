package vn.com.ifca.defecttracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.ifca.defecttracking.Model.Description;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 1/18/2018.
 */

public class DescriptionSpinAdapter extends BaseAdapter {
    Context context;
    ArrayList<Description> listDesc;
    LayoutInflater inflter;

    public DescriptionSpinAdapter(Context context, ArrayList<Description> listDesc) {
        this.context = context;
        this.listDesc = listDesc;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listDesc.size();
    }

    @Override
    public Object getItem(int position) {
        return listDesc.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflter.inflate(R.layout.one_row_spiner,null,true);
        TextView name = (TextView) view.findViewById(R.id.tvSpinner);
        name.setText(listDesc.get(position).getDetail().toString());
        return view;
    }
}
