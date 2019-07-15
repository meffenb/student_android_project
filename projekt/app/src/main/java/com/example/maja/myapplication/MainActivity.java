package com.example.maja.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public int send_ID;

    DatabaseHelper db;
    Button add_data2;
    ListView itemlist;
    ArrayList<String> listItem;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        listItem  = new ArrayList<>();
        add_data2 = (Button)findViewById(R.id.add_data2);
        itemlist = findViewById(R.id.item_list);

        viewData();

        itemlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                send_ID = i;
                Intent intent = new Intent(getApplicationContext(), Show_Item.class);
                intent.putExtra("Table_id", send_ID);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    private void viewData () {
        Cursor kursor = db.showdata();
        if (kursor.getCount() == 0){
            Toast.makeText(MainActivity.this, "Brak danych do wyświetlenia", Toast.LENGTH_LONG).show();
        } else {
            while (kursor.moveToNext()) {
                String NAZWA = kursor.getString(1);
                listItem.add(NAZWA);
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
            itemlist.setAdapter(adapter);
        }
    }

    public void openUnitsActivity(View view){
        Intent intent = new Intent(this, Add_Item.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
