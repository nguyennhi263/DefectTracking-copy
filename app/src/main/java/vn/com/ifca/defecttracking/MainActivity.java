package vn.com.ifca.defecttracking;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import vn.com.ifca.defecttracking.Activities.DefectByContractorActivity;
import vn.com.ifca.defecttracking.Activities.DefectManagementActivity;
import vn.com.ifca.defecttracking.Activities.LogInActivity;
import vn.com.ifca.defecttracking.Activities.SelectUnitActivity;
import vn.com.ifca.defecttracking.Activities.UserManagementActivity;
import vn.com.ifca.defecttracking.Model.DefectPlace;
import vn.com.ifca.defecttracking.Model.LanguagePf;
import vn.com.ifca.defecttracking.Model.SessionManager;
import vn.com.ifca.defecttracking.Model.ipconfig;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SessionManager sessionManager;
    boolean doubleBackToExitPressedOnce = false;
    LanguagePf lang;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //initial
        lang = new LanguagePf();
        res = this.getResources();
        lang.initialize(getApplicationContext(), this.getResources());
        TextView by_contractor = (TextView) findViewById(R.id.by_contractor);

        by_contractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DefectByContractorActivity.class));
            }
        });

        // check log in
        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()){
            // set tab layout
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        else {
            sessionManager.checkLogin();
        }


    }
    @Override
    protected void onStart() {
        super.onStart();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }else {
            System.exit(1);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_defect) {
            // Handle the camera action
            startActivity(new Intent(this, SelectUnitActivity.class));

        } else if (id == R.id.defect_management) {
            startActivity(new Intent(this, DefectManagementActivity.class));

        } else if (id == R.id.user_management) {
            startActivity(new Intent(this, UserManagementActivity.class));
        } else if (id == R.id.language_options) {
            final Dialog languageContractor = new Dialog(MainActivity.this);
            languageContractor.setContentView(R.layout.language_options);
            languageContractor.setCancelable(true);
            Button cancelBtn = languageContractor.findViewById(R.id.cancel_language);
            final Button okBtn = languageContractor.findViewById(R.id.confirm_language);
            final RadioButton english = languageContractor.findViewById(R.id.english_language_option);
            final RadioButton viet = languageContractor.findViewById(R.id.vietnamese_language_option);
            if (lang.getPreferences().equals("en")) english.toggle();
            else viet.toggle();
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (english.isChecked() == false && viet.isChecked() == false) {
                        Toast.makeText(getApplicationContext(), "@string/languageWarning", Toast.LENGTH_SHORT).show();
                    }
                    else if (english.isChecked()) {
                        lang.changeLanguage(res, "en");
                        lang.updatePreferences("en");

                        languageContractor.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                    else {
                        lang.changeLanguage(res, "vi");
                        lang.updatePreferences("vi");
                        languageContractor.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                }


            });
            // Cancel
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    languageContractor.dismiss();
                }
            });
            languageContractor.show();

        } else if (id == R.id.logout){
            Toast.makeText(getApplicationContext(),"log out",Toast.LENGTH_SHORT).show();
            sessionManager.logoutUser();
            startActivity( new Intent(getApplicationContext(),LogInActivity.class));
        }else if(id == R.id.exit){
            //System.exit(1);
            finish();
            moveTaskToBack(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private class MyBroswer extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    /*-------------------------------GET DEFECT PLACE -------------------------------*/
    private class GetStaticsNumber extends AsyncTask<String, Void, String> {
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
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(URL);
                List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
                valuePairs.add(new BasicNameValuePair("get_contend",""));
                valuePairs.add(new BasicNameValuePair("table", "DefectHeaderOpen"));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs);
                post.setEntity(entity);
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
            if (!s.equals("Empty"))
            {
                try {
                    JSONArray mang = new JSONArray(s);
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String total = cur.getString("Total");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
