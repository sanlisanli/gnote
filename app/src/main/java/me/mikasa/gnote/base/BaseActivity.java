package me.mikasa.gnote.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import me.mikasa.gnote.utils.ActivityCollector;


/**
 * Created by mikasacos on 2018/9/7.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(setLayoutResId());
        mContext = this;
        initData();
        initView();
        initListener();
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
}
