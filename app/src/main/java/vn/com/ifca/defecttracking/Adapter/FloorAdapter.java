package vn.com.ifca.defecttracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.ifca.defecttracking.Model.Floor;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 6/28/2018.
 */

public class FloorAdapter extends BaseAdapter {
    Context context;
    ArrayList<Floor> listFloor;
    LayoutInflater inflter;

    public FloorAdapter(Context context, ArrayList<Floor> listFloor) {
        this.context = context;
        this.listFloor = listFloor;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listFloor.size();
    }

    @Override
    public Object getItem(int position) {
        return listFloor.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflter.inflate(R.layout.one_row_spiner,null,true);
        TextView name = (TextView) view.findViewById(R.id.tvSpinner);
        name.setText(listFloor.get(position).getFloorID());
        return view;
    }
}
