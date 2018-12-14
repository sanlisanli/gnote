package me.mikasa.gnote.presenter;

import android.content.Context;

import java.util.List;

import me.mikasa.gnote.bean.Category;
import me.mikasa.gnote.bean.Note;
import me.mikasa.gnote.contract.GetNoteContract;
import me.mikasa.gnote.model.GetNoteModelImpl;

/**
 * Created by mikasacos on 2018/9/25.
 */

public class GetNotePresenterImpl implements GetNoteContract.Presenter {
    private GetNoteContract.View mView;
    private GetNoteContract.Model mModel;
    public GetNotePresenterImpl(GetNoteContract.View view){
        this.mView=view;
        mModel=new GetNoteModelImpl(this);
    }

    @Override
    public void getNote(Context context,String category) {
        mModel.getNote(context,category);
    }

    @Override
    public void getNoteSuccess(List<Note> list,List<Category>categoryList) {
        mView.getNoteSuccess(list,categoryList);
    }

    @Override
    public void getError(String msg) {
        mView.getError(msg);
    }

}
