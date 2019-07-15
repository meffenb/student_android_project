package com.example.maja.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;

public class Add_Item extends AppCompatActivity {

    DatabaseHelper db;
    Button add_data, add_qr, add_photo;
    EditText add_name, add_shop, add_price, add_note;
    String name, shop, price, qr, note;
    TextView qr_add;
    ImageView photo;
    Spinner category;
    String CATEGORY []={"Odzież", "Kosmetyki", "Chemia", "Elektronika", "Jedzenie"};
    ArrayAdapter<String> adapter;
    String categry_number;
    Bitmap bitmap;
    static final int CAPTURE_IMAGE_REQUEST = 1;
    byte[] byteArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__item);

        db = new DatabaseHelper(this);
        add_data = (Button)findViewById(R.id.buttonadd);
        add_qr = (Button) findViewById(R.id.buttonqr);
        add_photo = (Button) findViewById(R.id.buttonadd3);
        add_name = (EditText) findViewById(R.id.name);
        add_shop = (EditText) findViewById(R.id.shop);
        add_price = (EditText) findViewById(R.id.price);
        qr_add = (TextView) findViewById(R.id.textView5);
        add_note = (EditText) findViewById(R.id.note);
        photo = (ImageView) findViewById(R.id.imageView2);
        category = (Spinner) findViewById(R.id.textView13);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CATEGORY);
        category.setAdapter(adapter);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String name1 = getIntent().getStringExtra("Name");
        String shop1 = getIntent().getStringExtra("Shop");
        String price1 = getIntent().getStringExtra("Price");
        String category1 = getIntent().getStringExtra("Category");
        byte[] byteArray1 = getIntent().getByteArrayExtra("Photo");
        String qr1 = getIntent().getStringExtra("Qrcode");
        String note1 = getIntent().getStringExtra("Note");

        add_name.setText(name1);
        add_shop.setText(shop1);
        add_price.setText(price1);
        if (byteArray1 != null) {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
            photo.setImageBitmap(compressedBitmap);
            byteArray = byteArray1;
        }
        qr_add.setText(qr1);
        add_note.setText(note1);
        if (category1 == "Odzież"){
            category.setSelection(adapter.getPosition("Odzież"));;
        } else if (category1 == "Kosmetyki"){
            category.setSelection(adapter.getPosition("Category 2"));;
        } else if (category1 == "Chemia"){
            category.setSelection(adapter.getPosition("Chemia"));;
        } else if (category1 == "Elektronika"){
            category.setSelection(adapter.getPosition("Elektronika"));;
        } else if (category1 == "Jedzenie"){
            category.setSelection(adapter.getPosition("Jedzenie"));;
        }

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categry_number = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = add_name.getText().toString();
                shop = add_shop.getText().toString();
                price = add_price.getText().toString();
                qr = qr_add.getText().toString();
                note = add_note.getText().toString();
                // jeżeli są wpisane: nazwa, sklep i cena to zapisuje dane w bazie danych i wraca do odna z listą przemditów, w rpzywinym razie prosi o ich uzuepłnienie
                if (!name.equals("") && !shop.equals("") && !price.equals("") && db.adddata(name, shop, price, byteArray, categry_number, qr, note)){
                    Toast.makeText(Add_Item.this, "Dodano", Toast.LENGTH_LONG).show();
                    add_name.setText("");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(Add_Item.this, "Uzupełnij pola: Nazwa, Sklep, Cena.", Toast.LENGTH_LONG).show();
                }
            }
        });

        add_qr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int activity = 1;
                    name = add_name.getText().toString();
                    shop = add_shop.getText().toString();
                    price = add_price.getText().toString();
                    note = add_note.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), BarcodeScanner.class);
                    intent.putExtra("Activity", activity);
                    intent.putExtra("Name", name);
                    intent.putExtra("Shop", shop);
                    intent.putExtra("Category", categry_number);
                    intent.putExtra("Photo", byteArray);
                    intent.putExtra("Price", price);
                    intent.putExtra("Note", note);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
        });

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
        }
        });
    }

    private void captureImage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK){
        bitmap = (Bitmap) data.getExtras().get("data");
        photo.setImageBitmap(bitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
        byteArray = stream.toByteArray();
        }
       }

  @Override
   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            }else{
                Toast.makeText(Add_Item.this, "Wymagana jest zgoda na używanie aparatu", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
