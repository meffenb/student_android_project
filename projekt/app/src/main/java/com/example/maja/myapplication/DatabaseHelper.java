package com.example.maja.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static int DB_VERSION = 1;

    private final static String DB_NAME = "Shopping";
    private final static String DB_TABLE = "Items";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String SKLEP = "SKLEP";
    private static final String CENA = "CENA";
    private static final String PHOTO = "PHOTO";
    private static final String CATEGORY = "CATEGORY";
    private static final String QR = "QR";
    private static final String NOTE = "NOTE";

    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + "(" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + SKLEP + " TEXT, "
            + CENA + " INTEGER, " + PHOTO + " BLOB, " + CATEGORY + " TEXT, " + QR + " TEXT, " + NOTE + " TEXT)";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean adddata (String nazwa, String sklep, String cena, byte[] photo, String category, String qrcode, String notatki){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, nazwa);
        cv.put(SKLEP, sklep);
        cv.put(CENA, cena);
        cv.put(PHOTO, photo);
        cv.put(CATEGORY, category);
        cv.put(QR, qrcode);
        cv.put(NOTE, notatki);
        long result = db.insert(DB_TABLE, null, cv);
        if (result==-1){
            return false;
        } else {
            return true;
        }
    }

    public Cursor showdata (){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + DB_TABLE;
        Cursor kursor = db.rawQuery(query, null);
        return kursor;
    }

    public boolean edit (String id, String nazwa, String sklep, String cena, byte[] photo, String category, String qrcode, String notatki){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, nazwa);
        cv.put(SKLEP, sklep);
        cv.put(CENA, cena);
        cv.put(PHOTO, photo);
        cv.put(CATEGORY, category);
        cv.put(QR, qrcode);
        cv.put(NOTE, notatki);
        long result = db.update(DB_TABLE, cv, "ID = ?", new String[] { id });
        if (result==-1){
            return false;
        } else {
            return true;
        }
    }

    public boolean remove (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.delete(DB_TABLE, "ID = ?", new String[] {id})>0)
            return true;
        else
            return false;
    }


}
