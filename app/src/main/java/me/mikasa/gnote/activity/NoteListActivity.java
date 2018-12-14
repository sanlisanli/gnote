package me.mikasa.gnote.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.mikasa.gnote.R;
import me.mikasa.gnote.adapter.CategoryAdapter;
import me.mikasa.gnote.adapter.NoteAdapter;
import me.mikasa.gnote.base.BaseActivity;
import me.mikasa.gnote.bean.Category;
import me.mikasa.gnote.bean.Note;
import me.mikasa.gnote.contract.GetNoteContract;
import me.mikasa.gnote.db.NoteManager;
import me.mikasa.gnote.listener.OnRecyclerViewItemClickListener;
import me.mikasa.gnote.listener.PermissionListener;
import me.mikasa.gnote.presenter.GetNotePresenterImpl;
import me.mikasa.gnote.utils.SpUtil;

public class NoteListActivity extends BaseActivity implements GetNoteContract.View,
        OnRecyclerViewItemClickListener{
    private Toolbar mToolbar;
    private RecyclerView mRecyclerview;
    private FloatingActionButton note_add;
    private TextView mTitle;
    private ImageView iv_update,iv_search;
    private DrawerLayout drawerLayout;
    private RecyclerView rv_drawer;
    private NoteAdapter mAdapter;
    private GetNotePresenterImpl mPresenter;
    private List<Note>mNoteList=new ArrayList<>();
    private List<Category>mCategoryList=new ArrayList<>();
    private boolean isDeleteModel=false;
    private NoteManager noteManager;
    private CategoryAdapter cate_adapter;
    private HeaderAndFooterWrapper rv_wrapper;
    private LinearLayout settings,cate_manager;
    private static String currentCate;
    private ActionBarDrawerToggle toggle;
    private final String[] permissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_note_list;
    }

    @Override
    protected void initData() {
        SpUtil.init(mContext);
        Intent intent=getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra("currentCate"))){
            currentCate=intent.getStringExtra("currentCate");
        }else if (SpUtil.getInstance().getString("currentCate","")!=null){
            currentCate=SpUtil.getInstance().getString("currentCate","所有");
        }else {
            currentCate="所有";
        }
        noteManager=new NoteManager(mContext);
    }

    @Override
    protected void initView() {
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        iv_update=(ImageView)findViewById(R.id.note_update);
        iv_search=(ImageView)findViewById(R.id.note_search);
        mRecyclerview=(RecyclerView)findViewById(R.id.rv_note);
        note_add=(FloatingActionButton)findViewById(R.id.note_add);
        mTitle=(TextView)findViewById(R.id.tv_title);
        mTitle.setText(currentCate);
        initDrawer();
        mAdapter=new NoteAdapter(mContext);
        mRecyclerview.setLayoutManager(new GridLayoutManager(mContext,2));
        mRecyclerview.setAdapter(mAdapter);
        mPresenter=new GetNotePresenterImpl(this);
        requestRuntimePermission(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                getNote();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                for (String s:deniedPermission){
                    showToast("你决绝了权限："+s);
                }
                finish();
            }
        });
    }
    private void getNote(){
        mPresenter.getNote(mContext,currentCate);
    }
    private void initDrawer(){
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,mToolbar,
                R.string.app_name,R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        rv_drawer=(RecyclerView)findViewById(R.id.rv_drawer_left);
        rv_drawer.setLayoutManager(new LinearLayoutManager(mContext));//setLayoutManager
        cate_adapter=new CategoryAdapter(mContext,R.layout.item_category,mCategoryList);
        initHeaderAnFooter();
    }
    private void initHeaderAnFooter(){
        View header= LayoutInflater.from(mContext).inflate(R.layout.layout_drawer_header,rv_drawer,false);
        View footer= LayoutInflater.from(mContext).inflate(R.layout.layout_drawer_footer,rv_drawer,false);
        settings=(LinearLayout)footer.findViewById(R.id.settings);
        cate_manager=(LinearLayout)footer.findViewById(R.id.category_manager);
        rv_wrapper=new HeaderAndFooterWrapper(cate_adapter);
        rv_wrapper.addHeaderView(header);
        rv_wrapper.addFootView(footer);
        rv_drawer.setAdapter(rv_wrapper);//setAdapter
    }

    @Override
    protected void initListener() {
        iv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("更新到云 待开发");
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("搜索 待开发");
            }
        });
        mAdapter.setOnRecyclerViewItemClickListener(this);
        note_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.getInstance().setString("categoryCate",currentCate);
                Intent intent=new Intent(mContext,NoteAddActivity.class);
                intent.putExtra("currentCate",currentCate);
                startActivity(intent);
            }
        });
        cate_adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                String cate=mCategoryList.get(position-1).getCategory();
                currentCate=cate;
                mNoteList.clear();
                mAdapter.updateData(mNoteList);
                mTitle.setText(currentCate);
                mPresenter.getNote(mContext,currentCate);
                hideDrawer();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("待开发");
            }
        });
        cate_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.getInstance().setString("currentCate",currentCate);
                Intent intent=new Intent(mContext,CategoryManagerActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void getNoteSuccess(List<Note> list,List<Category>categoryList) {
        mNoteList=list;
        mCategoryList=categoryList;
        mAdapter.updateData(list);
        cate_adapter.updateData(categoryList);
    }

    @Override
    public void getError(String msg) {
        getCategory();
        Snackbar snackbar=Snackbar.make(note_add,currentCate+"文件夹空空如也哦",Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
        snackbar.show();
    }
    private void getCategory(){
        List<Category>list= noteManager.selectAllCategoryBean();
        mCategoryList=list;
        Collections.sort(mCategoryList);
        cate_adapter.updateData(mCategoryList);
    }

    @Override
    public void onItemClick(int pos) {
        if (isDeleteModel){
            //如果是删除模式，单击子项变色
            if (mNoteList.get(pos).isFlag()){//onBindViewHolder
                mNoteList.get(pos).setFlag(false);
            }else {
                mNoteList.get(pos).setFlag(true);
            }
            mAdapter.notifyDataSetChanged();//notice
        }else {
            //非删除模式，单击进入编辑模式
            SpUtil.getInstance().setString("category",currentCate);
            Intent intent=new Intent(this,NoteEditActivity.class);
            intent.putExtra("currentCate",currentCate);
            intent.putExtra("id",mNoteList.get(pos).getId());
            intent.putExtra("content",mNoteList.get(pos).getContent());
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(int pos) {
        if (!isDeleteModel){
            //进入删除模式
            mNoteList.get(pos).setFlag(true);
            //pos添加到deleteItem中
            mAdapter.notifyDataSetChanged();
            isDeleteModel=true;
            supportInvalidateOptionsMenu();//通知系统更新菜单
        }
        return false;
    }
    private void deleteNote(){
        for (int i=mNoteList.size()-1;i>-1;i--){//倒序，adapter.notifyItemRemoved会更新pos
            Note note=mNoteList.get(i);
            if (note.isFlag()){
                noteManager.delete(note.getId());
                mNoteList.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
        isDeleteModel=false;
        supportInvalidateOptionsMenu();//通知系统更新菜单
        mNoteList.clear();
        mAdapter.updateData(mNoteList);
        mPresenter.getNote(mContext,currentCate);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (isDeleteModel){
            getMenuInflater().inflate(R.menu.menu_delete_mode,menu);
        }else {
            getMenuInflater().inflate(R.menu.menu_note_list,menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                toggle.onOptionsItemSelected(item);
                return true;
            case R.id.list_delete:
                Snackbar snackbar=Snackbar.make(note_add,"单击笔记可删除笔记",Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
                snackbar.show();
                isDeleteModel=true;
                supportInvalidateOptionsMenu();//通知系统更新菜单
                return true;
            case R.id.delete:
                deleteNote();
                return true;
            case R.id.manager:
                SpUtil.getInstance().setString("currentCate",currentCate);
                Intent intent=new Intent(mContext,CategoryManagerActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        SpUtil.getInstance().setString("currentCate",currentCate);
        super.onDestroy();
    }
    private void hideDrawer(){
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

}
