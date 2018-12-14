package me.mikasa.gnote.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import me.mikasa.gnote.R;
import me.mikasa.gnote.base.BaseActivity;
import me.mikasa.gnote.bean.Category;
import me.mikasa.gnote.db.NoteManager;
import me.mikasa.gnote.utils.SpUtil;

/**
 * Created by mikasacos on 2018/9/26.
 */

public class CategoryAddActivity extends BaseActivity {
    private EditText cateName;
    private Toolbar toolbar;
    private TextView mTitle;
    private ImageView deleteImg;

    private int pos;
    private String cate;
    private NoteManager noteManager;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_category_add;
    }

    @Override
    protected void initData() {
        SpUtil.init(mContext);
        noteManager=new NoteManager(mContext);
        Intent intent=getIntent();
        pos=intent.getIntExtra("cateSize",-1);
    }

    @Override
    protected void initView() {
        cateName=(EditText)findViewById(R.id.et_cate_name);
        deleteImg=(ImageView)findViewById(R.id.delete);
        toolbar=(Toolbar)findViewById(R.id.toolbar_include);
        mTitle=(TextView)findViewById(R.id.include_tv_center_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("");
        mTitle.setText("新建文件夹");
        TextWatcher watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(cateName.getText().toString())){
                    deleteImg.setVisibility(View.VISIBLE);
                }else {
                    deleteImg.setVisibility(View.INVISIBLE);
                    cateName.setHint("笔记文件夹名称");
                }
            }
        };
        cateName.addTextChangedListener(watcher);
    }

    @Override
    protected void initListener() {
       deleteImg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!TextUtils.isEmpty(cateName.getText().toString())){
                   cateName.setText("");
                   cateName.setHint("笔记文件夹名称");
               }
           }
       });
    }
    private void goBack(){
        cate=cateName.getText().toString();
        if (cate.length()>0){
            Category category=new Category();
            int id=SpUtil.getInstance().getInt("categoryNum",-1);
            category.setId(String.valueOf(id+1));
            category.setCategory(cate);
            category.setPos(String.valueOf(pos+1));
            noteManager.insertCategory(category);
            SpUtil.getInstance().setInt("categoryNum",id+1);
            goToCateManager();
        }else{
            goToCateManager();
        }
    }
    private void goToCateManager(){
        Intent intent=new Intent(this,CategoryManagerActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                goBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
