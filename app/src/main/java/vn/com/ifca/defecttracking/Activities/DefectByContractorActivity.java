package vn.com.ifca.defecttracking.Activities;

import android.app.ProgressDialog;
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
import java.util.ArrayList;
import java.util.List;

import vn.com.ifca.defecttracking.Adapter.ContractorAdapter;
import vn.com.ifca.defecttracking.Model.Contractor;
import vn.com.ifca.defecttracking.Model.User;
import vn.com.ifca.defecttracking.Model.ipconfig;
import vn.com.ifca.defecttracking.R;

public class DefectByContractorActivity extends AppCompatActivity {
    ListView lvReportByContractor;
    ArrayList<Contractor> listContractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_by_contractor);
        lvReportByContractor = findViewById(R.id.lvReportByContractor);
        listContractor = new ArrayList<>();
        new GetListContractor().execute();
    }
    /*----------------------------------GET LIST REPORT-----------------------------------*/
    private class GetListContractor extends AsyncTask<String, Void, String> {
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
                valuePairs.add(new BasicNameValuePair("get_contend","true"));
                valuePairs.add(new BasicNameValuePair("table", "ReportByContractor"));
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
                    listContractor.clear();
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String contractorName = cur.getString("ContractorName");
                        String defect_close = cur.getString("Defect_Close");
                        String defect_open = cur.getString("Defect_Open");
                        Contractor contractor = new Contractor(contractorName,defect_open,defect_close);

                        listContractor.add(contractor);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(DefectByContractorActivity.this,s,Toast.LENGTH_SHORT).show();
            }
            ContractorAdapter userAdaper = new ContractorAdapter(DefectByContractorActivity.this,listContractor);
            lvReportByContractor.setAdapter(userAdaper);
        }
    }
}
