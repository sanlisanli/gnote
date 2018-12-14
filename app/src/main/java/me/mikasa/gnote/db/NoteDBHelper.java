package me.mikasa.gnote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mikasacos on 2018/9/21.
 */

public class NoteDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="gnote.db";//DB_NAME,可能要upgrade
    public static final String TABLE_NAME="gnote";//TABLE_NAME
    public static final String TABLE_CATEGORY="gnotecategory";
    public static final int DB_VERSION=1;
    private static NoteDBHelper sInstance=null;

    public NoteDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    //单例模式
    static synchronized NoteDBHelper getInstance(Context context){
        if (sInstance==null){
            sInstance=new NoteDBHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlNote="create table if not exists "+ TABLE_NAME +"("+
                "id integer primary key,"+
                "content text,"+
                "firsttime text,"+
                "lasttime text,"+ "category text)";
        db.execSQL(sqlNote);
        String sqlCategory="create table if not exists "+ TABLE_CATEGORY +"("+
                "id integer primary key,"+
                "category text,"+ "pos text)";
        db.execSQL(sqlCategory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if exists "+TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
}
