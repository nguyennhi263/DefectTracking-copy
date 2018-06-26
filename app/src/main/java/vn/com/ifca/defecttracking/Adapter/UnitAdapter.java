package vn.com.ifca.defecttracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.ifca.defecttracking.Model.Unit;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 1/18/2018.
 */

public class UnitAdapter  extends BaseAdapter {
    Context context;
    ArrayList<Unit> listUnit;
    LayoutInflater inflter;

    public UnitAdapter(Context context, ArrayList<Unit> listUnit) {
        this.context = context;
        this.listUnit = listUnit;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listUnit.size();
    }

    @Override
    public Object getItem(int position) {
        return listUnit.get(position);
    }

    @Override
    public long getItemId(int position) {
        Unit unit = listUnit.get(position);
        long a = Integer.parseInt(unit.getUnitID());
        return  a;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflter.inflate(R.layout.one_row_spiner,null,true);
        TextView name = (TextView) view.findViewById(R.id.tvSpinner);
        name.setText(listUnit.get(position).getUnitNo());
        return view;
    }
}
