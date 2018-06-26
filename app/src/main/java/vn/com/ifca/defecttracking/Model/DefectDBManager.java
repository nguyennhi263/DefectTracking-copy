package vn.com.ifca.defecttracking.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Nhi on 3/5/2018.
 */

public class DefectDBManager {
    private DefectDBHelper defectDBHelper;

    private android.content.Context Context;

    private SQLiteDatabase database;

    public DefectDBManager(Context context) {
        Context = context;
    }

    public DefectDBManager open() throws SQLException {
        defectDBHelper = new DefectDBHelper(Context);
        database = defectDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        defectDBHelper.close();
    }

    public void insert(String defecetPlaceID,String defectPlaceName,String tradeID, String tradeName,String descriptionID, String descriptionDetail) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DefectDBHelper.DefectPlaceID, defecetPlaceID);
        contentValue.put(DefectDBHelper.DefectPlaceName, defectPlaceName);
        contentValue.put(DefectDBHelper.TradeID,tradeID);
        contentValue.put(DefectDBHelper.TradeName,tradeName);
        contentValue.put(DefectDBHelper.DescriptionID,descriptionID);
        contentValue.put(DefectDBHelper.DescriptionDetail,descriptionDetail);

        database.insert(DefectDBHelper.TABLE_NAME, null, contentValue);
    }
    public long getIDinserted(String defecetPlaceID,String defectPlaceName,String tradeID, String tradeName,String descriptionID, String descriptionDetail) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DefectDBHelper.DefectPlaceID, defecetPlaceID);
        contentValue.put(DefectDBHelper.DefectPlaceName, defectPlaceName);
        contentValue.put(DefectDBHelper.TradeID,tradeID);
        contentValue.put(DefectDBHelper.TradeName,tradeName);
        contentValue.put(DefectDBHelper.DescriptionID,descriptionID);
        contentValue.put(DefectDBHelper.DescriptionDetail,descriptionDetail);

        long id = database.insert(DefectDBHelper.TABLE_NAME, null, contentValue);
        return id;
    }
    public void update(long defectID,String imageText){
        ContentValues cv = new ContentValues();
        cv.put(DefectDBHelper.ImageFileNameBefore,imageText);
        database.update(DefectDBHelper.TABLE_NAME,cv,DefectDBHelper.DefectItemID + "=" +defectID, null);
    }
    public void delete(String defectID) {
        database.delete(DefectDBHelper.TABLE_NAME, DefectDBHelper.DefectItemID + "=" +defectID, null);

    }
    public void deleteAll(){
        database.delete(DefectDBHelper.TABLE_NAME, null, null);
        //database.execSQL("DROP TABLE IF EXISTS " + DefectDBHelper.TABLE_NAME ) ;
        database.execSQL("UPDATE sqlite_sequence SET seq = (SELECT MAX("+DefectDBHelper.DefectItemID+") FROM "+DefectDBHelper.TABLE_NAME +") WHERE name = '"+DefectDBHelper.TABLE_NAME+"'");
    }
    public ArrayList<Defect> getValues() {
        ArrayList<Defect> values = new ArrayList<Defect>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + defectDBHelper.TABLE_NAME, null);
        if(cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(defectDBHelper.DefectItemID));
                String defectPlaceID = cursor.getString(cursor.getColumnIndex(defectDBHelper.DefectPlaceID));
                String defettPlaceName = cursor.getString(cursor.getColumnIndex(defectDBHelper.DefectPlaceName));
                String tradeID = cursor.getString(cursor.getColumnIndex(defectDBHelper.TradeID));
                String tradeName = cursor.getString(cursor.getColumnIndex(defectDBHelper.TradeName));
                String descriptionID = cursor.getString(cursor.getColumnIndex(defectDBHelper.DescriptionID));
                String descriptionDetail =  cursor.getString(cursor.getColumnIndex(defectDBHelper.DescriptionDetail));
                String imageBefore = cursor.getString(cursor.getColumnIndex(defectDBHelper.ImageFileNameBefore));

                Defect defect = new Defect(id,defectPlaceID,defettPlaceName,tradeID,tradeName,descriptionID,descriptionDetail,imageBefore);
                values.add(defect);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return values;
    }
}
