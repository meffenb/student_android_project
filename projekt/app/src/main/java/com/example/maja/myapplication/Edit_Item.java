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

public class Edit_Item extends AppCompatActivity {

    DatabaseHelper db;
    Button edit, add_qr,  add_photo;
    EditText add_name, add_shop, add_price, add_note;
    String name, shop, price, qr, note;
    TextView qr_add;
    ImageView photo;
    Spinner category;
    String CATEGORY []={"Odzież", "Kosmetyki", "Chemia", "Elektronika", "Jedzenie"};
    ArrayAdapter<String> adapter;
    private static final int CAPTURE_PHOTO = 1;
    String categry_number;
    Bitmap bitmap;
    String mCurrentPhotoPath;
    static final int CAPTURE_IMAGE_REQUEST = 1;
    byte[] byteArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__item);


        db = new DatabaseHelper(this);
        edit = (Button) findViewById(R.id.button);
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

        final String ID = getIntent().getStringExtra("Table_id");
        final int table = getIntent().getIntExtra("Table", 1);
        String name1 = getIntent().getStringExtra("Name");
        String shop1 = getIntent().getStringExtra("Shop");
        String price1 = getIntent().getStringExtra("Price");
        String category1 = getIntent().getStringExtra("Category");
        final byte[] byteArray1 = getIntent().getByteArrayExtra("Photo");
        String qr1 = getIntent().getStringExtra("Qrcode");
        String note1 = getIntent().getStringExtra("Note");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        add_name.setText(name1);
        add_shop.setText(shop1);
        add_price.setText(price1);
        if (byteArray1 != null) {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
            photo.setImageBitmap(compressedBitmap);
        }else {
            Toast.makeText(Edit_Item.this, "Brak zdjęcia", Toast.LENGTH_LONG).show();
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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = add_name.getText().toString();
                shop = add_shop.getText().toString();
                price = add_price.getText().toString();
                qr = qr_add.getText().toString();
                note = add_note.getText().toString();
                if (!name.equals("") && !shop.equals("") && !price.equals("") && db.edit(ID, name, shop, price, byteArray1, categry_number, qr, note)){
                    Toast.makeText(Edit_Item.this, "Zaaktualizowano", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Show_Item.class);
                    intent.putExtra("Table_id", table);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(Edit_Item.this, "Nie zaaktualizowano, sprawdz czy wypisałeś wymagane dane: nazwa, sklep, cena", Toast.LENGTH_LONG).show();
                }
            }
        });

        add_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int activity = 2;
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
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            photo.setImageBitmap(bitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
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
                Toast.makeText(Edit_Item.this, "Wymagana jest zgoda na używanie aparatu", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
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
