package vn.com.ifca.defecttracking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.Buffer;
import java.util.ArrayList;

import vn.com.ifca.defecttracking.Activities.DefectListActivity;
import vn.com.ifca.defecttracking.Model.DefectHeader;
import vn.com.ifca.defecttracking.Model.Unit;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 2/5/2018.
 */

public class DefectHeaderAdapter extends BaseAdapter {
    Context context;
    ArrayList<DefectHeader> listDefectHeader, listTemp;
    LayoutInflater inflter;

    public DefectHeaderAdapter(Context context, ArrayList<DefectHeader> listDefectHeader) {
        this.context = context;
        this.listDefectHeader = listDefectHeader;
        this.listTemp = listDefectHeader;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listTemp.size();
    }

    @Override
    public Object getItem(int position) {
        return listTemp.get(position);
    }

    @Override
    public long getItemId(int position) {
        DefectHeader defectHeader = listTemp.get(position);
        long a = Integer.parseInt(defectHeader.getID());
        return a;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflter.inflate(R.layout.one_row_defect_header, null, true);
        LinearLayout oneRowDefectHeader = (LinearLayout) view.findViewById(R.id.oneRowDfHeader);
        TextView unitNo = (TextView) view.findViewById(R.id.txtUnitNo);
        TextView txtCraeteDefectDate = (TextView) view.findViewById(R.id.txtCraeteDefectDate);
        Button statusColor = (Button) view.findViewById(R.id.statusColor);
        final DefectHeader defectHeader = listTemp.get(position);
        if (position % 2 == 0) {
            oneRowDefectHeader.setBackgroundColor(Color.LTGRAY);
        }
        unitNo.setText(defectHeader.getUnitNo());
        txtCraeteDefectDate.setText(defectHeader.getCreateDate());
        String recordStatus = defectHeader.getRecordStatus();
        if (recordStatus.equals("Open")) {
            // low
            statusColor.setBackgroundColor(Color.WHITE);
        } else if (recordStatus.equals("Close")) {
            // medium
            statusColor.setBackgroundColor(Color.DKGRAY);
        }
        oneRowDefectHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itent = new Intent(context, DefectListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("DefectHeaderLocation",defectHeader.getLocationDetail()+"->"+defectHeader.getUnitNo());
                bundle.putString("DefectHeaderID", defectHeader.getID());
                itent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                itent.putExtras(bundle);
                context.startActivity(itent);
            }
        });
        return view;
    }
    public Filter getFilter() {
        ValueFilter valueFilter = new ValueFilter();
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<DefectHeader> filterList = new ArrayList<DefectHeader>();
                for (int i = 0; i < listDefectHeader.size(); i++) {
                    DefectHeader cur = listDefectHeader.get(i);
                    if (cur.getUnitNo().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(cur);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = listDefectHeader.size();
                results.values = listDefectHeader;
            }
            return results;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listTemp = (ArrayList<DefectHeader>) results.values;
            notifyDataSetChanged();
        }
    }
}