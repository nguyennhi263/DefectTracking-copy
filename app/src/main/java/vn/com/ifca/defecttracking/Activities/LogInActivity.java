package vn.com.ifca.defecttracking.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import vn.com.ifca.defecttracking.MainActivity;
import vn.com.ifca.defecttracking.Model.SessionManager;
import vn.com.ifca.defecttracking.Model.ipconfig;
import vn.com.ifca.defecttracking.R;

public class LogInActivity extends AppCompatActivity {

    EditText txtname, txtpass;
    Button loginbtn;
    ProgressDialog pDialog;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_log_in);
        // Session Manager Class
        session = new SessionManager(getApplicationContext());

        txtname = (EditText) findViewById(R.id.txtUsername);
        txtpass = (EditText) findViewById(R.id.txtPassword);
        loginbtn = (Button) findViewById(R.id.loginbtn);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get id,pass from edit text
                String name = txtname.getText().toString().trim();
                final String password = txtpass.getText().toString().trim();
                //neu email rong
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(LogInActivity.this, "Enter your username", Toast.LENGTH_SHORT).show();
                    //dung chuong trinh lai
                    return;
                }
                //neu password rong
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LogInActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    //dung chuong trinh lai
                    return;
                }
                new   CheckLogin().execute(name,password);
            }
        });

    }
    private class CheckLogin extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL = ips;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LogInActivity.this);
            pDialog.setMessage("Loading data..");
            pDialog.setCancelable(false);
            pDialog.show();
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
                valuePairs.add(new BasicNameValuePair("check_login", "true"));


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
                        String Pass = txtpass.getText().toString().trim();
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
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
