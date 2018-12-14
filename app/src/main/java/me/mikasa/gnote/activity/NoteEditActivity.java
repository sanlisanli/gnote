package me.mikasa.gnote.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import me.mikasa.gnote.base.BaseActivity;
import me.mikasa.gnote.bean.Note;
import me.mikasa.gnote.db.NoteManager;
import me.mikasa.gnote.R;

public class NoteEditActivity extends BaseActivity {
    private EditText et_noteEdit;
    private Toolbar toolbar;
    private TextView mTitle;

    private String id;
    private String content;
    private String category;
    private NoteManager noteManager;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_note_edit;
    }

    @Override
    protected void initData() {
        noteManager=new NoteManager(mContext);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        content=intent.getStringExtra("content");
        category=intent.getStringExtra("currentCate");
    }

    @Override
    protected void initView() {
        toolbar=(Toolbar)findViewById(R.id.toolbar_include);
        et_noteEdit=(EditText)findViewById(R.id.et_note_edit);
        mTitle=(TextView)findViewById(R.id.include_tv_center_title);
        et_noteEdit.setText(content);
        et_noteEdit.setSelection(content.length());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("");
        mTitle.setText("编辑");
    }
    private void goBack(){
        if (TextUtils.isEmpty(et_noteEdit.getText())){
            noteManager.delete(id);
            goToNoteList();
        }else {
            String s=et_noteEdit.getText().toString();
            Note note=new Note();
            //note.setId(id);
            note.setContent(s);
            Date date=new Date();
            note.setLastTime(Long.toString(date.getTime()));
            //note.setCategory(category);
            noteManager.update(note);
            goToNoteList();
        }
    }
    private void goToNoteList(){
        Intent intent=new Intent(NoteEditActivity.this,NoteListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void initListener() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit,menu);
        return true;
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

    @Override
    public void onBackPressed() {
        goBack();
    }
}
