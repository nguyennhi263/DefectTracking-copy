package vn.com.ifca.defecttracking.Model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Nhi on 1/18/2018.
 */

public class DefectItem {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "DefectItem";
    public static final String KEY_LOCATON = "";
    public static final String KEY_DEFECTID= "DEFECTID";
    public static final String KEY_UNIT= "UNIT";
    public static final String KEY_DESCRIPTION = "DESCRIPTION";
    public static final String KEY_UNITID = "UNITID";
    public static final String KEY_DESCRIPTIONID = "DESCRIPTIONID";
    public static final String KEY_TRADE = "TRADE";
    public static final String KEY_TRADEID = "TRADEID";
    public static final String KEY_IMAGETEXT = "IMAGETEXT";
    public static final String KEY_IMAGEBEFORE = "IMAGEBEFORE";
    public static final String KEY_STATUS = "STATUS";
    public static final String KEY_NOTE = "NOTE";
    public static final String KEY_CONTRACTOR = "ASSIGN";
    public static final String KEY_DATECREATE = "DATECREATE";
    public static final String KEY_IMAGEAFTER = "IMAGEAFTER";
    public static final String KEY_DEFECTPLACE = "DEFECTPLACE";

    // Constructor
    public DefectItem(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */

    public void createDefectItem(String defectID,String defectPlace, String trade, String description,
                                 String imageBefore, String status, String note, String assign, String imageAfter, String tradeID, String descriptionID){
        editor.putString(KEY_DEFECTID,defectID);
        editor.putString(KEY_DESCRIPTION, description);
        editor.putString(KEY_TRADE,trade);
        editor.putString(KEY_TRADEID,tradeID);
        editor.putString(KEY_DESCRIPTIONID, descriptionID);
        editor.putString(KEY_IMAGEBEFORE,imageBefore);
        editor.putString(KEY_STATUS,status);
        editor.putString(KEY_NOTE,note);
        editor.putString(KEY_CONTRACTOR,assign);
        editor.putString(KEY_IMAGEAFTER,imageAfter);
        editor.putString(KEY_DEFECTPLACE,defectPlace);
        editor.commit();
    }
    public void addLocation (String location){
        editor.putString(KEY_LOCATON,location);
        editor.commit();
    }
    public void addUnit(String unitID, String unitName){
        editor.putString(KEY_UNITID,unitID);
        editor.putString(KEY_UNIT,unitName);
        editor.commit();
    }
    public void addTrade(String tradeID, String trade, String defectPlace){
        editor.putString(KEY_TRADEID,tradeID);
        editor.putString(KEY_TRADE,trade);
        editor.putString(KEY_DEFECTPLACE,defectPlace);
        editor.commit();
    }
    public void addDescription(String descriptionID, String description){
        editor.putString(KEY_DESCRIPTIONID,descriptionID);
        editor.putString(KEY_DESCRIPTION,description);
        editor.commit();
    }
    public void addImage(String imageText){
        editor.putString(KEY_IMAGETEXT,imageText);
        editor.commit();
    }
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getDefectDetail(){
        HashMap<String, String> defect = new HashMap<String, String>();

        defect.put(KEY_DEFECTID,pref.getString(KEY_DEFECTID,null));
        defect.put(KEY_UNIT, pref.getString(KEY_UNIT, null));
        defect.put(KEY_UNITID, pref.getString(KEY_UNITID, null));
        defect.put(KEY_DESCRIPTION, pref.getString(KEY_DESCRIPTION, null));
        defect.put(KEY_DESCRIPTIONID, pref.getString(KEY_DESCRIPTIONID, null));
        defect.put(KEY_TRADE, pref.getString(KEY_TRADE,null));
        defect.put(KEY_TRADEID, pref.getString(KEY_TRADEID,null));
        defect.put(KEY_IMAGETEXT, pref.getString(KEY_IMAGETEXT,null));
        defect.put(KEY_IMAGEBEFORE,pref.getString(KEY_IMAGEBEFORE,null));
        defect.put(KEY_STATUS,pref.getString(KEY_STATUS,null));
        defect.put(KEY_NOTE,pref.getString(KEY_NOTE,null));
        defect.put(KEY_CONTRACTOR,pref.getString(KEY_CONTRACTOR,null));
        defect.put(KEY_DATECREATE,pref.getString(KEY_DATECREATE,null));
        defect.put(KEY_IMAGEAFTER,pref.getString(KEY_IMAGEAFTER,null));
        defect.put(KEY_DEFECTPLACE,pref.getString(KEY_DEFECTPLACE,null));
        defect.put(KEY_LOCATON,pref.getString(KEY_LOCATON,null));
        return defect;
    }


    public void clear(){
        editor.clear();
        editor.commit();
    }
}
