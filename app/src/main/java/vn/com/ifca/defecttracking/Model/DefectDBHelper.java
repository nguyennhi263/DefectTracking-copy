package vn.com.ifca.defecttracking.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nhi on 3/5/2018.
 */

public class DefectDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "DEFECT_ITEM";

    public static final String DefectItemID="DefectItemID";

    public static final String TradeID="TradeID";
    public static final String TradeName="TradeName";
    public static final String DescriptionID="DescriptionID";
    public static final String DescriptionDetail="DescriptionDetail";
    public static final String ImageFileNameBefore="ImageFileNameBefore";
    public static final String Note="Note";
    public static final String DefectPlaceID="DefectPlaceID";
    public static final String DefectPlaceName = "DefectPlaceName";
    // Database Information
    static final String DB_NAME = "IFCAMobile.DB.Defect";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "Create table " + TABLE_NAME + "("
            + DefectItemID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TradeID + " TEXT NOT NULL, "
            + TradeName  + " TEXT NOT NULL, "
            + DefectPlaceID +" TEXT NOT NULL, "
            + DefectPlaceName + " TEXT NOT NULL, "
            + DescriptionDetail + " TEXT NOT NULL, "
            + DescriptionID +" TEXT NOT NULL, "
            + ImageFileNameBefore + " TEXT NULL, "
            + Note + " TEXT "
            +" );";
    public DefectDBHelper(Context context){

        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
