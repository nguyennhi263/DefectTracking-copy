package vn.com.ifca.defecttracking.Model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.com.ifca.defecttracking.Activities.LogInActivity;

/**
 * Created by Nhi on 1/18/2018.
 */

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    //private static final String PREF_NAME = "AndroidHivePref";
    private static final String PREF_NAME = "Defect Tracking";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // User pass (make variable public to access from outside)
    public static final String KEY_PASS = "password";

    // User full name (make variable public to access from outside)
    public static final String KEY_FULLNAME = "fullname";

    // User phone number (make variable public to access from outside)
    public static final String KEY_PHONE = "phonenumber";


    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Level account
    public static  final String KEY_LEVEL="level";

    // UserID
    public static final String KEY_ID="userid";
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, String pass, String id,String level){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_PASS, pass);

        // Storing level in pref
        editor.putString(KEY_LEVEL,level);
        // Storing id in pref
        editor.putString(KEY_ID,id);

        // commit changes
        editor.commit();
    }
    public void createLoginSession(String id,String name, String pass, String fullname,String level,String email,String phone){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_ID, id);

        // Storing email in pref
        editor.putString(KEY_PASS, pass);

        // Storing level in pref
        editor.putString(KEY_LEVEL,level);
        // Storing id in pref
        editor.putString(KEY_FULLNAME,fullname);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PHONE,phone);
        editor.putString(KEY_NAME,name);
        // commit changes
        editor.commit();
    }
    public void updateInfo(String fullname, String email, String phone){
        editor.putString(KEY_FULLNAME,fullname);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PHONE,phone);
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LogInActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_LEVEL, pref.getString(KEY_LEVEL,null));

        user.put(KEY_FULLNAME, pref.getString(KEY_FULLNAME,null));

        user.put(KEY_PHONE, pref.getString(KEY_PHONE,null));

        user.put(KEY_ID, pref.getString(KEY_ID,null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Log in Activity
        Intent i = new Intent(_context, LogInActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public void reCheckLogin(){
        // Toast.makeText(_context,pref.getString(KEY_NAME,null),Toast.LENGTH_SHORT).show();
        new CheckLogin().execute(pref.getString(KEY_NAME,null),pref.getString(KEY_PASS,null));
    }

    private class CheckLogin extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL = ips;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //Khởi tạo đối tượng client
                HttpClient client = new DefaultHttpClient();
                //Đối tượng chứa nội dung cần gửi
                HttpPost post = new HttpPost(URL);
                //Gán tham số vào giá trị gửi
                List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
                valuePairs.add(new BasicNameValuePair("username", params[0]));
                valuePairs.add(new BasicNameValuePair("pass", params[1]));

                //Gán nội dung lên form
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs);
                post.setEntity(entity);
                //Đón nhận kết quả
                HttpResponse response = client.execute(post);
                InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                String result  ="";
                while ((line = bufferedReader.readLine())!=null){
                    result +=line;
                }
                return result;

            }catch (Exception e){
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            //Kết quả trả về
            if (s.equals("Wrong password"))
            {
                logoutUser();
            }
            else {
                // Toast.makeText(_context,"test",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
