package com.example.wn10.bproje.Storage;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {
    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Veri9.db";
    public static final String TABLE_NAME = "OgrBilgi";
    public static final String COLUMN_OKUL = "Okul";
    public static final String COLUMN_VAKIT = "Vakit";
    public static final String COLUMN_AD = "Ad";
    public static final String COLUMN_SOYAD = "Soyad";
    public static final String COLUMN_TELEFONNO = "TelefonNo";
    public static final String COLUMN_KOORDINATLAT = "KoordinatLat";
    public static final String COLUMN_KOORDINATLNG = "KoordinatLng";


    //initialize the database
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME  +
                " ("+COLUMN_OKUL+" text,"+ COLUMN_VAKIT +" text,"+COLUMN_AD +" text,"+ COLUMN_SOYAD +" text,"+ COLUMN_TELEFONNO +" text,"+COLUMN_KOORDINATLAT+" real,"+COLUMN_KOORDINATLNG+" real)";
        db.execSQL(CREATE_TABLE);
    }
    /*
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME  +
                " (Okul text, Vakit text,Ad text,Soyad text, TelefonNo text,KoordinatLat real,KoordinatLng real)";
        db.execSQL(CREATE_TABLE);

     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}
    public String loadHandler2(String Okul, String Vakit) {
        String result = "";
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_OKUL + " = " + "'" +  Okul + "'" + " AND " + COLUMN_VAKIT + " = " +  "'" + Vakit  +"'"  ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String result_2 = cursor.getString(2);
            String result_3 = cursor.getString(3);
            result+=( result_2 + " " + result_3 );
        }
        cursor.close();
        db.close();
        return result;
    }
    public ArrayList<String> loadHandler(String Okul, String Vakit) {
        ArrayList<String> result = new ArrayList<String>();
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_OKUL + " = " + "'" +  Okul + "'" + " AND " + COLUMN_VAKIT + " = " +  "'" + Vakit  +"'"  ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String result_2 = cursor.getString(2);
            String result_3 = cursor.getString(3);
            result.add( result_2 + " " + result_3 );
        }
        cursor.close();
        db.close();
        return result;
    }
    public ArrayList<String> TelefonNoHandler(String Okul, String Vakit) {
        ArrayList<String> result = new ArrayList<String>();
       // String result = "";
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_OKUL + " = " + "'" +  Okul + "'" + " AND " + COLUMN_VAKIT + " = " +  "'" + Vakit  +"'"  ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String result_0 = cursor.getString(4);
            result.add( result_0 );
            //result+=result_0;
        }
        cursor.close();
        db.close();
        return result;
    }
    public ArrayList<Double> KoordinatLatHandler(String Okul, String Vakit) {
        ArrayList<Double> result = new ArrayList<Double>();
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_OKUL + " = " + "'" +  Okul + "'" + " AND " + COLUMN_VAKIT + " = " +  "'" + Vakit  +"'"  ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Double result_0 = cursor.getDouble(5);
            result.add( result_0 );
        }
        cursor.close();
        db.close();
        return result;
    }
    public ArrayList<Double> KoordinatLngHandler(String Okul, String Vakit) {
        ArrayList<Double> result = new ArrayList<Double>();
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_OKUL + " = " + "'" +  Okul + "'" + " AND " + COLUMN_VAKIT + " = " +  "'" + Vakit  +"'"  ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Double result_0 = cursor.getDouble(6);
            result.add( result_0 );
        }
        cursor.close();
        db.close();
        return result;
    }
    public void addHandler(veri veri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("KoordinatLat", veri.getLat());
        values.put("KoordinatLng", veri.getLng());
        values.put("Ad", veri.getAd());
        values.put("Okul", veri.getOkul());
        values.put("Soyad", veri.getSoyad());
        values.put("TelefonNo", veri.getTelefonNo());
        values.put("Vakit", veri.getVakit());
        db.insert("OgrBilgi",null,values);
        db.close();
    }
    //public veri findHandler(String studentname) {}
    public boolean deleteHandler(String Ad) {
        boolean result = false;
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_AD + " = "  + "'" + Ad + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        veri veri = new veri();
        if (cursor.moveToFirst()) {
            veri.setAd(cursor.getString(2));
            db.delete(TABLE_NAME, COLUMN_AD + "=?" ,
                    new String[] {
                String.valueOf(veri.getAd())
            });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
    public boolean updateHandler(String Okul, String Vakit, String Ad, String Soyad, String telefonNo, double lat, double lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OKUL, Okul);
        values.put(COLUMN_VAKIT, Vakit);
        values.put(COLUMN_AD, Ad);
        values.put(COLUMN_SOYAD, Soyad);
        values.put(COLUMN_TELEFONNO, telefonNo);
        values.put(COLUMN_KOORDINATLAT, lat);
        values.put(COLUMN_KOORDINATLNG, lng);
        return db.update(TABLE_NAME, values, COLUMN_OKUL + "=" + Okul, null) > 0;
    }
}
