package vn.com.ifca.defecttracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import vn.com.ifca.defecttracking.Model.DefectPlace;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 1/30/2018.
 */

public class DefectPlaceSpinAdapter extends BaseAdapter {
    Context context;
    ArrayList<DefectPlace> listPlace;
    LayoutInflater inflter;

    public DefectPlaceSpinAdapter(Context context, ArrayList<DefectPlace> listPlace) {
        this.context = context;
        this.listPlace = listPlace;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listPlace.size();
    }

    @Override
    public Object getItem(int position) {
        return listPlace.get(position);
    }

    @Override
    public long getItemId(int position) {
        DefectPlace place = listPlace.get(position);
        long a = Integer.parseInt(place.getID());
        return  a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflter.inflate(R.layout.one_row_spiner,null,true);
        TextView name = (TextView) view.findViewById(R.id.tvSpinner);
        name.setText(listPlace.get(position).getDefectPlaceName());
        return view;
    }
}
