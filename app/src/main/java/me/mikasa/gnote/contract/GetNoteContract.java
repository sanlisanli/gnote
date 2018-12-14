package me.mikasa.gnote.contract;

import android.content.Context;

import java.util.List;

import me.mikasa.gnote.bean.Category;
import me.mikasa.gnote.bean.Note;

/**
 * Created by mikasacos on 2018/9/25.
 */

public interface GetNoteContract {
    interface View{//activity或fragment
        void getNoteSuccess(List<Note> list,List<Category>categoryList);
        void getError(String msg);
    }
    interface Model{//后台耗时操作
        void getNote(Context context,String category);
    }
    interface Presenter{//桥梁,连接UI和后台耗时操作
        void getNote(Context context,String category);
        void getNoteSuccess(List<Note>list,List<Category>categoryList);
        void getError(String msg);
    }
}
