package me.mikasa.gnote.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.mikasa.gnote.R;
import me.mikasa.gnote.base.BaseActivity;
import me.mikasa.gnote.bean.Note;
import me.mikasa.gnote.db.NoteManager;
import me.mikasa.gnote.utils.SpUtil;

public class NoteAddActivity extends BaseActivity{
    private EditText et_addNote;
    private Toolbar toolbar;
    private TextView mTitle;

    private NoteManager noteManager;
    private String currentCate;
    @Override
    protected int setLayoutResId() {
        return R.layout.activity_note_add;
    }

    @Override
    protected void initData() {
        SpUtil.init(mContext);
        noteManager=new NoteManager(mContext);
        Intent intent=getIntent();
        currentCate=intent.getStringExtra("currentCate");
    }

    @Override
    protected void initView() {
        toolbar=(Toolbar)findViewById(R.id.toolbar_include);
        et_addNote=(EditText)findViewById(R.id.et_note_add);
        mTitle=(TextView)findViewById(R.id.include_tv_center_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("");
        mTitle.setText("编辑");
    }

    @Override
    protected void initListener() {
    }
    private void goBack(){
        if (!TextUtils.isEmpty(et_addNote.getText())){
            String s=et_addNote.getText().toString();
            Note note=new Note();
            int i=SpUtil.getInstance().getInt("noteId",-1);
            note.setId(String.valueOf(i+1));
            note.setContent(s);
            Date date=new Date();
            SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日");
            String firstTime=sdf.format(date);
            note.setFirstTime(firstTime);
            note.setLastTime(Long.toString(date.getTime()));
            note.setCategory(currentCate);
            noteManager.insert(note);
            SpUtil.getInstance().setInt("noteId",i+1);
            goToNoteList();
        }else {
            goToNoteList();
        }
    }
    private void goToNoteList(){
        Intent intent=new Intent(this,NoteListActivity.class);
        intent.putExtra("currentCate",currentCate);
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
