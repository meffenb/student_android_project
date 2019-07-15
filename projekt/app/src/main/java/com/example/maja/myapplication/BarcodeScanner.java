package com.example.maja.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;

public class BarcodeScanner extends AppCompatActivity {

    Button save;
    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    BarcodeDetector barcodeDetector;
    String qrname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
        textView = (TextView) findViewById(R.id.cameraText);
        save = (Button) findViewById(R.id.buttonsave);

final int activity = getIntent().getIntExtra("Activity", 1);
final String name = getIntent().getStringExtra("Name");
final String shop = getIntent().getStringExtra("Shop");
final String price = getIntent().getStringExtra("Price");
final String category = getIntent().getStringExtra("Category");
final byte[] byteArray1 = getIntent().getByteArrayExtra("Photo");
final String note = getIntent().getStringExtra("Note");

        barcodeDetector = new BarcodeDetector.Builder(this)
        .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this,barcodeDetector).
        setRequestedPreviewSize(640,480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
        return;
        }
        try {
        cameraSource.start(surfaceView.getHolder());
        }catch (IOException e){
        e.printStackTrace();
        }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        cameraSource.stop();
        }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
        @Override
        public void release() {

        }

        @Override
        public void receiveDetections(Detector.Detections<Barcode> detections) {
        final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
        if (qrCodes.size() != 0){
        textView.post(new Runnable() {
@Override
public void run() {
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        qrname = qrCodes.valueAt(0).displayValue;
        textView.setText(qrname);
        }
        });
        }
        }
        });

        save.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        if (activity == 1){
        Intent intent = new Intent(getApplicationContext(), Add_Item.class);
        intent.putExtra("Name", name);
        intent.putExtra("Shop", shop);
        intent.putExtra("Price", price);
        intent.putExtra("Category", category);
        intent.putExtra("Photo", byteArray1);
        intent.putExtra("Note", note);
        intent.putExtra("Qrcode", qrname);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (activity == 2){
        Intent intent = new Intent(getApplicationContext(), Edit_Item.class);
        intent.putExtra("Name", name);
        intent.putExtra("Shop", shop);
        intent.putExtra("Price", price);
        intent.putExtra("Photo", byteArray1);
        intent.putExtra("Note", note);
        intent.putExtra("Qrcode", qrname);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        }
        });
        }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
