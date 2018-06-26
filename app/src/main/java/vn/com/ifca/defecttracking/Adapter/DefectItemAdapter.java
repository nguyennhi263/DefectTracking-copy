package vn.com.ifca.defecttracking.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.com.ifca.defecttracking.Activities.DefectDetailActivity;
import vn.com.ifca.defecttracking.Model.Defect;
import vn.com.ifca.defecttracking.Model.DefectItem;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 1/18/2018.
 */

public class DefectItemAdapter  extends ArrayAdapter<Defect> {
    private Activity contex;
    private List<Defect> defectList;
    DefectItem defectItem ;

    public DefectItemAdapter(Activity contex, List<Defect> defectList){
        super (contex , R.layout.one_row_defect,defectList);
        this.contex=contex;
        this.defectList = defectList;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = contex.getLayoutInflater();
        View listView = inflater.inflate(R.layout.one_row_defect,null,true);
        LinearLayout oneRowDefect = (LinearLayout) listView.findViewById(R.id.oneRowDefect);
        Button defectItemStatusBtn = (Button) listView.findViewById(R.id.defectItemStatus);
        TextView defectPlacetxt = (TextView) listView.findViewById(R.id.defectPlacetxt);
        TextView defectTypetxt = (TextView) listView.findViewById(R.id.defectTypetxt);
        final Defect defect = defectList.get(position);
        if (position%2==0){
            oneRowDefect.setBackgroundColor(Color.LTGRAY);
        }
        String tradeName = defect.getTradeName();
        String descrip = defect.getDescriptionDetail();
        String defectPlace = defect.getDefectPlaceName();
        if (tradeName.length()>45){
            tradeName = tradeName.substring(0,45)+"...";
        }
        if (descrip.length()>30){
            descrip = descrip.substring(0,30)+"...";
        }
        if (defectPlace.length()>30){
            defectPlace = defectPlace.substring(0,30)+"..";
        }
        if (defect.getDefectStatus().equals("1")){
            defectItemStatusBtn.setBackgroundColor(Color.RED);
        }else if (defect.getDefectStatus().equals("2")){
            defectItemStatusBtn.setBackgroundColor(Color.GREEN);
        }


        oneRowDefect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defectItem = new DefectItem(contex);
                defectItem.createDefectItem( defect.getID(), defect.getDefectPlaceName(),defect.getTradeName(),
                        defect.getDescriptionDetail(),
                        defect.getImageFileNameBefore(), defect.getDefectStatus(),
                        defect.getNote(),defect.getContractorName(), defect.getImageFileNameAfter(),
                        defect.getTradeID(),defect.getDescriptionID());
                Intent it = new Intent(contex,DefectDetailActivity.class);
                contex.startActivity(it);
            }
        });
        defectPlacetxt.setText(defectPlace);
        defectTypetxt.setText(tradeName);
        return listView;
    }

    @Nullable
    @Override
    public Defect getItem(int position) {
        return defectList.get(position);
    }

}
