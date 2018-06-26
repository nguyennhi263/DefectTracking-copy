package vn.com.ifca.defecttracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.ifca.defecttracking.Model.Phase;
import vn.com.ifca.defecttracking.Model.Project;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 1/25/2018.
 */

public class ProjectSpinAdapter extends BaseAdapter {
    Context context;
    ArrayList<Project> listProject;
    LayoutInflater inflter;

    public ProjectSpinAdapter(Context context, ArrayList<Project> listProject) {
        this.context = context;
        this.listProject = listProject;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listProject.size();
    }

    @Override
    public Object getItem(int position) {
        return listProject.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
       Project project = listProject.get(position);
        long a = Integer.parseInt(project.getProjectID());
        return  a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflter.inflate(R.layout.one_row_spiner,null,true);
        TextView name = (TextView) view.findViewById(R.id.tvSpinner);

        name.setText(listProject.get(position).getProjectName());

        return view;
    }

}
