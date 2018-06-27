package vn.com.ifca.defecttracking.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import java.util.HashMap;
import java.util.List;

import vn.com.ifca.defecttracking.MainActivity;
import vn.com.ifca.defecttracking.Model.IP4V;
import vn.com.ifca.defecttracking.Model.SessionManager;
import vn.com.ifca.defecttracking.R;


public class LogInActivity extends AppCompatActivity {
    private EditText user;
    private EditText pass;
    private CardView LoginBtn;
    private String username;
    private String password;
    private ImageButton settingBtn;
    ProgressBar pBar;
    IP4V link;
    HashMap<String, String> ip;
    SessionManager session;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide support bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_log_in);
        /*
        *   Initial
        * */
        user = findViewById(R.id.txtUsername);
        pass = findViewById(R.id.txtPassword);
        LoginBtn = findViewById(R.id.LoginBtn);
        pBar = findViewById(R.id.progress);
        pBar.setVisibility(View.INVISIBLE);
        link = new IP4V(getApplicationContext());
        settingBtn = findViewById(R.id.url_destinationBtn);
        ip = link.getIP();
        // Session Manager Class
        session = new SessionManager(getApplicationContext());
        /*
        *   Event
        * */
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                password = pass.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(LogInActivity.this, "@string/logInToastUsername", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LogInActivity.this, "@string/logInToastPassword", Toast.LENGTH_SHORT).show();
                } else {
                     //Toast.makeText(getApplicationContext(), "test" + username + "!", Toast.LENGTH_SHORT).show();
                    new   CheckLogin().execute(username,password);
                }
            }
        });


        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogContractor = new Dialog(LogInActivity.this);
                dialogContractor.setContentView(R.layout.url_options);
                dialogContractor.setCancelable(true);
                // initial
                Button cancelBtn;
                cancelBtn = (Button) dialogContractor.findViewById(R.id.cancel_url);
                final Button urlBtn = dialogContractor.findViewById(R.id.ok_url_entry);
                final EditText urtText = (EditText) dialogContractor.findViewById(R.id.url_entry);
                //get url in db
                ip = link.getIP();
                String URL = ip.get(link.KEY_URL);
                urtText.setText(URL);

                // Save Event
                urlBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (urlBtn.getText().toString().equals("")) {

                        } else {
                            final String urlText = urtText.getText().toString();
                            link.add_KEYURL(urlText);
                            Toast.makeText(getApplicationContext(), "@string/updateURL", Toast.LENGTH_SHORT).show();
                            dialogContractor.dismiss();
                        }
                    }
                });

                // Cancel
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogContractor.dismiss();
                    }
                });
                dialogContractor.show();
            };
        });
    };
    private class CheckLogin extends AsyncTask<String, Void, String> {
        String URL = ip.get(link.KEY_URL);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //create client
                HttpClient client = new DefaultHttpClient();
                // create client post
                HttpPost post = new HttpPost(URL);
                // add params
                List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
                valuePairs.add(new BasicNameValuePair("username", params[0]));
                valuePairs.add(new BasicNameValuePair("pass", params[1]));
                valuePairs.add(new BasicNameValuePair("check_login", "true"));

                // convert data to form
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs);
                post.setEntity(entity);
                // receive data
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
            // data received
            if (!s.equals("false"))
            {
                try {
                    JSONArray mang = new JSONArray(s);

                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);

                        String UserID = cur.getString("UserID");
                        String Level = cur.getString("PositionName");
                        String Username = cur.getString("LoginName");
                        String fullname = cur.getString("FullName");
                        String email = cur.getString("Email");
                        String mobile = cur.getString("Mobile");
                        String Pass = password;
                        session.createLoginSession(UserID,Username,Pass,fullname,Level,email,mobile);
                        Toast.makeText(LogInActivity.this,"Log in success",Toast.LENGTH_SHORT).show();

                        // Staring MainActivity
                        finish();
                        Intent it = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(it);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(LogInActivity.this,"Wrong password or username",Toast.LENGTH_SHORT).show();
            }
            pBar.setVisibility(View.INVISIBLE);
        }
    }
}
