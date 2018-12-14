package me.mikasa.gnote.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.mikasa.gnote.listener.PermissionListener;
import me.mikasa.gnote.utils.ActivityCollector;


/**
 * Created by mikasacos on 2018/9/7.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    private static PermissionListener sListener;//static

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(setLayoutResId());
        mContext = this;
        initData();
        initView();
        initListener();
        //  int hasWritePermission=ContextCompat.checkSelfPermission(this,
        //         Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //  if (hasWritePermission== PackageManager.PERMISSION_GRANTED){
        //     init();
        // }else {
        //     ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        // }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    protected abstract int setLayoutResId();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initListener();

    public void showToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("unchecked")
    protected void startActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    public static void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        Activity topActivity = ActivityCollector.getTopActivity();
        if (topActivity == null) {
            return;
        }
        sListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(topActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(topActivity, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            sListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {//length>0,--->user作出反馈
                    List<String> deniedPermission = new ArrayList<>();
                    //List<String>grantedPermission=new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {//grant-->user反馈
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermission.add(permission);
                        }
                    }
                    if (deniedPermission.isEmpty()) {
                        sListener.onGranted();
                    } else {
                        sListener.onDenied(deniedPermission);
                        //sListener.onGranted(grantedPermission);
                    }
                }
                break;
        }
    }
}
    //@Override
    //public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
     //   if (requestCode==0){
      //      if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
        //        init();
        //    }else {
         //       finish();
         //   }
       // }
   // }

