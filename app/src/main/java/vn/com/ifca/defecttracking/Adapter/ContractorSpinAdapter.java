package vn.com.ifca.defecttracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.ifca.defecttracking.Model.Contractor;

import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 2/1/2018.
 */

public class ContractorSpinAdapter extends BaseAdapter {
    Context context;
    ArrayList<Contractor> listContractor;
    LayoutInflater inflter;

    public ContractorSpinAdapter(Context context, ArrayList<Contractor> listContractor) {
        this.context = context;
        this.listContractor = listContractor;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listContractor.size();
    }

    @Override
    public Object getItem(int position) {
        return listContractor.get(position);
    }

    @Override
    public long getItemId(int position) {
        Contractor contractor= listContractor.get(position);
        long a = Integer.parseInt(contractor.getID());
        return  a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflter.inflate(R.layout.one_row_spiner,null,true);
        TextView name = (TextView) view.findViewById(R.id.tvSpinner);
        name.setText(listContractor.get(position).getContractorName());
        return view;
    }
}
