package vn.com.ifca.defecttracking.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
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

import vn.com.ifca.defecttracking.Model.User;
import vn.com.ifca.defecttracking.Model.ipconfig;
import vn.com.ifca.defecttracking.R;

public class UserManagementActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    List<User> userList;
    List<String> preStatus;
    ListView lvUser;
    Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);
        // back button on tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(getApplicationContext());
        saveBtn = (Button) findViewById(R.id.saveBtn);
        userList = new ArrayList<>();
        preStatus = new ArrayList<>();
        lvUser = (ListView) findViewById(R.id.lvUser);
        new GetListUser().execute();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < userList.size();i++){
                    User cur = userList.get(i);
                    if (!cur.getRecordStatus().equals(preStatus.get(i))){
                        new UpdateRecordStatus().execute(cur.getUserId(),cur.getRecordStatus());
                    }
                }
                new GetListUser().execute();
            }
        });
    }
    /*
        * Intent back screen
        *
        * */
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void go_to_back_screen(View v){
        super.onBackPressed();
        this.finish();
    }
    /*----------------------------------GET LIST USER-----------------------------------*/
    private class GetListUser extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL = ips;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserManagementActivity.this);
            pDialog.setMessage("Loading data..");
            pDialog.setCancelable(true);
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
                valuePairs.add(new BasicNameValuePair("get_contend","true"));
                valuePairs.add(new BasicNameValuePair("table", "ListUser"));
                //build new form
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs);
                post.setEntity(entity);
                //Recive result
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
                    userList.clear();
                    preStatus.clear();
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String userID = cur.getString("UserID");
                        String fullname = cur.getString("FullName");
                        String position = cur.getString("PositionName");
                        String level = cur.getString("UserLevel");
                        String email = cur.getString("Email");
                        String recordStatus = cur.getString("RecordStatus");
                        User user = new User(userID, position, fullname,email,recordStatus);
                        userList.add(user);
                        preStatus.add(recordStatus);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(UserManagementActivity.this,s,Toast.LENGTH_SHORT).show();
            }
            if (pDialog.isShowing())
            {    pDialog.dismiss();}
            UserAdaper userAdaper = new UserAdaper(UserManagementActivity.this,userList);
            lvUser.setAdapter(userAdaper);
        }
    }
    /*------------------------END FILE JSON-----------------------*/

    public class UserAdaper extends ArrayAdapter<User> {

        private Activity contex;
        private List<User> userList;

        public UserAdaper(Activity contex, List<User> userList){
            super (contex ,R.layout.one_row_user,userList);
            this.contex=contex;
            this.userList = userList;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = contex.getLayoutInflater();
            View listView = inflater.inflate(R.layout.one_row_user,null,true);
            //anh xa cac phan tu
            TextView txtUsername = (TextView)listView.findViewById(R.id.tvUserName);
            TextView txtUserLevel = (TextView)listView.findViewById(R.id.tvUserLevel);
            Switch statusSwitch = (Switch) listView.findViewById(R.id.statusSwitch);
            final User user = userList.get(position);
            txtUsername.setText(user.getFullname());
            txtUserLevel.setText(user.getUserPosition());
            if (user.getRecordStatus().equals("Active")){
                statusSwitch.setChecked(true);
            } else {
                statusSwitch.setChecked(false);
            }

            statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

                        user.setRecordStatus("Active");
                    }
                    else {
                        user.setRecordStatus("Inactive");
                    }
                }
            });
            return listView;
        }
    }
    /*----------------------------------GET LIST USER-----------------------------------*/
    private class UpdateRecordStatus extends AsyncTask<String, Void, String> {
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
                valuePairs.add(new BasicNameValuePair("updateRecordStatus","true"));
                valuePairs.add(new BasicNameValuePair("userid", params[0]));
                valuePairs.add(new BasicNameValuePair("status", params[1]));

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
            if (s.equals("true"))
            {
                Toast.makeText(getApplicationContext(),"updated",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(UserManagementActivity.this,"Some things wrong",Toast.LENGTH_SHORT).show();
            }

            if (pDialog.isShowing())
            {    pDialog.dismiss();}
            UserAdaper userAdaper = new UserAdaper(UserManagementActivity.this,userList);
            lvUser.setAdapter(userAdaper);
        }
    }
}
