package me.mikasa.gnote.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mikasacos on 2018/9/3.
 */

public class SpUtil {
    private static final String preName="mikasaPref";
    private static SpUtil sInstance;
    private final SharedPreferences mPref;
    private SpUtil(Context context){
        mPref=context.getSharedPreferences(preName,Context.MODE_PRIVATE);
    }
    //初始化init
    public static synchronized void init(Context context){
        if (sInstance==null){
            sInstance=new SpUtil(context);
        }
    }
    //调用getInstance()
    public static synchronized SpUtil getInstance(){
        if (sInstance==null){
            throw new IllegalStateException("is not initialized");
        }
        return sInstance;
    }
    public void setBoolean(String key,boolean value){
        mPref.edit().putBoolean(key,value).commit();
    }
    public boolean getBoolean(String key,boolean value){
        return mPref.getBoolean(key, value);
    }
    public String getString(String key,String value){
        return mPref.getString(key, value);
    }
    public void setString(String key,String value){
        mPref.edit().putString(key, value).commit();
    }
    public int getInt(String key,int value){
        return mPref.getInt(key, value);
    }
    public void setInt(String key,int value){
        mPref.edit().putInt(key, value).commit();
    }
    public void remove(String key){
        mPref.edit().remove(key).commit();
    }
    public void clear(){
        mPref.edit().clear().commit();
    }
}
