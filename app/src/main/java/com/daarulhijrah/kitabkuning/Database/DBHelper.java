package com.daarulhijrah.kitabkuning.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "DATABASE_KITAB.db";
    public final static int DB_VERSION = 1;
    public static SQLiteDatabase db;

    public static final String TABLE_KITAB = "tbl_kitab";
    public static final String Kitab_id_tabel = "id_tabel";
    public static final String Kitab_id = "id";
    public static final String Kitab_judul_indonesia = "judul_indonesia";
    public static final String Kitab_judul_arab = "judul_arab";
    public static final String Kitab_isi_arab = "isi_arab";
    public static final String Kitab_isi_indonesia = "isi_indonesia";
    public static final String Kitab_url_gambar = "url_gambar";
    public static final String Kitab_url_audio = "url_audio";
    public static final String Kitab_favorite = "favorite";
    public static final String Kitab_recent = "recent";

    private static final String CREATE_TABLE_KITAB = "CREATE TABLE "
            +TABLE_KITAB
            +" ( "
            +Kitab_id_tabel+ " integer,"
            +Kitab_id+ " integer,"
            +Kitab_judul_indonesia+ " text,"
            +Kitab_judul_arab+ " text,"
            +Kitab_isi_indonesia+ " text,"
            +Kitab_isi_arab+ " text,"
            +Kitab_url_gambar+ " text,"
            +Kitab_url_audio+ " text,"
            +Kitab_favorite+ " integer,"
            +Kitab_recent+ " integer"
            +" ) ";

    public DBHelper (Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_KITAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
