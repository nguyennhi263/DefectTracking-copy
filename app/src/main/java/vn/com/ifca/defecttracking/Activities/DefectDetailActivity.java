package vn.com.ifca.defecttracking.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import vn.com.ifca.defecttracking.Adapter.DefectPlaceSpinAdapter;
import vn.com.ifca.defecttracking.Adapter.DescriptionSpinAdapter;
import vn.com.ifca.defecttracking.Adapter.TradeSpinAdapter;
import vn.com.ifca.defecttracking.MainActivity;
import vn.com.ifca.defecttracking.Model.DefectItem;
import vn.com.ifca.defecttracking.Model.DefectPlace;
import vn.com.ifca.defecttracking.Model.Description;
import vn.com.ifca.defecttracking.Model.Trade;
import vn.com.ifca.defecttracking.Model.TradeType;
import vn.com.ifca.defecttracking.Model.ipconfig;
import vn.com.ifca.defecttracking.R;

public class DefectDetailActivity extends AppCompatActivity {
    ImageView imageViewDownload, imageClose, imageDialogCloseDefect;
    DefectItem defectItem;
    HashMap<String, String> defect;
    TextView txtDes,txtLocation,txtTrade,txtNote,txtAssign,txtDateCreate;
    EditText editNote;
    Button  CloseDefectBtn,editDefectDetail;
    CheckBox checkComplete;
    String imageText;
    Dialog dialogContractor;
    ProgressDialog pDialog;
    Spinner defectPlaceSpin,tradeDTSpin,descriptionDTSpin;
    private static final int CAMERA_REQUEST = 1888;
    ArrayList<DefectPlace> listDefectPlace;
    ArrayList<Trade> listTrade;
    ArrayList<Description> listDesc;
    DefectPlaceSpinAdapter placeSpinAdapter;
    TradeSpinAdapter tradeSpinAdapter;
    DescriptionSpinAdapter descriptionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_detail);
        // initiate
        imageViewDownload =  (ImageView ) findViewById(R.id.imageViewDownload);
        imageClose = (ImageView) findViewById(R.id.imageAfterClose);
        txtDes = (TextView) findViewById(R.id.txtDes);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtTrade = (TextView) findViewById(R.id.txtTrade);
        txtNote = (TextView) findViewById(R.id.txtNote);
        txtAssign = (TextView) findViewById(R.id.txtAssign);
        txtDateCreate = (TextView) findViewById(R.id.txtDateCreate);
        checkComplete = (CheckBox) findViewById(R.id.checkComplete);
        CloseDefectBtn = (Button) findViewById(R.id.CloseDefectBtn);
        editDefectDetail = (Button) findViewById(R.id.editDefectDetail);
        defectPlaceSpin = (Spinner) findViewById(R.id.placeDTSpin);
        tradeDTSpin = (Spinner) findViewById(R.id.tradeDTSpin);
        descriptionDTSpin = (Spinner) findViewById(R.id.descriptionDTSpin);
        editNote = (EditText) findViewById(R.id.editNote);
        listDefectPlace = new ArrayList<DefectPlace>();
        listTrade = new ArrayList<Trade>();
        listDesc = new ArrayList<Description>();
        placeSpinAdapter = new DefectPlaceSpinAdapter(getApplicationContext(),listDefectPlace);
        tradeSpinAdapter = new TradeSpinAdapter(getApplicationContext(),listTrade);
        descriptionAdapter = new DescriptionSpinAdapter(getApplicationContext(),listDesc);
        // back button on tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        defectItem = new DefectItem(getApplicationContext());
        defect = defectItem.getDefectDetail();
        String tDes = defect.get(defectItem.KEY_DESCRIPTION);
        String tDefectPlace = defect.get(defectItem.KEY_DEFECTPLACE);

        final String tTrade = defect.get(defectItem.KEY_TRADE);
        String tImageFileName = defect.get(defectItem.KEY_IMAGEBEFORE);
        String tStatus = defect.get(defectItem.KEY_STATUS);
        String tNote =defect.get(defectItem.KEY_NOTE);
        String tAssign = defect.get(defectItem.KEY_CONTRACTOR);
        String tImageAfter = defect.get(defectItem.KEY_IMAGEAFTER);

        // show info
        txtDes.setText(tDes);
        txtLocation.setText(tDefectPlace);
        txtTrade.setText(tTrade);
        txtNote.setText(tNote);
        txtAssign.setText(tAssign);
        Picasso.with(getApplication()).load("http://demo.ifca.com.vn:2603/_IFCA_WebServer/pictures/"+tImageFileName+".JPG")
                .into(imageViewDownload);
        Picasso.with(getApplicationContext()).load("http://demo.ifca.com.vn:2603/_IFCA_WebServer/pictures/"+tImageAfter+".JPG")
                .into(imageClose);

        if (tStatus.equals("2")){
            editDefectDetail.setText("Back");
            checkComplete.setChecked(true);
            checkComplete.setClickable(false);
        }
        //imageViewDownload.setVisibility(View.GONE);
        if (tImageFileName.equals("picture")){
            imageViewDownload.setVisibility(View.GONE);
        }
        if (tImageAfter.equals("null")||tImageAfter.equals("picture")){
            imageClose.setVisibility(View.GONE);
        }

        new  GetDefectPlace().execute();
        dialogContractor = new Dialog(DefectDetailActivity.this,R.style.mydialogstyle);
        dialogContractor.setContentView(R.layout.picture_defect_close);
        dialogContractor.setCancelable(true);

        Button takePiture = (Button) dialogContractor.findViewById(R.id.takePicture);
        Button submitDefect = (Button) dialogContractor.findViewById(R.id.submitCloseDefect);
        imageDialogCloseDefect = (ImageView) dialogContractor.findViewById(R.id.imgCloseDefect);

        submitDefect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = defect.get(defectItem.KEY_DEFECTID);
                final int status = 2;
                new CloseDefect().execute(id,status+"",imageText);
            }
        });

        takePiture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File file = new File(Environment.getExternalStorageDirectory()+File.separator + "imageclose.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        CloseDefectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = defect.get(defectItem.KEY_DEFECTID);
                final int status = 2;
                // show dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(DefectDetailActivity.this);
                builder.setTitle("Close Defect");
                builder.setMessage("Would you like to attrach picture?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogContractor.show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new CloseDefect().execute(id,status+"",null);
                    }
                });
                builder.create().show();

            }
        });
        editDefectDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editDefectDetail.getText().equals("Back")){
                  go_to_back_screen();
                }
                if (editDefectDetail.getText().equals("Save")){
                    final String id = defect.get(defectItem.KEY_DEFECTID);
                    DefectPlace curPlace = (DefectPlace) defectPlaceSpin.getSelectedItem();
                    Description curDes = (Description) descriptionDTSpin.getSelectedItem();
                    new UpdateDefect().execute(id,curPlace.getID(),curDes.getDescriptionID(),editNote.getText().toString());

                }
                if (editDefectDetail.getText().equals("Edit")){
                    // set view
                    defectPlaceSpin.setVisibility(View.VISIBLE);
                    tradeDTSpin.setVisibility(View.VISIBLE);
                    descriptionDTSpin.setVisibility(View.VISIBLE);
                    editNote.setVisibility(View.VISIBLE);
                    editNote.setText(defect.get(defectItem.KEY_NOTE));

                    txtLocation.setVisibility(View.GONE);
                    txtTrade.setVisibility(View.GONE);
                    txtDes.setVisibility(View.GONE);
                    txtNote.setVisibility(View.GONE);

                    new GetTrade().execute("TradeAll");
                    new GetDescription().execute("TradeDescription",defect.get(defectItem.KEY_TRADEID));
                    editDefectDetail.setText("Save");
                }
            }

        });
        tradeDTSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Trade trade = listTrade.get(position);
                new GetDescription().execute("TradeDescription",trade.getTradeID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                listDesc.clear();
                descriptionAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        defectItem = new DefectItem(getApplicationContext());
        defect = defectItem.getDefectDetail();
        String tImageAfter = defect.get(defectItem.KEY_IMAGEAFTER);
        if (tImageAfter == "null"){
            imageClose.setVisibility(View.INVISIBLE);
        }
        Picasso.with(getApplicationContext()).load("http://demo.ifca.com.vn:2603/_IFCA_WebServer/pictures/"+tImageAfter+".JPG")
                .into(imageClose);
    }


    public void go_to_homescreen(View v){
        startActivity(new Intent(getApplication(),MainActivity.class));
    }
    private void setSelectionTrade(String oldTrade){
        for (int i = 0 ; i < listTrade.size() ; i++){
            if (oldTrade.equals(listTrade.get(i).getTradeName())){
                tradeDTSpin.setSelection(i);
            }
        }
    }
    private void setSelectionDescription(String  oldDescriptionID){
        for (int i = 0 ; i < listDesc.size() ; i++){
            if (oldDescriptionID.equals(listDesc.get(i).getDescriptionID()) ){
                descriptionDTSpin.setSelection(i);
            }
        }
    }
    private void setSelectionDefectPlace (String oldDefectPlace){
        for (int i = 0 ; i < listDefectPlace.size() ; i++){
            if (oldDefectPlace.equals(listDefectPlace.get(i).getDefectPlaceName()) ){
                defectPlaceSpin.setSelection(i);
            }
        }
    }
    /*-------------------------------UPDATE DEFECT-------------------------------*/
    private class CloseDefect extends AsyncTask<String, Void, String> {

        String ips= new ipconfig().getIpconfig();
        String URL =ips;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DefectDetailActivity.this);
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
               valuePairs.add(new BasicNameValuePair("closeDefect", "true"));
               valuePairs.add(new BasicNameValuePair("defectID", params[0]));
               valuePairs.add(new BasicNameValuePair("status", params[1]));
               valuePairs.add(new BasicNameValuePair("image",params[2]));
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
            if (s.equals("Success"))
            {
                Toast.makeText(DefectDetailActivity.this,s,Toast.LENGTH_SHORT).show();
                dialogContractor.dismiss();
                editDefectDetail.setText("Back");
            }
            else {
                Toast.makeText(DefectDetailActivity.this,"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
    private class UpdateDefect extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        String ips= new ipconfig().getIpconfig();
        String URL =ips;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DefectDetailActivity.this);
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
                valuePairs.add(new BasicNameValuePair("updateDefect", "true"));
                valuePairs.add(new BasicNameValuePair("DefectID", params[0]));
                valuePairs.add(new BasicNameValuePair("PlaceID", params[1]));
                valuePairs.add(new BasicNameValuePair("DesctiptionID",params[2]));
                valuePairs.add(new BasicNameValuePair("Note",params[3]));
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
            if (s.equals("Success"))
            {
                Toast.makeText(DefectDetailActivity.this,"Update Successfull",Toast.LENGTH_SHORT).show();
                dialogContractor.dismiss();
                editDefectDetail.setText("Back");

                // set view
                defectPlaceSpin.setVisibility(View.GONE);
                tradeDTSpin.setVisibility(View.GONE);
                descriptionDTSpin.setVisibility(View.GONE);
                editNote.setVisibility(View.GONE);


                txtLocation.setVisibility(View.VISIBLE);
                txtTrade.setVisibility(View.VISIBLE);
                txtDes.setVisibility(View.VISIBLE);
                txtNote.setVisibility(View.VISIBLE);

                DefectPlace curPlace = (DefectPlace) defectPlaceSpin.getSelectedItem();
                Trade trade = (Trade) tradeDTSpin.getSelectedItem();
                Description description = (Description) descriptionDTSpin.getSelectedItem();

                txtLocation.setText(curPlace.getDefectPlaceName());
                txtTrade.setText(trade.getTradeName());
                txtDes.setText(description.getDetail());
                txtNote.setText(editNote.getText().toString());

            }
            else {
                Toast.makeText(DefectDetailActivity.this,"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
            if (pDialog.isShowing())
               pDialog.dismiss();
        }
    }
    /*-------------------------------GET DEFECT PLACE -------------------------------*/
    private class GetDefectPlace extends AsyncTask<String, Void, String> {
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
               valuePairs.add(new BasicNameValuePair("get_contend",""));
               valuePairs.add(new BasicNameValuePair("table", "DefectPlace"));
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
                    listDefectPlace.clear();
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String ID = cur.getString("DefectPlaceID");
                        String placeName = cur.getString("DefectPlaceName");
                        DefectPlace defectPlace = new DefectPlace(ID,placeName);
                        listDefectPlace.add(defectPlace);
                    }
                    placeSpinAdapter = new DefectPlaceSpinAdapter(getApplicationContext(),listDefectPlace);
                    defectPlaceSpin.setAdapter(placeSpinAdapter);
                    placeSpinAdapter.notifyDataSetChanged();
                    setSelectionDefectPlace(txtLocation.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
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

    public void go_to_back_screen(){
        super.onBackPressed();
        this.finish();
    }

    /*-----------------------------SHOW PICTURE AFTER TAKE------------------------------*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if user choose OKAY
        int datetime = new Date().getDate();
        if (resultCode == RESULT_OK){
            if (requestCode == CAMERA_REQUEST){
                File file = new File(Environment.getExternalStorageDirectory()+File.separator +
                        "imageclose.jpg");
                Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 500, 250);
                bitmap = RotateBitmap(bitmap,90);
                imageDialogCloseDefect.setImageBitmap(bitmap);
                imageText = getStringImage(bitmap);
            }
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
        }
        @Override
        protected String doInBackground(String... params) {
            try {
               HttpClient client = new DefaultHttpClient();
               HttpPost post = new HttpPost(URL);
               List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
               valuePairs.add(new BasicNameValuePair("get_contend",""));
               valuePairs.add(new BasicNameValuePair("table", params[0]));
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
                    listTrade.clear();
                    for (int i= 0; i<mang.length();  i++){
                        JSONObject cur = mang.getJSONObject(i);
                        String TradeID = cur.getString("TradeID");
                        String TradeName = cur.getString("TradeName");
                        Trade trade = new Trade(TradeID,TradeName);
                        listTrade.add(trade);
                    }
                    tradeSpinAdapter = new TradeSpinAdapter(getApplicationContext(),listTrade);
                    tradeDTSpin.setAdapter(tradeSpinAdapter);
                    tradeSpinAdapter.notifyDataSetChanged();
                    setSelectionTrade((String) txtTrade.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
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
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(URL);
                List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
                valuePairs.add(new BasicNameValuePair("get_contend", ""));
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
                    descriptionAdapter = new DescriptionSpinAdapter(getApplicationContext(),listDesc);
                    descriptionDTSpin.setAdapter(descriptionAdapter);
                    descriptionAdapter.notifyDataSetChanged();
                  //  setSelectionDescription(defect.get(defectItem.KEY_DESCRIPTIONID));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Somethings wrong",Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*-------------------------------CONVER IMAGE TO STRING----------------------------*/
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        //options.outHeight=options.outWidth;
        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;
        if (expectedWidth > reqWidth)
        {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }
        options.inSampleSize =inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
    private File  photoDirectory(){
        //if there is no SD card, create new directory objects to make directory on device
        File directory=null;
        File photoDirectory = null;
        if (Environment.getExternalStorageState() == null) {
            //create new file directory object
            photoDirectory = new File(Environment.getDataDirectory()
                    + "/Images/DefectTracking/test.jpg");

            // if no directory exists, create new directory
            if (!photoDirectory.exists()) {
                photoDirectory.mkdir();
            }

            // if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {

            photoDirectory = new File(
                    Environment.getExternalStorageDirectory()
                            + "/Images/DefectTracking/test.jpg");
            if (!photoDirectory.exists()) {
                directory.mkdir();
            }
        }

        return photoDirectory;
    }

}
