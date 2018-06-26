package vn.com.ifca.defecttracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.ifca.defecttracking.Model.Block;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 1/18/2018.
 */

public class BlockSpinAdapter extends BaseAdapter {
    Context context;
    ArrayList<Block> listBlock;
    LayoutInflater inflter;

    public BlockSpinAdapter(Context context, ArrayList<Block> listBlock) {
        this.context = context;
        this.listBlock = listBlock;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listBlock.size();
    }

    @Override
    public Object getItem(int position) {
        return listBlock.get(position);
    }

    @Override
    public long getItemId(int position) {
        Block block = listBlock.get(position);
        long a = Integer.parseInt(block.getBlockID());
        return  a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflter.inflate(R.layout.one_row_spiner,null,true);
        TextView name = (TextView) view.findViewById(R.id.tvSpinner);
        name.setText(listBlock.get(position).getBlockName());
        return view;
    }
}
