package vn.com.ifca.defecttracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.ifca.defecttracking.Model.User;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 1/18/2018.
 */

public class UserSpinAdapter extends BaseAdapter {
    Context context;
    ArrayList<User> listUser;
    LayoutInflater inflter;

    public UserSpinAdapter(Context context, ArrayList<User> listUser) {
        this.context = context;
        this.listUser = listUser;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listUser.size();
    }

    @Override
    public Object getItem(int position) {
        return listUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        User user= listUser.get(position);
        long a = Integer.parseInt(user.getUserId());
        return  a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflter.inflate(R.layout.one_row_spiner,null,true);
        TextView name = (TextView) view.findViewById(R.id.tvSpinner);
        name.setText(listUser.get(position).getFullname());
        return view;
    }
}
