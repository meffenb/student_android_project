package com.example.maja.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Show_Item extends AppCompatActivity {

    DatabaseHelper db;
    TextView name, shop, price, qrcode, note, category;
    Button edit, delite;
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__item);

        db = new DatabaseHelper(this);
        name = (TextView) findViewById(R.id.textView7);
        shop = (TextView) findViewById(R.id.textView8);
        price = (TextView) findViewById(R.id.textView10);
        qrcode = (TextView) findViewById(R.id.textView6);
        note = (TextView) findViewById(R.id.textView9);
        category = (TextView) findViewById(R.id.textView14);
        edit = (Button) findViewById(R.id.button);
        delite = (Button) findViewById(R.id.button2);
        photo = (ImageView) findViewById(R.id.imageView);

        final int table = getIntent().getIntExtra("Table_id", 1);
        Cursor kursor = db.showdata();
        kursor.moveToPosition(table);
        final String ID = kursor.getString(0);
        final String NAZWA = kursor.getString(1);
        final String SKLEP = kursor.getString(2);
        final String CENA = kursor.getString(3);
        final byte[] byteArray = kursor.getBlob(4);
        final String CATEGORY = kursor.getString(5);
        final String QR = kursor.getString(6);
       final String NOTE = kursor.getString(7);
        name.setText(NAZWA);
        shop.setText(SKLEP);
        price.setText(CENA);
        category.setText(CATEGORY);
        qrcode.setText(QR);
        note.setText(NOTE);

        if (byteArray != null) {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            photo.setImageBitmap(compressedBitmap);
        } else {
            Toast.makeText(Show_Item.this, "Nie ma zdjecia", Toast.LENGTH_LONG).show();
        }


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        delite.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (db.remove(ID)){
                Toast.makeText(Show_Item.this, "Usunięto", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else {
                Toast.makeText(Show_Item.this, "Nie usunięto", Toast.LENGTH_LONG).show();
            }
        }
    });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Edit_Item.class);
                intent.putExtra("Table_id", ID);
                intent.putExtra("Name", NAZWA);
                intent.putExtra("Shop", SKLEP);
                intent.putExtra("Price", CENA);
                intent.putExtra("Category", CATEGORY);
                intent.putExtra("Photo", byteArray);
                intent.putExtra("Qrcode", QR);
                intent.putExtra("Note", NOTE);
                intent.putExtra("Table", table);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
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
