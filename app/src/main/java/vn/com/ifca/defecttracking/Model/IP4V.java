package vn.com.ifca.defecttracking.Model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Nhi on 3/29/2018.
 */

public class IP4V {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "IFCA_Defect_Tracking";
    public static final String KEY_URL ="IP";
    // Constructor
    public IP4V(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void add_KEYURL (String ip){
        editor.putString(KEY_URL,ip);
        editor.commit();
    }
    public HashMap<String, String> getIP(){
        HashMap<String, String> defect = new HashMap<String, String>();
        defect.put(KEY_URL,pref.getString(KEY_URL,null));
        return defect;
    }
    public void clear(){
        editor.clear();
        editor.commit();
    }
}
