package me.mikasa.gnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.mikasa.gnote.bean.Category;
import me.mikasa.gnote.bean.Note;
import me.mikasa.gnote.utils.SpUtil;

import static me.mikasa.gnote.constants.Constants.NoteProfile;
/**
 * Created by mikasacos on 2018/9/21.
 */

public class NoteManager {
    private Context mContext;
    private NoteDBHelper noteDBHelper;
    private SQLiteDatabase database;
    private final String[] TABLE_COLUMNS=new String[]{"id","content","firsttime","lasttime","category"};
    private final String[] CATEGORY_COLUMNS=new String[]{"id","category","pos"};
    public NoteManager(Context context){
        mContext=context;
        SpUtil.init(context);
        noteDBHelper=NoteDBHelper.getInstance(mContext);
    }
    /**
     * 初始化表格
     */
    public void initTable(){
        SpUtil.getInstance().setString("category","所有");
        try {
            database=noteDBHelper.getWritableDatabase();
            database.beginTransaction();
            String[] contents=NoteProfile;
            for (int i=0;i<contents.length;i++){
                ContentValues values=new ContentValues();
                values.put("id",String.valueOf(i+1));
                values.put("content",contents[i]);
                Date date=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日");
                String firstTime=sdf.format(date);
                values.put("firsttime",firstTime);
                values.put("lasttime",Long.toString(date.getTime()));
                values.put("category","所有");//
                database.insert(noteDBHelper.TABLE_NAME,null,values);
            }
            SpUtil.getInstance().setInt("noteId",contents.length);
            ContentValues values=new ContentValues();
            values.put("id",1);
            values.put("category","所有");//所有--1
            values.put("pos",String.valueOf(1));
            database.insert(noteDBHelper.TABLE_CATEGORY,null,values);
            SpUtil.getInstance().setInt("categoryNum",1);//category已有一个“所有”分类
            database.setTransactionSuccessful();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (database!=null){
                database.endTransaction();
                database.close();
            }
        }
    }

    /**
     * 判断表格是否为空，为空初始化表格
     * @return
     */
    public boolean isEmpty(){
        Cursor cursor=null;
        try {
            database=noteDBHelper.getReadableDatabase();
            cursor=database.query(NoteDBHelper.TABLE_NAME,
                    new String[]{"count(id)"},
                    null,null,null,null,null);
            if (cursor.moveToFirst()){
                if (cursor.getInt(0)>0){
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            if (database!=null){
                database.close();
            }
        }
        return true;
    }

    /**
     * 清空表格数据
     */
    public void clearTable(){
        try {
            database=noteDBHelper.getWritableDatabase();
            database.delete(noteDBHelper.TABLE_NAME,null,null);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database!=null){
                database.close();
            }
        }
    }
    /**
     * 向表中添加一条数据
     */
    public void insert(Note note){
        try {
            database=noteDBHelper.getWritableDatabase();
            database.beginTransaction();
            ContentValues values=new ContentValues();
            values.put("id",Integer.parseInt(note.getId()));//parseInt
            values.put("content",note.getContent());
            values.put("firsttime",note.getFirstTime());
            values.put("lasttime",note.getLastTime());
            values.put("category",note.getCategory());
            database.insertOrThrow(noteDBHelper.TABLE_NAME,null,values);
            database.setTransactionSuccessful();
        }catch (SQLiteConstraintException e){
            Toast.makeText(mContext, "主键重复", Toast.LENGTH_SHORT).show();
        }finally {
            if (database!=null){
                database.endTransaction();
                database.close();
            }
        }
    }

    /**
     * insert category的时候记得把position的int型转string型
     * @param category
     */
    public void insertCategory(Category category){
        try {
            database=noteDBHelper.getWritableDatabase();
            database.beginTransaction();
            ContentValues values=new ContentValues();
            values.put("id",Integer.parseInt(category.getId()));//parseInt
            values.put("category",category.getCategory());
            values.put("pos",category.getPos());
            database.insertOrThrow(noteDBHelper.TABLE_CATEGORY,null,values);
            database.setTransactionSuccessful();
        }catch (SQLiteConstraintException e){
            Toast.makeText(mContext, "主键重复", Toast.LENGTH_SHORT).show();
        }finally {
            if (database!=null){
                database.endTransaction();
                database.close();
            }
        }
    }
    /**
     * 从表中删除一条数据
     */
    public void delete(String id){
        try {
            database=noteDBHelper.getWritableDatabase();
            database.beginTransaction();
            database.delete(noteDBHelper.TABLE_NAME,"id=?",new String[]{String.valueOf(id)});
            database.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database!=null){
                database.endTransaction();
                database.close();
            }
        }
    }
    public void deleteCategory(String id){
        try {
            database=noteDBHelper.getWritableDatabase();
            database.beginTransaction();
            database.delete(noteDBHelper.TABLE_CATEGORY,"id=?",new String[]{String.valueOf(id)});
            database.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database!=null){
                database.endTransaction();
                database.close();
            }
        }
    }

    /**
     * 修改
     * @param note
     */
    public void update(Note note){
        try {
            database=noteDBHelper.getWritableDatabase();
            database.beginTransaction();
            ContentValues values=new ContentValues();
            if (!TextUtils.isEmpty(note.getContent())){
                values.put("content",note.getContent());
            }
            if (!TextUtils.isEmpty(note.getLastTime())){
                values.put("lasttime",note.getLastTime());
            }
            database.update(noteDBHelper.TABLE_NAME,values,"id=?",
                    new String[]{String.valueOf(note.getId())});
            database.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database!=null){
                database.endTransaction();
                database.close();
            }
        }
    }
    public void updateCategory(Category category){
        try {
            database=noteDBHelper.getWritableDatabase();
            database.beginTransaction();
            ContentValues values=new ContentValues();
            values.put("category",category.getCategory());
            values.put("pos",category.getPos());
            //根据id来update
            database.update(noteDBHelper.TABLE_CATEGORY,values,"id=?",
                    new String[]{String.valueOf(category.getId())});
            database.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database!=null){
                database.endTransaction();
                database.close();
            }
        }
    }
    /**
     * 根据条件查询
     */
    public List<Note> select(Note note){
        Cursor cursor=null;
        try {
            database=noteDBHelper.getReadableDatabase();
            if (!TextUtils.isEmpty(note.getId())){
                cursor=database.query(noteDBHelper.TABLE_NAME,TABLE_COLUMNS,
                        "id=?",new String[]{note.getId()},null,null,null);//三个null
                if (cursor.getCount()>0){
                    List<Note>list=new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()){
                        list.add(parseNote(cursor));
                    }
                    return list;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            if (database!=null){
                database.close();
            }
        }
        return null;
    }

    /**
     * 通过 string category查询note
     * @param category
     * @return
     */
    public List<Note> selectCategory(String category){
        Cursor cursor=null;
        try {
            database=noteDBHelper.getReadableDatabase();
            if (!TextUtils.isEmpty(category)){
                cursor=database.query(noteDBHelper.TABLE_NAME,TABLE_COLUMNS,
                        "category=?",new String[]{category},null,null,null);//三个null
                if (cursor.getCount()>0){
                    List<Note>list=new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()){
                        list.add(parseNote(cursor));
                    }
                    return list;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            if (database!=null){
                database.close();
            }
        }
        return null;
    }

    /**
     * 获取category表中所有的category
     * @return
     */
    public List<String> selectAllCategory(){
        Cursor cursor=null;
        try {
            database=noteDBHelper.getReadableDatabase();
            cursor=database.query(noteDBHelper.TABLE_CATEGORY,CATEGORY_COLUMNS,null,null,null,null,null);//五个null
            if (cursor.getCount()>0){
                List<String>list=new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()){
                    list.add(cursor.getString(cursor.getColumnIndex("category")));
                }
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            if (database!=null){
                database.close();
            }
        }
        return null;//return null
    }

    /**
     * 查询表格全部note
     * @return
     */
    public List<Note>selectAll(){
        Cursor cursor=null;
        try {
            database=noteDBHelper.getReadableDatabase();
            cursor=database.query(noteDBHelper.TABLE_NAME,TABLE_COLUMNS,null,null,null,null,null);//五个null
            if (cursor.getCount()>0){
                List<Note>list=new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()){
                    list.add(parseNote(cursor));
                }
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            if (database!=null){
                database.close();
            }
        }
        return null;//return null
    }
    public List<Category>selectAllCategoryBean(){
        Cursor cursor=null;
        try {
            database=noteDBHelper.getReadableDatabase();
            cursor=database.query(noteDBHelper.TABLE_CATEGORY,CATEGORY_COLUMNS,null,null,null,null,null);//五个null
            if (cursor.getCount()>0){
                List<Category>list=new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()){
                    list.add(parseCategory(cursor));
                }
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            if (database!=null){
                database.close();
            }
        }
        return null;//return null
    }
    /**
     * 将cursor查询到的数据转化为javabean,即Note
     * @param cursor
     * @return
     */
    private Note parseNote(Cursor cursor){//cursor
        Note note=new Note();
        note.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
        note.setContent(cursor.getString(cursor.getColumnIndex("content")));
        note.setFirstTime(cursor.getString(cursor.getColumnIndex("firsttime")));
        note.setLastTime(cursor.getString(cursor.getColumnIndex("lasttime")));
        note.setCategory(cursor.getString(cursor.getColumnIndex("category")));
        return note;
    }
    private Category parseCategory(Cursor cursor){
        Category category=new Category();
        category.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
        category.setCategory(cursor.getString(cursor.getColumnIndex("category")));
        category.setPos(cursor.getString(cursor.getColumnIndex("pos")));
        return category;
    }
}
