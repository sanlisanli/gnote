package me.mikasa.gnote.base;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import me.mikasa.gnote.listener.PermissionListener;
import me.mikasa.gnote.utils.ActivityCollector;

/**
 * Created by mikasacos on 2019/1/2.
 */

public abstract class BasePermissionActivity extends BaseActivity {
    private static PermissionListener sListener;//static
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
                        sListener.onDenied();
                        //sListener.onGranted(grantedPermission);
                    }
                }
                break;
        }
    }

    public interface PermissionListener{//内部接口
        void onGranted();
        void onDenied();//List<String> deniedPermission
    }
}
