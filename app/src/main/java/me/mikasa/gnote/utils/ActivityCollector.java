package me.mikasa.gnote.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikasacos on 2018/9/22.
 */

public class ActivityCollector {
    private static List<Activity>activityList=new ArrayList<>();
    public static void addActivity(Activity act){
        activityList.add(act);
    }
    public static void removeActivity(Activity act){
        activityList.remove(act);
    }
    public static Activity getTopActivity(){
        if (activityList.isEmpty()){
            return null;
        }else {
            return activityList.get(activityList.size()-1);//size()-1
        }
    }
}
