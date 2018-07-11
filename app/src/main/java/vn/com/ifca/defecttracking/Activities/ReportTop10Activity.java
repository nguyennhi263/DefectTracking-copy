package vn.com.ifca.defecttracking.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import vn.com.ifca.defecttracking.MainActivity;
import vn.com.ifca.defecttracking.Model.Defect;
import vn.com.ifca.defecttracking.Model.Project;
import vn.com.ifca.defecttracking.Model.ipconfig;
import vn.com.ifca.defecttracking.R;

public class ReportTop10Activity extends AppCompatActivity {
    ListView lvReportByProject;
    ArrayList<Defect> listDefect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_top10);
        // back button on tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get data
        lvReportByProject = findViewById(R.id.lvReportTop10);
        listDefect = new ArrayList<>();
        new GetListTop10().execute();
    }
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
        finish();
        super.onBackPressed();

    }
    public void go_to_homescreen(View v){
        finish();
        startActivity(new Intent(ReportTop10Activity.this,MainActivity.class));
    }
    /*----------------------------------GET LIST REPORT-----------------------------------*/
    private class GetListTop10 extends AsyncTask<String, Void, String> {
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
                valuePairs.add(new BasicNameValuePair("table", "Top10Defect"));
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
                    listDefect.clear();
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String defectName = cur.getString("TradeName");
                        String defectNo = cur.getString("CountNo");

                        Defect defect = new Defect(defectName,defectNo);

                        listDefect.add(defect);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            }
            Top10Adapter projectAdaper = new Top10Adapter(ReportTop10Activity.this,listDefect);
            lvReportByProject.setAdapter(projectAdaper);
        }
    }
    private class Top10Adapter extends ArrayAdapter<Defect> {
        private Activity contex;
        private List<Defect> projectList;

        public Top10Adapter(Activity contex, List<Defect> defecttList){
            super (contex , R.layout.one_row_report_by_project,defecttList);
            this.contex=contex;
            this.projectList = defecttList;
        }
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = contex.getLayoutInflater();
            View listView = inflater.inflate(R.layout.one_row_top_defect,null,true);
            TextView defectName = (TextView) listView.findViewById(R.id.defectName);
            TextView defectNo = (TextView) listView.findViewById(R.id.defectNo);

            Defect defect = projectList.get(position);
            defectName.setText(defect.getTradeName());
            defectNo.setText(defect.getDefectNo());
            //set text color to orange
            defectName.setTextColor(0xFFFA9A2A);
            defectNo.setTextColor(0xFFFA9A2A);
            return listView;
        }
    }
}
