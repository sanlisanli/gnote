package me.mikasa.gnote.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mikasa on 2018/12/28.
 */
public class SPUtil {
    public static final String NAME="mikasa";
    public static void putString(Context context,String key,String value){
        SharedPreferences sp=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }
    public static String getString(Context context,String key,String def){
        SharedPreferences sp=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getString(key,def);
    }
    public static void putInt(Context context,String key,int value){
        SharedPreferences sp=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }
    public static int getInt(Context context,String key,int def){
        SharedPreferences sp=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getInt(key,def);
    }
    public static void putBoolean(Context context,String key,boolean value){
        SharedPreferences sp=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }
    public static boolean getBoolean(Context context,String key,boolean def){
        SharedPreferences sp=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key,def);
    }
    public static void deleteKey(Context context,String key){
        SharedPreferences sp=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }
    public static void deleteAll(Context context){
        SharedPreferences sp=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
