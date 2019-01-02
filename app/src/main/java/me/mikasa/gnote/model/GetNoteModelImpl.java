package me.mikasa.gnote.model;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
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
    public GetNoteModelImpl(GetNoteContract.Presenter presenter){
        this.mPresenter=presenter;
    }

    @Override
    public void getNote(Context context,String category) {
        noteManager=new NoteManager(context);
        List<Note>noteList=new ArrayList<>();
        if (noteManager.isEmpty()){
            noteManager.initTable();
        }
        if (category.equals("所有")){
            noteList=noteManager.selectAll();
        }else {
            noteList=noteManager.selectCategory(category);//noteList
        }
        if (noteList!=null){
            //对note按照最后编辑时间重新排序
            Collections.sort(noteList);
            mPresenter.getNoteSuccess(noteList);
        }
    }

    @Override
    public void getCategory(Context context) {
        noteManager=new NoteManager(context);
        if (noteManager.isEmpty()){
            noteManager.initTable();
        }
        List<Category> categoryList=noteManager.selectAllCategoryBean();
        Collections.sort(categoryList);
        mPresenter.getCategorySuccess(categoryList);
    }
}
