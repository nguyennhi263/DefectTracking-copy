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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.com.ifca.defecttracking.Model.Defect;
import vn.com.ifca.defecttracking.Model.DefectDBManager;
import vn.com.ifca.defecttracking.R;

/**
 * Created by Nhi on 3/6/2018.
 */

public class DefectItemConfirmAdapter extends ArrayAdapter<Defect> {
private Activity contex;
private List<Defect> defectList;


public DefectItemConfirmAdapter(Activity contex, List<Defect> defectList){
        super (contex , R.layout.one_row_defect,defectList);
        this.contex=contex;
        this.defectList = defectList;
        }

@NonNull
@Override
public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = contex.getLayoutInflater();
        View listView = inflater.inflate(R.layout.one_row_defect_confirm,null,true);
        LinearLayout oneRowDefect = (LinearLayout) listView.findViewById(R.id.one_row_defect_confirm);
        TextView defectPlace = (TextView) listView.findViewById(R.id.defectPlacetxtConfirm);
        TextView tradeName = (TextView) listView.findViewById(R.id.tradeConfirm);
        TextView defectDescription = (TextView) listView.findViewById(R.id.descriptionConfirm);
        Button deleteBtn = (Button) listView.findViewById(R.id.deleteDefectItemConfirmBtn);
        ImageView imageView = (ImageView) listView.findViewById(R.id.imageConfirm);
        final Defect defect = defectList.get(position);
        if (position%2 == 0){
            oneRowDefect.setBackgroundColor(Color.LTGRAY);
        }
        if (defect.getImageFileNameBefore() != null){
               // Toast.makeText(contex,"sai",Toast.LENGTH_SHORT).show();
            imageView.setImageBitmap(StringToBitMap(defect.getImageFileNameBefore()));
        }
        String tradeNametxt = defect.getTradeName();
        String descriptiontxt = defect.getDescriptionDetail();
        String defectPlacetxt = defect.getDefectPlaceName();
        if (tradeName.length()>45){
        tradeNametxt = tradeNametxt.substring(0,45)+"...";
        }
        defectPlace.setText(defectPlacetxt);
        tradeName.setText(tradeNametxt);
        defectDescription.setText(descriptiontxt);

        final DefectDBManager defectDBManager = new DefectDBManager(contex);
        defectDBManager.open();
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defectDBManager.delete(defect.getID());
                defectList.remove(defect);
                notifyDataSetChanged();
            }
        });
        return listView;
        }
        private Bitmap StringToBitMap(String encodedString){
                try{
                        byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
                        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        return bitmap;
                }catch(Exception e){
                        e.getMessage();
                        return null;
                }
        }

@Nullable

@Override
public Defect getItem(int position) {
        return defectList.get(position);
        }

        }
