package vn.com.ifca.defecttracking.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import vn.com.ifca.defecttracking.Adapter.BlockSpinAdapter;
import vn.com.ifca.defecttracking.Adapter.DefectHeaderAdapter;
import vn.com.ifca.defecttracking.Adapter.DefectItemAdapter;
import vn.com.ifca.defecttracking.Adapter.PhaseSpinAdapter;
import vn.com.ifca.defecttracking.Adapter.ProjectSpinAdapter;
import vn.com.ifca.defecttracking.Model.Block;
import vn.com.ifca.defecttracking.Model.Defect;
import vn.com.ifca.defecttracking.Model.DefectHeader;
import vn.com.ifca.defecttracking.Model.Phase;
import vn.com.ifca.defecttracking.Model.Project;
import vn.com.ifca.defecttracking.Model.SessionManager;
import vn.com.ifca.defecttracking.Model.Unit;
import vn.com.ifca.defecttracking.Model.ipconfig;
import vn.com.ifca.defecttracking.R;

import static android.widget.Toast.LENGTH_SHORT;

public class DefectManagementActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    ipconfig ipconfig;
    ArrayList<Phase> listPhase;
    ArrayList<Block> listBlock;
    ArrayList<String> listFloor;
    ArrayList<Project> listProject;
    ArrayList<DefectHeader> listDefectHeader;
    Spinner spPhase,spBlock, spProject;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String level,userID;
    ListView lvDefectHeader;
    DefectHeaderAdapter defectHeaderAdapter;
    EditText unitText;
    Project projectCur;
    Block blockCur;
    Phase phaseCur;
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_management);
        ipconfig = new ipconfig();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spPhase = (Spinner) findViewById(R.id.spPhase);
        spProject = (Spinner) findViewById(R.id.spProject);
        spBlock = (Spinner) findViewById(R.id.spBlock);
        lvDefectHeader = (ListView) findViewById(R.id.lvDefectHeader);
        defectHeaderAdapter = new DefectHeaderAdapter(getApplicationContext(),listDefectHeader);
        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();
        level = user.get(SessionManager.KEY_LEVEL);
        userID=  user.get(SessionManager.KEY_ID);
        listPhase = new ArrayList<>();
        listBlock = new ArrayList<>();
        listFloor = new ArrayList<>();
        listProject = new ArrayList<>();
        listDefectHeader = new ArrayList<>();
        unitText = (EditText) findViewById(R.id.unitText);
        new GetProject().execute();


        spProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listPhase.clear();
                listBlock.clear();
                listFloor.clear();
                 projectCur = listProject.get(i);
                new GetPhase().execute("Phase",projectCur.getProjectID());
                listDefectHeader.clear();
                defectHeaderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spPhase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listDefectHeader.clear();
                defectHeaderAdapter.notifyDataSetChanged();
                listBlock.clear();
                listFloor.clear();
                phaseCur = listPhase.get(position);
                new GetBlock().execute("Block",phaseCur.getPhaseID());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                listBlock.clear();
                listFloor.clear();
                listDefectHeader.clear();
                defectHeaderAdapter.notifyDataSetChanged();
            }
        });
        spBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listFloor.clear();
                blockCur = listBlock.get(position);
                listDefectHeader.clear();
                defectHeaderAdapter.notifyDataSetChanged();
                new GetDefectHeader().execute("DefectHeader",blockCur.getBlockName());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        unitText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable){
                defectHeaderAdapter.getFilter().filter(unitText.getText());
                }
        });

    }
    private Date StringToDate(String s){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(s);
            // System.out.println(date);
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
    /*-------------------------------GET PROJECT-------------------------------*/
    private class GetProject extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL = ips;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DefectManagementActivity.this);
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
                valuePairs.add(new BasicNameValuePair("get_contend", "1"));
                valuePairs.add(new BasicNameValuePair("table", "Project"));
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

            if (!s.equals("Wrong"))
            {
                try {
                    JSONArray mang = new JSONArray(s);
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String ProjectID = cur.getString("ProjectID");
                        String ProjectName = cur.getString("ProjectName");
                        Project project = new Project(ProjectID,ProjectName);
                        listProject.add(project);
                    }
                    ProjectSpinAdapter customAdapter=new ProjectSpinAdapter(getApplicationContext(),listProject);
                    spProject.setAdapter(customAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(DefectManagementActivity.this,"Somethings wrong", LENGTH_SHORT).show();
            }
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
    /*-------------------------------GET PHASE-------------------------------*/
    private class GetPhase extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL = ips;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DefectManagementActivity.this);
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
                valuePairs.add(new BasicNameValuePair("get_contend", "1"));
                valuePairs.add(new BasicNameValuePair("table", params[0]));
                valuePairs.add(new BasicNameValuePair("command", params[1]));

                //Gán nội dung lên form
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs);
                post.setEntity(entity);
                //Đón nhận kết quả
                HttpResponse response = client.execute(post);
                InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                String result = "";
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
            if (!s.equals("Wrong"))
            {
                try {
                    JSONArray mang = new JSONArray(s);
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String PhaseID = cur.getString("PhaseID");
                        String PhaseDesc = cur.getString("PhaseDesc");
                        Phase phase = new Phase(PhaseID,PhaseDesc);
                        listPhase.add(phase);
                    }
                    PhaseSpinAdapter customAdapter=new PhaseSpinAdapter(getApplicationContext(),listPhase);
                    spPhase.setAdapter(customAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                Toast.makeText(DefectManagementActivity.this,"Somethings wrong", LENGTH_SHORT).show();
            }
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
    /*-------------------------------GET Defect Header-------------------------------*/
    private class GetDefectHeader extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL = ips;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DefectManagementActivity.this);
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
                valuePairs.add(new BasicNameValuePair("get_contend", "1"));
                valuePairs.add(new BasicNameValuePair("table", params[0]));
                valuePairs.add(new BasicNameValuePair("command", params[1]));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs);
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                String result = "";
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
                try {
                    listDefectHeader.clear();
                    JSONArray mang = new JSONArray(s);
                    String locationDetail = phaseCur.getPhaseDesc()+"->"+blockCur.getBlockName();
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String defectID = cur.getString("DefectID");
                        String unitNo = cur.getString("UnitNo");
                        String createDate = cur.getString("CreateDate");
                        String recordStatus = cur.getString("RecordStatus");
                        String unitID = cur.getString("UnitID");
                        DefectHeader defectHeader = new DefectHeader(defectID,unitID,unitNo,createDate,recordStatus,locationDetail);
                        listDefectHeader.add(defectHeader);
                    }
                    defectHeaderAdapter = new DefectHeaderAdapter(getApplicationContext(),listDefectHeader);
                    lvDefectHeader.setAdapter(defectHeaderAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(DefectManagementActivity.this,"Somethings wrong", LENGTH_SHORT).show();
            }
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
    /*-----------------------------GET BLOCK------------------------------*/
    private class GetBlock extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL =ips;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DefectManagementActivity.this);
            pDialog.setMessage("Loading data..");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(URL);
                List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
                valuePairs.add(new BasicNameValuePair("get_contend", "1"));
                valuePairs.add(new BasicNameValuePair("table", params[0]));
                valuePairs.add(new BasicNameValuePair("command", params[1]));
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
                    listBlock.clear();
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String BlockID = cur.getString("BlockID");
                        String BlockName = cur.getString("BlockName");
                        Block block = new Block(BlockID,BlockName);
                        listBlock.add(block);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                listFloor.clear();
            }
            if (pDialog.isShowing())
            {    pDialog.dismiss();}
            BlockSpinAdapter customAdapter = new BlockSpinAdapter(getApplicationContext(),listBlock);
            spBlock.setAdapter(customAdapter);

        }
    }
}
