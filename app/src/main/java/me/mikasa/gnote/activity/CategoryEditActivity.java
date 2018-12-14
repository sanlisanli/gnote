package me.mikasa.gnote.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.List;

import me.mikasa.gnote.R;
import me.mikasa.gnote.base.BaseActivity;
import me.mikasa.gnote.bean.Category;
import me.mikasa.gnote.bean.Note;
import me.mikasa.gnote.db.NoteManager;
import me.mikasa.gnote.utils.SpUtil;

public class CategoryEditActivity extends BaseActivity {
    private EditText et_cate_edit;
    private ImageView deleteImg;
    private Toolbar toolbar;
    private TextView mTitle;
    private String cate;
    private String catePos;
    private NoteManager noteManager;
    private String cateId;
    private List<Note>mNoteList=new ArrayList<>();


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_category_add;
    }

    @Override
    protected void initData() {
        SpUtil.init(mContext);
        Intent intent=getIntent();
        cate=intent.getStringExtra("category");
        cateId=intent.getStringExtra("cateId");
        //catePos=intent.getStringExtra("pos");
        noteManager=new NoteManager(mContext);
    }

    @Override
    protected void initView() {
        et_cate_edit=(EditText)findViewById(R.id.et_cate_name);
        et_cate_edit.setText(cate);
        deleteImg=(ImageView)findViewById(R.id.delete);
        toolbar=(Toolbar)findViewById(R.id.toolbar_include);
        mTitle=(TextView)findViewById(R.id.include_tv_center_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("");
        mTitle.setText("编辑文件夹");
        TextWatcher watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(et_cate_edit.getText().toString())){
                    deleteImg.setVisibility(View.VISIBLE);
                }else {
                    deleteImg.setVisibility(View.INVISIBLE);
                    et_cate_edit.setHint("笔记文件夹名称");
                }
            }
        };
        et_cate_edit.addTextChangedListener(watcher);
    }

    @Override
    protected void initListener() {
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_cate_edit.getText().toString())){
                    et_cate_edit.setText("");
                    et_cate_edit.setHint("笔记文件夹名称");
                }
            }
        });
    }
    private void deleteCategory(){
        //笔记不为空删除笔记,为空仅删除文件夹
        if (noteManager.selectCategory(cate)!=null){
            mNoteList=noteManager.selectCategory(cate);
            for (Note note:mNoteList){
                noteManager.delete(note.getId());//删除笔记
            }
        }
        noteManager.deleteCategory(cateId);//删除文件夹
        SpUtil.getInstance().setString("currentCate","所有");//返回所有
        goToCateManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category_edit,menu);
        return true;
    }
    private void goBack(){
        String s=et_cate_edit.getText().toString();
        if (!TextUtils.isEmpty(s)){
            Category category=new Category();
            //category.setId(cateId);
            category.setCategory(s);
            //category.setPos(catePos);
            noteManager.updateCategory(category);//更新文件夹
            goToCateManager();
        }else {
            showDeleteDialog();//文件夹名称为为空
        }
    }
    private void showDeleteCateAndNoteDialog(){
        AlertDialog dialog=new AlertDialog.Builder(mContext).create();
        dialog.setMessage("你确定要删除该文件夹及其笔记吗？");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(et_cate_edit.getText().toString())){
                    et_cate_edit.setHint("笔记文件夹名称");
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCategory();
                goToCateManager();
            }
        });
        dialog.show();
    }
    private void showDeleteDialog(){
        AlertDialog dialog=new AlertDialog.Builder(mContext).create();
        dialog.setMessage("文件夹名称不能为空，你确定要删除该文件夹吗？");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_cate_edit.setHint("笔记文件夹名称");
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteManager.deleteCategory(cateId);//删除文件夹
                goToCateManager();
            }
        });
        dialog.show();
    }
    private void goToCateManager(){
        Intent intent=new Intent(this,CategoryManagerActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                goBack();//判断editView
                return true;
            case R.id.delete_cate:
                showDeleteCateAndNoteDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}
