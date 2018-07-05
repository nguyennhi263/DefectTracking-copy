package vn.com.ifca.defecttracking.Activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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

import vn.com.ifca.defecttracking.Adapter.ContractorAdapter;
import vn.com.ifca.defecttracking.Model.Contractor;
import vn.com.ifca.defecttracking.Model.Project;
import vn.com.ifca.defecttracking.Model.ipconfig;
import vn.com.ifca.defecttracking.R;

public class ReportByProjectActivity extends AppCompatActivity {
    ListView lvReportByProject;
    ArrayList<Project> listProject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_by_project);
        lvReportByProject = findViewById(R.id.lvReportByProject);
        listProject = new ArrayList<>();
        new GetListReportProject().execute();
    }
    /*----------------------------------GET LIST REPORT-----------------------------------*/
    private class GetListReportProject extends AsyncTask<String, Void, String> {
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
                valuePairs.add(new BasicNameValuePair("table", "ReportByProject"));
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
                    listProject.clear();
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String projectName = cur.getString("ProjectName");
                        String defect_close = cur.getString("Defect_Close");
                        String defect_open = cur.getString("Defect_Open");
                        Project project = new Project(projectName,defect_open,defect_close);

                        listProject.add(project);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            }
            ProjectAdapter projectAdaper = new ProjectAdapter(ReportByProjectActivity.this,listProject);
            lvReportByProject.setAdapter(projectAdaper);
        }
    }
    private class ProjectAdapter extends ArrayAdapter<Project> {
        private Activity contex;
        private List<Project> projectList;


        public ProjectAdapter(Activity contex, List<Project> projectList){
            super (contex , R.layout.one_row_report_by_project,projectList);
            this.contex=contex;
            this.projectList = projectList;
        }
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = contex.getLayoutInflater();
            View listView = inflater.inflate(R.layout.one_row_report_by_project,null,true);
            TextView contractorName = (TextView) listView.findViewById(R.id.projectName);
            TextView defectOpen = (TextView) listView.findViewById(R.id.defectOpen);
            TextView defectClose = (TextView) listView.findViewById(R.id.defectClose);

            Project project = projectList.get(position);
            contractorName.setText(project.getProjectName());
            defectOpen.setText(project.getReport_Defect_Open());
            defectClose.setText(project.getReport_Defect_Close());
            return listView;
        }
    }
}
