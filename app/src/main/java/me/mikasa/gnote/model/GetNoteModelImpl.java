package me.mikasa.gnote.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.mikasa.gnote.bean.Category;
import me.mikasa.gnote.bean.Note;
import me.mikasa.gnote.contract.GetNoteContract;
import me.mikasa.gnote.db.NoteManager;
/**
 * Created by mikasacos on 2018/9/25.
 */

public class GetNoteModelImpl implements GetNoteContract.Model {
    private GetNoteContract.Presenter mPresenter;
    private NoteManager noteManager;
    private List<Note>noteList=new ArrayList<>();
    private static List<Category>categoryList=new ArrayList<>();
    public GetNoteModelImpl(GetNoteContract.Presenter presenter){
        this.mPresenter=presenter;
    }

    @Override
    public void getNote(Context context,String category) {
        noteManager=new NoteManager(context);
        if (noteManager.isEmpty()){
            noteManager.initTable();
            //使用sharedPreference持久记录Note的Id,避免id重复而引起数据冲突
        }
        if (category.equals("所有")){
            noteList=noteManager.selectAll();
        }else {
            Log.i("opop","在get"+category);
            if (noteManager.selectCategory(category)!=null){
                noteList=noteManager.selectCategory(category);
            }else {
                mPresenter.getError("");
            }
        }
        categoryList=noteManager.selectAllCategoryBean();
        if (noteList.size()>0&&categoryList.size()>0){
            //对note按照最后编辑时间重新排序
            Collections.sort(noteList, new Comparator<Note>() {
                @Override
                public int compare(Note o1, Note o2) {
                    if (Long.parseLong(o1.getLastTime())==Long.parseLong(o2.getLastTime())){//string型转long型
                        return 0;
                    }else if (Long.parseLong(o1.getLastTime())>Long.parseLong(o2.getLastTime())){
                        return -1;
                    }else {
                        return 1;
                    }
                }
            });
            //根据pos对category进行重新排序
            Collections.sort(categoryList);
            mPresenter.getNoteSuccess(noteList,categoryList);
        }else {
            mPresenter.getError("获取笔记失败");
        }
    }
}
