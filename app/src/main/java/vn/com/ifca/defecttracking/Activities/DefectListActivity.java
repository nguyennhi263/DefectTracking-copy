package vn.com.ifca.defecttracking.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import vn.com.ifca.defecttracking.Adapter.DefectItemAdapter;
import vn.com.ifca.defecttracking.Model.Defect;
import vn.com.ifca.defecttracking.Model.SessionManager;
import vn.com.ifca.defecttracking.Model.ipconfig;
import vn.com.ifca.defecttracking.R;

public class DefectListActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    vn.com.ifca.defecttracking.Model.ipconfig ipconfig;
    ArrayList<Defect> listDefect;
    ListView lvDefect;
    DefectItemAdapter defectItemAdapter;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String level,userID;
    String defectHeaderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_list);
        ipconfig = new ipconfig();
        // initiate
        listDefect = new ArrayList<>();
        lvDefect = (ListView) findViewById(R.id.lvDefectItem);
        // back button on tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();
        userID=  user.get(SessionManager.KEY_ID);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String title = bundle.getString("DefectHeaderLocation", "");
            setTitle(title);
             defectHeaderID = bundle.getString("DefectHeaderID",null);
          //  new GetListDefectItem().execute(defectHeaderID);
        }


    }

    private Date StringToDate(String s){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    private String DateToString (Date date){
        SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd");
        String datetime = dateformat.format(date);
        return datetime;
    }
    /*-------------------------------GET LIST DEFECT-------------------------------*/
    private class GetListDefectItem extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL = ips;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DefectListActivity.this);
            pDialog.setMessage("Loading data..");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(URL);
                List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
                valuePairs.add(new BasicNameValuePair("get_contend", "true"));
                valuePairs.add(new BasicNameValuePair("table","DefectItem"));
                valuePairs.add(new BasicNameValuePair("command",params[0]));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs);
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                String result  = "";
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
            if (!s.equals("Wrong"))
            {
                listDefect.clear();
                try {

                    JSONArray mang = new JSONArray(s);
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String defectID = cur.getString("DefectItemID");
                        String description = cur.getString("TradeDescriptionDetail");
                        String tradeName = cur.getString("TradeName");
                        String defectStatus = cur.getString("DefectStatus");
                        String imageFileNameBefore = cur.getString("ImageFileNameBefore");
                        String imageFileNameAfter = cur.getString("ImageFileNameAfter");
                        String note = cur.getString("Note");
                        String defectPlaceID = cur.getString("DefectPlaceID");
                        String defectPlaceName = cur.getString("DefectPlaceName");
                        String contractorName = cur.getString("ContractorName");
                        String tradeID = cur.getString("TradeID");
                        String descriptionID = cur.getString("TradeDescriptionID");
                        Defect defect = new Defect(defectID, description, defectStatus, tradeName,
                                imageFileNameBefore, imageFileNameAfter,  note,  defectPlaceName,  contractorName,tradeID,descriptionID);
                        listDefect.add(defect);

                    }
                    defectItemAdapter = new DefectItemAdapter(DefectListActivity.this,listDefect);
                    lvDefect.setAdapter(defectItemAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }
            else {
                Toast.makeText(DefectListActivity.this,"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        new GetListDefectItem().execute(defectHeaderID);
    }
}

