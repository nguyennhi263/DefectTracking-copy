package vn.com.ifca.defecttracking.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import java.util.concurrent.ExecutionException;

import vn.com.ifca.defecttracking.Adapter.ContractorSpinAdapter;
import vn.com.ifca.defecttracking.Adapter.DefectItemConfirmAdapter;
import vn.com.ifca.defecttracking.Adapter.UserSpinAdapter;
import vn.com.ifca.defecttracking.MainActivity;
import vn.com.ifca.defecttracking.Model.Contractor;
import vn.com.ifca.defecttracking.Model.Defect;
import vn.com.ifca.defecttracking.Model.DefectDBManager;
import vn.com.ifca.defecttracking.Model.DefectItem;
import vn.com.ifca.defecttracking.Model.SessionManager;
import vn.com.ifca.defecttracking.Model.User;
import vn.com.ifca.defecttracking.Model.ipconfig;
import vn.com.ifca.defecttracking.R;

public class ConfirmDefectActivity extends AppCompatActivity {
    Button submitDefect,addDefectItem;
    Bitmap bitmapText;
    ImageView imageView;
    DefectItem defectItem;
    HashMap<String, String> defect,user;
    ipconfig ipconfig;
    SessionManager sessionManager;
    TextView txtLocation;
    EditText txtNote;
    ArrayList<Contractor> listContractor;
    Spinner contractorSpin;
    String defectID;
    DefectDBManager defectDBManager;
    ArrayList<Defect> listDefetItem;
    ListView defectItemListview;
    DefectItemConfirmAdapter defectItemConfirmAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_defect);
        // back button on tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ipconfig = new ipconfig();
        txtLocation = (TextView) findViewById(R.id.txtLocation) ;
        submitDefect = (Button) findViewById(R.id.submitDefectBtn);
        addDefectItem = (Button) findViewById(R.id.addDefectItemConfirmBtn);
        defectItemListview = (ListView) findViewById(R.id.defectItemConfirmListview);
        listContractor = new ArrayList<>();
        listDefetItem = new ArrayList<>();
        defectItem = new DefectItem(getApplicationContext());
        defect = defectItem.getDefectDetail();
        String tLoc = defect.get(defectItem.KEY_UNIT);
        txtLocation.setText(tLoc);
        String tittle = defect.get(defectItem.KEY_LOCATON);
        this.setTitle(tittle);
        //get defectitem
        bitmapText=null;
        defectItem = new DefectItem(getApplicationContext());
        defect = defectItem.getDefectDetail();
        defectDBManager = new DefectDBManager(getApplicationContext());
        defectDBManager.open();
        listDefetItem = defectDBManager.getValues();
        defectItemConfirmAdapter = new DefectItemConfirmAdapter(ConfirmDefectActivity.this,listDefetItem);
        defectItemListview.setAdapter(defectItemConfirmAdapter);
        addDefectItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SelectTradeActivity.class));
            }
        });
        submitDefect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get user info
                sessionManager = new SessionManager(getApplicationContext());
                user = sessionManager.getUserDetails();
                final String userID = user.get(sessionManager.KEY_ID);
                final String unitID = defect.get(defectItem.KEY_UNITID);
                final String[] defectHeaderID = new String[1];
                final PostNewDefect postNewDefect = new PostNewDefect();

                // show dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmDefectActivity.this);
                //thiết lập tiêu đề cho Dialog
                builder.setTitle("!!!");
                builder.setCancelable(false);
                builder.setMessage("Defect will be assigned automatic?");

                //   builder.setIcon(R.mipmap.ic_launcher);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //postNewDefect.execute(imageText,loc,des,trade,userID,note,"null");
                        //function insert_defectItem($headerID,$image,$tradeDesID,$tradeID,$note,$contractorID,$placeID)
                        String defectJson= null;
                        try {
                            defectJson = new PostDefectHeader().execute(unitID,userID).get();
                            try {
                                JSONArray array = new JSONArray(defectJson);
                                JSONObject cur = array.getJSONObject(0);
                                defectID = cur.getString("DefectID");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        for (int i=0;i<listDefetItem.size();i++){
                            Defect defect = listDefetItem.get(i);
                            String imageText = "null";
                            if (defect.getImageFileNameBefore() != null){
                                imageText = defect.getImageFileNameBefore();
                            }
                            new PostNewDefect().execute(defectID,imageText,defect.getDescriptionID()
                                    ,defect.getTradeID(),defect.getNote(),"null",defect.getDefectPlaceID());
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmDefectActivity.this);
                        builder.setTitle("Post Defect success!!");
                        builder.setCancelable(false);
                        builder.setMessage("");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                defectDBManager.deleteAll();
                                startActivity(new Intent(ConfirmDefectActivity.this,MainActivity.class));
                            }
                        });
                        builder.create().show();
                    }


                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                        * Open Dialog to select contractor
                        * */
                        final Dialog dialogContractor = new Dialog(ConfirmDefectActivity.this,R.style.mydialogstyle);
                        dialogContractor.setContentView(R.layout.select_contractor);
                        dialogContractor.setCancelable(true);
                        contractorSpin = (Spinner) dialogContractor.findViewById(R.id.contractorSpin);
                        Button cancelBtn = (Button) dialogContractor.findViewById(R.id.cancelBtn);
                        Button okBtn = (Button) dialogContractor.findViewById(R.id.okBtn);
                        new GetListContractor().execute();
                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Contractor contractor = (Contractor) contractorSpin.getSelectedItem();
                                String  contractorID = contractor.getID();
                                String defectJson= null;
                                try {
                                    defectJson = new PostDefectHeader().execute(unitID,userID).get();
                                    try {
                                        JSONArray array = new JSONArray(defectJson);
                                        JSONObject cur = array.getJSONObject(0);
                                        defectID = cur.getString("DefectID");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                              //  new PostNewDefect().execute(defectID,imageText,tradeDesID,tradeID,note,contractorID,placeID);
                                for (int i=0;i<listDefetItem.size();i++){
                                    Defect defect = listDefetItem.get(i);
                                    String imageText = "null";
                                    if (defect.getImageFileNameBefore() != null){
                                        imageText = defect.getImageFileNameBefore();
                                    }
                                    new PostNewDefect().execute(defectID,imageText,defect.getDescriptionID()
                                            ,defect.getTradeID(),defect.getNote(),contractorID,defect.getDefectPlaceID());
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmDefectActivity.this);
                                builder.setTitle("Post Defect success!!");
                                builder.setCancelable(false);
                                builder.setMessage("");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        defectDBManager.deleteAll();
                                        startActivity(new Intent(ConfirmDefectActivity.this,MainActivity.class));
                                    }
                                });
                                builder.create().show();
                            }
                        });
                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogContractor.dismiss();
                            }
                        });
                        dialogContractor.show();
                    }
                });
                builder.create().show();
            }
        });
    }
    public void write_note(View v){
        txtNote.setVisibility(View.VISIBLE);
    }
    /*-------------------------------------INTENT ACTIVYTI------------------------------------*/
    public void go_to_back_screen(View v){
        finish();
        super.onBackPressed();
    }
    public void go_to_home_screen(View v){
        finish();
        startActivity(new Intent(ConfirmDefectActivity.this,MainActivity.class));

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
    /*--------------------------------END INTENT ACTIVYTI--------------------------------------*/
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    class PostDefectHeader extends AsyncTask<String,Void,String> {
        private String UPLOAD_URL =ipconfig.getIpconfig();

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(ConfirmDefectActivity.this, "Uploading data", "Please wait...",true,true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            if (!s.equals("Empty")) {

                try {
                    JSONArray array = new JSONArray(s);
                    JSONObject cur = array.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String unitID = params[0];
                String userid = params [1];
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(UPLOAD_URL);
                //Gán tham số vào giá trị gửi
                List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
                valuePairs.add(new BasicNameValuePair("insert_defectHeader", "1"));
                valuePairs.add(new BasicNameValuePair("UnitId", unitID));
                valuePairs.add(new BasicNameValuePair("HandOverStage", "D2C"));
                valuePairs.add(new BasicNameValuePair("CreateUserID", userid));
                UrlEncodedFormEntity entity = null;
                entity = new UrlEncodedFormEntity(valuePairs);
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                String result  ="";
                while ((line = bufferedReader.readLine())!=null){
                    result = line;
                }
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    class PostNewDefect extends AsyncTask<String,Void,String> {
        private String UPLOAD_URL =ipconfig.getIpconfig();
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(ConfirmDefectActivity.this,
                    "Uploading data", "Please wait...",true,true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            if (s.equals("Success")){
                defectItem.clear();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                defectItem.clear();
               /* AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmDefectActivity.this);
                builder.setTitle("Post Defect success!!");
                builder.setCancelable(false);
                builder.setMessage("");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ConfirmDefectActivity.this,MainActivity.class));
                    }
                });
                builder.create().show();*/
                defectItem.clear();
            }
            else {
                Toast.makeText(ConfirmDefectActivity.this,
                        "Something wrong, check it again",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String headerID = params[0];
                String image = params[1];
                String tradeDesID = params[2];
                String tradeID = params [3];
                String note = params[4];
                String contractorID = params[5];
                String placeID = params[6];
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(UPLOAD_URL);
                List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
                valuePairs.add(new BasicNameValuePair("insert_new_defect", "1"));
                valuePairs.add(new BasicNameValuePair("image", image));
                valuePairs.add(new BasicNameValuePair("tradeDesID", tradeDesID));
                valuePairs.add(new BasicNameValuePair("placeID", placeID));
                valuePairs.add(new BasicNameValuePair("headerID", headerID));
                valuePairs.add(new BasicNameValuePair("note", note));
                valuePairs.add(new BasicNameValuePair("contractorID", contractorID));
                UrlEncodedFormEntity entity = null;
                entity = new UrlEncodedFormEntity(valuePairs);
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                String result  ="";
                while ((line = bufferedReader.readLine())!=null){
                    result = line;
                }
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /*-------------------------------GET LIST Contractor-------------------------------*/
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
                valuePairs.add(new BasicNameValuePair("get_contend", "true"));
                valuePairs.add(new BasicNameValuePair("table", "ListContractor"));
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
                    listContractor.clear();
                    for (int i= 0; i< mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String contractorID = cur.getString("ContractorID");
                        String contractorName = cur.getString("ContractorName");
                        Contractor contractor = new Contractor(contractorID,contractorName);
                        listContractor.add(contractor);
                    }
                    ContractorSpinAdapter contractorSpinAdapter= new ContractorSpinAdapter(getApplicationContext(),listContractor);
                    contractorSpin.setAdapter(contractorSpinAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(ConfirmDefectActivity.this,"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
