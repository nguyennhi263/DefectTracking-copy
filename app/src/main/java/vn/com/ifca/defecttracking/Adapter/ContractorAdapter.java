package vn.com.ifca.defecttracking.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import vn.com.ifca.defecttracking.Model.Contractor;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 7/2/2018.
 */

public class ContractorAdapter extends ArrayAdapter<Contractor> {
    private Activity contex;
    private List<Contractor> contractorList;


    public ContractorAdapter(Activity contex, List<Contractor> contractorList){
        super (contex , R.layout.one_row_contractor,contractorList);
        this.contex=contex;
        this.contractorList = contractorList;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = contex.getLayoutInflater();
        View listView = inflater.inflate(R.layout.one_row_contractor,null,true);

        TextView contractorName = (TextView) listView.findViewById(R.id.contractorName);
        TextView defectOpen = (TextView) listView.findViewById(R.id.defectOpen);
        TextView defectClose = (TextView) listView.findViewById(R.id.defectClose);

        Contractor contractor = contractorList.get(position);
        contractorName.setText(contractor.getContractorName());
        defectOpen.setText(contractor.getDefectOpen());
        defectClose.setText(contractor.getDefectClose());
        return listView;
    }
}
