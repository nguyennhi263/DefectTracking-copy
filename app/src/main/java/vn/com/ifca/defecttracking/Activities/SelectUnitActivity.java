package vn.com.ifca.defecttracking.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.com.ifca.defecttracking.Adapter.BlockSpinAdapter;
import vn.com.ifca.defecttracking.Adapter.PhaseSpinAdapter;
import vn.com.ifca.defecttracking.Adapter.ProjectSpinAdapter;
import vn.com.ifca.defecttracking.Adapter.UnitAdapter;
import vn.com.ifca.defecttracking.MainActivity;
import vn.com.ifca.defecttracking.Model.Block;
import vn.com.ifca.defecttracking.Model.DefectItem;
import vn.com.ifca.defecttracking.Model.Phase;
import vn.com.ifca.defecttracking.Model.Project;
import vn.com.ifca.defecttracking.Model.SessionManager;
import vn.com.ifca.defecttracking.Model.Unit;
import vn.com.ifca.defecttracking.Model.ipconfig;
import vn.com.ifca.defecttracking.R;

public class SelectUnitActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    SessionManager session;
    DefectItem defectItem;
    ArrayList<Phase> listPhase;
    ArrayList<Block> listBlock;
    ArrayList<Unit> listUnit;
    ArrayList<String> listFloor;
    ArrayList<Project> listProject;
    Spinner spPhase,spBlock,spUnit,spFloor, spProject;
    UnitAdapter customAdapter;
    ArrayAdapter<String> floorAdapter;
    Unit cur;
    String projectCur,phaseCur,blockCur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_unit);
        // back button on tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spPhase = (Spinner) findViewById(R.id.spPhase);
        spBlock = (Spinner) findViewById(R.id.spBlock);
        spUnit = (Spinner) findViewById(R.id.spUnit);
        spFloor = (Spinner) findViewById(R.id.spFloor);
        spProject = (Spinner) findViewById(R.id.spProject);
        listPhase = new ArrayList<>();
        listBlock = new ArrayList<>();
        listUnit = new ArrayList<>();
        listFloor = new ArrayList<>();
        listProject = new ArrayList<>();
        customAdapter = new UnitAdapter(getApplicationContext(),listUnit);
        floorAdapter=new ArrayAdapter<String>(SelectUnitActivity.this, android.R.layout.simple_spinner_item, listFloor);
        floorAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        defectItem = new DefectItem(getApplicationContext());


        new GetProject().execute("Project","");
       spProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               cur = null;
               listPhase.clear();
               listBlock.clear();
               listFloor.clear();
               Project project = listProject.get(i);
               projectCur = project.getProjectName();
               new GetPhase().execute("Phase",project.getProjectID());
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });
        spPhase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cur = null;
                listBlock.clear();
                listFloor.clear();
                Phase phase = listPhase.get(position);
                phaseCur = phase.getPhaseDesc();
                new GetBlock().execute("Block",phase.getPhaseID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                listBlock.clear();
                listFloor.clear();
            }
        });
        spBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listFloor.clear();
                Block block = listBlock.get(position);
                blockCur = block.getBlockName();
                new GetFloor().execute(block.getBlockID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listUnit.clear();
                String floor = listFloor.get(position);
                Block cur = (Block) spBlock.getSelectedItem();
                new GetUnit().execute("Unit",cur.getBlockID(),floor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cur = (Unit) spUnit.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /*-------------------------INTENT SCREEN-----------------------------------*/
    public  void go_to_select_trade_screen(View v){

        if (cur==null){
            Toast.makeText(SelectUnitActivity.this,"Please choose Unit level",Toast.LENGTH_SHORT).show();
        }
        else {
            defectItem.addUnit(cur.getUnitID(),cur.getUnitNo());
            Intent intent = new Intent(SelectUnitActivity.this,SelectTradeActivity.class);
            Bundle bundle = new Bundle();
            String location = phaseCur+"/"+blockCur+"/"+cur.getUnitNo();
            bundle.putString("Location", location);
            intent.putExtras(bundle);
            defectItem.addLocation(location);
            startActivity(intent);
        }

    }
    public void go_to_back_screen(View v){
        finish();
        super.onBackPressed();

    }
    public void go_to_homescreen(View v){
        finish();
        startActivity(new Intent(SelectUnitActivity.this,MainActivity.class));
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
            pDialog = new ProgressDialog(SelectUnitActivity.this);
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
                Toast.makeText(SelectUnitActivity.this,"Somethings wrong",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SelectUnitActivity.this,"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
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
            //    Toast.makeText(CreateNewTrade.this,s,Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
            //Kết quả trả về
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
                listUnit.clear();
                floorAdapter.notifyDataSetChanged();
                customAdapter.notifyDataSetChanged();
            }
            BlockSpinAdapter customAdapter = new BlockSpinAdapter(getApplicationContext(),listBlock);
            spBlock.setAdapter(customAdapter);
        }
    }
    /*-----------------------------GET FLOOR---------------------------*/
    private class GetFloor extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL =ips;
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
                valuePairs.add(new BasicNameValuePair("get_floor_size", "1"));
                valuePairs.add(new BasicNameValuePair("BlockID", params[0]));
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
            //    Toast.makeText(CreateNewTrade.this,s,Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
            //Kết quả trả về
            if (!s.equals("Empty")) {
                try {
                    JSONObject array = new JSONObject(s);
                    //  Toast.makeText(CreateNewTrade.this,mang.getString("numsize"),Toast.LENGTH_SHORT).show();
                    int length = Integer.parseInt(array.getString("numsize"));
                    listFloor.clear();
                    for (int i= 1;i<=length;i++){
                        listFloor.add(array.getString(i+""));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                listUnit.clear();
                listUnit = new ArrayList<>();
                customAdapter.notifyDataSetChanged();
            }
            spFloor.setAdapter(floorAdapter);
            floorAdapter.notifyDataSetChanged();
        }
    }
    /*-------------------------------GET UNIT-------------------------------*/
    private class GetUnit extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL=ips;
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
                valuePairs.add(new BasicNameValuePair("get_contend","0"));
                valuePairs.add(new BasicNameValuePair("table", params[0]));
                valuePairs.add(new BasicNameValuePair("command", params[1]));
                valuePairs.add(new BasicNameValuePair("command2", params[2]));
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
            //    Toast.makeText(CreateNewTrade.this,s,Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
            //Kết quả trả về
            if (!s.equals("Empty"))
            {
                try {
                    JSONArray mang = new JSONArray(s);
                    listUnit.clear();
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String UnitID = cur.getString("UnitID");
                        String UnitNo = cur.getString("UnitNo");
                        String Floor = cur.getString("UnitFloor");
                        Unit unit = new Unit(UnitID,UnitNo,Floor);
                        listUnit.add(unit);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                listUnit = new ArrayList<>();
            }
            customAdapter = new UnitAdapter(getApplicationContext(),listUnit);
            spUnit.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
        }
    }
}
