package vn.com.ifca.defecttracking.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.com.ifca.defecttracking.Adapter.DefectPlaceSpinAdapter;
import vn.com.ifca.defecttracking.Adapter.DescriptionSpinAdapter;
import vn.com.ifca.defecttracking.Adapter.TradeSpinAdapter;
import vn.com.ifca.defecttracking.Adapter.TradeTypeSpinAdapter;
import vn.com.ifca.defecttracking.MainActivity;
import vn.com.ifca.defecttracking.Model.Defect;
import vn.com.ifca.defecttracking.Model.DefectDBManager;
import vn.com.ifca.defecttracking.Model.DefectItem;
import vn.com.ifca.defecttracking.Model.DefectPlace;
import vn.com.ifca.defecttracking.Model.Description;
import vn.com.ifca.defecttracking.Model.Trade;
import vn.com.ifca.defecttracking.Model.TradeType;
import vn.com.ifca.defecttracking.Model.ipconfig;
import vn.com.ifca.defecttracking.R;

public class SelectTradeActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    TradeTypeSpinAdapter tradeTypeSpinAdapter;
    TradeSpinAdapter tradeSpinAdapter;
    DescriptionSpinAdapter desAdapter;
    DefectPlaceSpinAdapter placeSpinAdapter;
    ArrayList<TradeType> listTradeType;
    ArrayList<Trade> listTrade;
    ArrayList<Description> listDesc;
    ArrayList<DefectPlace> listDefectPlace;
    Spinner spTradeType,spTrade,spDescription, spDefectPlace;
    DefectItem defectItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefectItem defectItem = new DefectItem(getApplicationContext());
        HashMap<String, String> defect = defectItem.getDefectDetail();
        String tittle = defect.get(defectItem.KEY_LOCATON);
        this.setTitle(tittle);
        setContentView(R.layout.activity_select_trade);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spTrade = (Spinner) findViewById(R.id.spTrade);
        spDescription = (Spinner) findViewById(R.id.spDescription);
        spTradeType = (Spinner) findViewById(R.id.spTradeType);
        spDefectPlace = (Spinner) findViewById(R.id.spDefectPlace);

        listTradeType = new ArrayList<>();
        listTrade = new ArrayList<>();
        listDesc = new ArrayList<>();
        listDefectPlace = new ArrayList<>();

        placeSpinAdapter = new DefectPlaceSpinAdapter(getApplicationContext(),listDefectPlace);
        tradeTypeSpinAdapter = new TradeTypeSpinAdapter(getApplicationContext(),listTradeType);
        tradeSpinAdapter = new TradeSpinAdapter(getApplicationContext(),listTrade);
        desAdapter = new DescriptionSpinAdapter(getApplicationContext(),listDesc);

        new GetDefectPlace().execute();
        new GetTradeType().execute("get_contend","TradeType");
        defectItem = new DefectItem(getApplicationContext());

        spTradeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listTrade.clear();
                TradeType tradeType = listTradeType.get(position);
                new GetTrade().execute("get_contend","Trade",tradeType.getTradeTypeID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spTrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listDesc.clear();
                Trade trade = listTrade.get(position);
                new GetDescription().execute("get_contend","TradeDescription",trade.getTradeID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                listDesc.clear();
                desAdapter.notifyDataSetChanged();
            }
        });

    }
    /*--------------------------------------INTENT SCREEN----------------------------------------*/
    public void go_to_home_screen(View v){
        startActivity( new Intent(SelectTradeActivity.this,MainActivity.class));
    }
    public void go_to_take_picture(View v){
        final Description curDes = (Description) spDescription.getSelectedItem();
        final Trade curTrade = (Trade) spTrade.getSelectedItem();
        final DefectPlace curDefectPlace = (DefectPlace) spDefectPlace.getSelectedItem();

        //defectItem.addDescription(curDes.getDescriptionID(), curDes.getDetail());
        //defectItem.addTrade(curTrade.getTradeID(),curTrade.getTradeName(),curDefectPlace.getID());
        final DefectDBManager defectDBManager = new DefectDBManager(getApplicationContext());
        defectDBManager.open();


        // show dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectTradeActivity.this);
        builder.setTitle("Would you like to attach picture?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                long defectItemInserted = defectDBManager.getIDinserted(curDefectPlace.getID(),curDefectPlace.getDefectPlaceName(),
                        curTrade.getTradeID(),curTrade.getTradeName(),curDes.getDescriptionID(),curDes.getDetail());

                Intent intent = new Intent(SelectTradeActivity.this,TakeDefectPictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("DefectItemID",(int)defectItemInserted);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                defectDBManager.close();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                defectDBManager.insert(curDefectPlace.getID(),curDefectPlace.getDefectPlaceName(),
                        curTrade.getTradeID(),curTrade.getTradeName(),curDes.getDescriptionID(),curDes.getDetail());
                defectDBManager.close();
                startActivity( new Intent(SelectTradeActivity.this,ConfirmDefectActivity.class));
                finish();
            }
        });
        builder.create().show();


    }
    public void finish(){
        this.finish();
    }
    public void got_to_back_screen(View v){
        super.onBackPressed();
        this.finish();
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
    /*-------------------------------GET TRADE TYPE-------------------------------*/
    private class GetTradeType extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL = ips;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectTradeActivity.this);
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
                valuePairs.add(new BasicNameValuePair("get_contend", params[0]));
                valuePairs.add(new BasicNameValuePair("table", params[1]));

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
            if (!s.equals("Wrong"))
            {
                try {
                    JSONArray mang = new JSONArray(s);

                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String TradeTypeID = cur.getString("TradeTypeID");
                        String TradeTypeName = cur.getString("TradeTypeName");
                        TradeType tradeType = new TradeType(TradeTypeID,TradeTypeName);
                        listTradeType.add(tradeType);
                    }
                    tradeTypeSpinAdapter = new TradeTypeSpinAdapter(getApplicationContext(),listTradeType);
                    spTradeType.setAdapter(tradeTypeSpinAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                Toast.makeText(SelectTradeActivity.this,"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
    /*-------------------------------GET TRADE -------------------------------*/
    private class GetTrade extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL =ips;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectTradeActivity.this);
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
                valuePairs.add(new BasicNameValuePair("get_contend", params[0]));
                valuePairs.add(new BasicNameValuePair("table", params[1]));
                valuePairs.add(new BasicNameValuePair("command", params[2]));
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
            if (!s.equals("Empty"))
            {
                try {
                    JSONArray mang = new JSONArray(s);
                    listTrade.clear();
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String TradeID = cur.getString("TradeID");
                        String TradeName = cur.getString("TradeName");
                        Trade trade = new Trade(TradeID,TradeName);
                        listTrade.add(trade);
                    }
                    tradeSpinAdapter = new TradeSpinAdapter(getApplicationContext(),listTrade);
                    spTrade.setAdapter(tradeSpinAdapter);
                    tradeSpinAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                Toast.makeText(SelectTradeActivity.this,"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
    /*-------------------------------GET DESCRIPTION -------------------------------*/
    private class GetDescription extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL = ips;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectTradeActivity.this);
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
                valuePairs.add(new BasicNameValuePair("get_contend", params[0]));
                valuePairs.add(new BasicNameValuePair("table", params[1]));
                valuePairs.add(new BasicNameValuePair("command", params[2]));
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
            if (!s.equals("Empty"))
            {
                try {
                    JSONArray mang = new JSONArray(s);
                    listDesc.clear();
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String DescID = cur.getString("TradeDescriptionID");
                        String Detail = cur.getString("TradeDescriptionDetail");
                        Description description = new Description(DescID,Detail);
                        listDesc.add(description);
                    }
                    desAdapter = new DescriptionSpinAdapter(getApplicationContext(),listDesc);
                    spDescription.setAdapter(desAdapter);
                    desAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(SelectTradeActivity.this,"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
    /*-------------------------------GET DEFECT PLACE -------------------------------*/
    @SuppressLint("StaticFieldLeak")
    private class GetDefectPlace extends AsyncTask<String, Void, String> {
        ipconfig ip= new ipconfig();
        String ips= ip.getIpconfig();
        String URL = ips;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectTradeActivity.this);
            pDialog.setMessage("Loading data..");
            pDialog.setCancelable(true);

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
                valuePairs.add(new BasicNameValuePair("get_contend",""));
                valuePairs.add(new BasicNameValuePair("table", "DefectPlace"));
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
            if (!s.equals("Empty"))
            {
                try {
                    JSONArray mang = new JSONArray(s);
                    listDefectPlace.clear();
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String ID = cur.getString("DefectPlaceID");
                        String placeName = cur.getString("DefectPlaceName");
                        DefectPlace defectPlace = new DefectPlace(ID,placeName);
                        listDefectPlace.add(defectPlace);
                    }
                    placeSpinAdapter = new DefectPlaceSpinAdapter(getApplicationContext(),listDefectPlace);
                    spDefectPlace.setAdapter(placeSpinAdapter);
                    placeSpinAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                pDialog.dismiss();
                Toast.makeText(SelectTradeActivity.this,"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
}
