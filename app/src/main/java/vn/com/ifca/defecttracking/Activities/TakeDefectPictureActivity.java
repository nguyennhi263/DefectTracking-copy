package vn.com.ifca.defecttracking.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import vn.com.ifca.defecttracking.CustomView.DrawCanvasImage;
import vn.com.ifca.defecttracking.Model.DefectDBManager;
import vn.com.ifca.defecttracking.Model.DefectItem;
import vn.com.ifca.defecttracking.R;

public class TakeDefectPictureActivity extends AppCompatActivity {
    Button cameraBtn, clearBtn,saveBtn;
    ImageView imageView;
    DrawCanvasImage imageViewT;
    String imageText;
    Uri selectedImage;
    Canvas canvas = null;
    int defectItemID;
    private static final int CAMERA_REQUEST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DefectItem defectItem = new DefectItem(getApplicationContext());
        HashMap<String, String> defect = defectItem.getDefectDetail();
        String tittle = defect.get(defectItem.KEY_LOCATON);
        this.setTitle(tittle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_defect_picture);
        // back button on tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get defectItemID
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            defectItemID = bundle.getInt("DefectItemID", -1);
        }
        clearBtn = (Button) findViewById(R.id.clearBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        defectItem = new DefectItem(getApplicationContext());
        cameraBtn = (Button) findViewById(R.id.Camerabtn);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageViewT = (DrawCanvasImage) findViewById(R.id.imageViewT);
        /*------------OPEN CAMERA-------------*/
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    imageViewT.clear();
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                else{
                    ActivityCompat.requestPermissions(TakeDefectPictureActivity.this,
                            new String[] {Manifest.permission.CAMERA}, 0);
                }
                }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        imageViewT.clear();
    }
    /*-----------------------------------INTENT ACTIVITY----------------------------*/
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
    public void clear_canvas(View v){
        imageViewT.clear();
    }
    public void go_to_next_screen(View v){
        imageViewT.invalidate();
        Bitmap bmpBase = Bitmap.createBitmap(imageViewT.getWidth(), imageViewT.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bmpBase);
        imageViewT.buildDrawingCache();
        Bitmap bmap = imageViewT.getDrawingCache();
        imageViewT.setImageBitmap(bmap);
        imageText = getStringImage(bmap);
        final DefectDBManager defectDBManager = new DefectDBManager(getApplicationContext());
        defectDBManager.open();
        defectDBManager.update(defectItemID,imageText);
        Intent it = new Intent(TakeDefectPictureActivity.this,ConfirmDefectActivity.class);
        defectDBManager.close();
        startActivity(it);
        this.finish();
    }
    /*-------------------------------CONVER IMAGE TO STRING----------------------------*/
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    /*-----------------------------SHOW PICTURE AFTER TAKE------------------------------*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                imageViewT.setVisibility(View.VISIBLE);
                clearBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageViewT.setImageBitmap(bitmap);
            }
        }
    }
}
