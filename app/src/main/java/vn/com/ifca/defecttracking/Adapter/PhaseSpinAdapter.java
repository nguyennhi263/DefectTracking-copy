package vn.com.ifca.defecttracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.ifca.defecttracking.Model.Phase;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 1/18/2018.
 */

public class PhaseSpinAdapter extends BaseAdapter {
    Context context;
    ArrayList<Phase> listPhase;
    LayoutInflater inflter;

    public PhaseSpinAdapter(Context context, ArrayList<Phase> listPhase) {
        this.context = context;
        this.listPhase = listPhase;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listPhase.size();
    }

    @Override
    public Object getItem(int position) {
        return listPhase.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        Phase phase = listPhase.get(position);
        long a = Integer.parseInt(phase.getPhaseID());
        return  a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflter.inflate(R.layout.one_row_spiner,null,true);
        TextView name = (TextView) view.findViewById(R.id.tvSpinner);

        name.setText(listPhase.get(position).getPhaseDesc());

        return view;
    }

}
