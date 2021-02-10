package com.example.qrcodegenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    Button btn;
    Button Shot;
    EditText mydata;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.qrbtn);
        Shot  = findViewById(R.id.screenshot);
        mydata = findViewById(R.id.data);
        img = findViewById(R.id.qrimg);

        ActivityCompat.requestPermissions(this,new String[]{WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
        PackageManager.PERMISSION_GRANTED);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeButton(v);
                mydata.setText("");
            }
        });
        Shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Captured",Toast.LENGTH_LONG).show();
            }
        });

    }
    public void QRCodeButton(View view){
        QRCodeWriter qrwrite = new QRCodeWriter();
        try {
            BitMatrix btx = qrwrite.encode(mydata.getText().toString(), BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200,200,Bitmap.Config.RGB_565);

            for(int x = 0; x<200;x++){
                for(int y=0;y<200;y++){
                    bitmap.setPixel(x,y,btx.get(x,y)? Color.BLACK:Color.WHITE);
                }
            }
            img.setImageBitmap(bitmap);
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
    public void ScreenShot(View v){
        Date date = new Date();
        CharSequence now = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss",date);

        String filename = Environment.getExternalStorageDirectory()+ "/ScreenShooter/"+ now +".jpg";
        View root = getWindow().getDecorView();
        root.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
        root.setDrawingCacheEnabled(false);

        File file = new File(filename);
       // file.mkdir();
        FileOutputStream fileOutputStream = null;
        try{
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        }catch(FileNotFoundException ex){
                ex.printStackTrace();

        }catch(IOException ex){
                ex.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri= Uri.fromFile(file);
        intent.setDataAndType(uri,"Image/jpeg");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
}