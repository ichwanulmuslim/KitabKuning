package com.daarulhijrah.kitabkuning.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.daarulhijrah.kitabkuning.Model.Kitab;

import java.util.ArrayList;
import java.util.Iterator;

public class DataSource {

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public DataSource (Context context){
        dbHelper = new DBHelper(context);
    }

    public void open () throws SQLException{
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        if(db.isOpen()){
            dbHelper.close();
        }
    }

    public void saveKitab(ArrayList<Kitab> kitabArrayList){
        for(Iterator<Kitab> iterator = kitabArrayList.iterator();
                iterator.hasNext();){
            Kitab itemKitab = iterator.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.Kitab_id_tabel,itemKitab.getIdTable());
            contentValues.put(DBHelper.Kitab_id,itemKitab.getId());
            contentValues.put(DBHelper.Kitab_judul_indonesia,itemKitab.getJudulIndonesia());
            contentValues.put(DBHelper.Kitab_judul_arab,itemKitab.getJudulArab());
            contentValues.put(DBHelper.Kitab_isi_indonesia,itemKitab.getIsiIndonesia());
            contentValues.put(DBHelper.Kitab_isi_arab,itemKitab.getIsiArab());
            contentValues.put(DBHelper.Kitab_url_gambar,itemKitab.getUrlGambar());
            contentValues.put(DBHelper.Kitab_url_audio,itemKitab.getUrlAudio());
            contentValues.put(DBHelper.Kitab_favorite,itemKitab.getFavorite());
            contentValues.put(DBHelper.Kitab_recent,itemKitab.getRecent());

            db.insert(DBHelper.TABLE_KITAB,null,contentValues);

        }
    }

    public static String [] allColumns = {
            DBHelper.Kitab_id_tabel,
            DBHelper.Kitab_id,
            DBHelper.Kitab_judul_indonesia,
            DBHelper.Kitab_judul_arab,
            DBHelper.Kitab_isi_indonesia,
            DBHelper.Kitab_isi_arab,
            DBHelper.Kitab_url_gambar,
            DBHelper.Kitab_url_audio,
            DBHelper.Kitab_favorite,
            DBHelper.Kitab_recent
    };

    public ArrayList<Kitab> getItemKitab(int idTabel){
        ArrayList<Kitab> kitabArrayList = new ArrayList<Kitab>();

        Cursor cursor = db.query(DBHelper.TABLE_KITAB, allColumns, DBHelper.Kitab_id_tabel+" = "+idTabel, null, null, null, DBHelper.Kitab_id_tabel+" asc");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Kitab itemKitab = cursorToKitab(cursor,"");
            kitabArrayList.add(itemKitab);
            cursor.moveToNext();
        }
        return kitabArrayList;
    }

    public ArrayList<Kitab> getAllData(){
        ArrayList<Kitab> kitabArrayList = new ArrayList<Kitab>();

        Cursor cursor = db.query(DBHelper.TABLE_KITAB, allColumns, null, null, null, null, DBHelper.Kitab_id+" asc");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Kitab kitab = cursorToKitab(cursor, "");
            kitabArrayList.add(kitab);
            cursor.moveToNext();
        }
        return kitabArrayList;
    }

    public ArrayList<Kitab> getFavoriteData(){
        ArrayList<Kitab> kitabArrayList = new ArrayList<Kitab>();

        Cursor cursor = db.query(DBHelper.TABLE_KITAB, allColumns, DBHelper.Kitab_favorite+"=1", null, null, null, DBHelper.Kitab_id+" asc");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Kitab kitab = cursorToKitab(cursor, "");
            kitabArrayList.add(kitab);
            cursor.moveToNext();
        }
        return kitabArrayList;
    }

    public ArrayList<Kitab> getRecentData(){
        ArrayList<Kitab> kitabArrayList = new ArrayList<Kitab>();

        Cursor cursor = db.query(DBHelper.TABLE_KITAB, allColumns, DBHelper.Kitab_recent+"=1", null, null, null, DBHelper.Kitab_id+" asc");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Kitab kitab = cursorToKitab(cursor, "");
            kitabArrayList.add(kitab);
            cursor.moveToNext();
        }
        return kitabArrayList;
    }


    private Kitab cursorToKitab(Cursor cursor, String text){
        Kitab itemKitab = new Kitab();
        if(cursor!=null){
            itemKitab.setIdTable(cursor.getInt(0));
            itemKitab.setId(cursor.getInt(1));
            itemKitab.setJudulIndonesia(cursor.getString(2));
            itemKitab.setJudulArab(cursor.getString(3));
            itemKitab.setIsiIndonesia(cursor.getString(4));
            itemKitab.setIsiArab(cursor.getString(5));
            itemKitab.setUrlGambar(cursor.getString(6));
            itemKitab.setUrlAudio(cursor.getString(7));
            itemKitab.setFavorite(cursor.getInt(8));
            itemKitab.setRecent(cursor.getInt(9));
        }
        return itemKitab;
    }

    public ArrayList<Kitab> getSearchText(String textSearch){

        ArrayList<Kitab> kitabArrayList = new ArrayList<Kitab>();
        Cursor cursor = db.query(DBHelper.TABLE_KITAB,allColumns,
                DBHelper.Kitab_judul_arab+ " like '%"+textSearch+"%' OR "
                        +DBHelper.Kitab_isi_arab+" like '%"+textSearch+"%' OR "
                        +DBHelper.Kitab_judul_indonesia+" like '%"+textSearch+"%' OR "
                        +DBHelper.Kitab_isi_indonesia+" like '%"+textSearch+"%' ",
                null, null, null,
                DBHelper.Kitab_id+" asc ");

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Kitab kitab = cursorToKitab(cursor, "");
            kitabArrayList.add(kitab);
            cursor.moveToNext();
        }


        return kitabArrayList;
    }

    public void deleteTableKitab(){
        db.execSQL("DELETE FROM "+DBHelper.TABLE_KITAB);
        db.execSQL("VACUUM");
    }

    public void updateFavorite(long id, int intFav){
        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(DBHelper.Kitab_favorite, intFav);
//        values.put(TOTAL_PRICE, total_price);

        // ask the database object to update the database row of given rowID
        try {db.update(DBHelper.TABLE_KITAB, values, DBHelper.Kitab_id_tabel + "=" + id, null);}
        catch (Exception e)
        {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

    public void resetRecent(){
        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(DBHelper.Kitab_recent, 0);

        // ask the database object to update the database row of given rowID
        try {db.update(DBHelper.TABLE_KITAB, values, null, null);}
        catch (Exception e)
        {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

    public void updateRecent(long id, int recent){
        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(DBHelper.Kitab_recent, recent);

        // ask the database object to update the database row of given rowID
        try {db.update(DBHelper.TABLE_KITAB, values, DBHelper.Kitab_id_tabel + "=" + id, null);}
        catch (Exception e)
        {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

}
