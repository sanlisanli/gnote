package me.mikasa.gnote.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import me.mikasa.gnote.R;
import me.mikasa.gnote.base.BaseRecyclerViewAdapter;
import me.mikasa.gnote.bean.Note;

/**
 * Created by mikasacos on 2018/9/19.
 */

public class NoteAdapter extends BaseRecyclerViewAdapter<Note> {
    private Context mContext;
    public NoteAdapter(Context context){
        this.mContext=context;
   }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_note,parent,false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((NoteHolder)holder).bindView(mDataList.get(position));
        if (mDataList.get(position).isFlag()){
            ((NoteHolder)holder).root.setSelected(true);
            ((NoteHolder)holder).trashImg.setVisibility(View.VISIBLE);
        }else {
            ((NoteHolder)holder).root.setSelected(false);
            ((NoteHolder)holder).trashImg.setVisibility(View.GONE);
        }
    }

    class NoteHolder extends BaseRvViewHolder{
        TextView content;
        TextView title;
        TextView noteFirstTime;
        CircleImageView trashImg;
        RelativeLayout root;
        NoteHolder(View itemView){
            super(itemView);
            content=(TextView)itemView.findViewById(R.id.note_content);
            title=(TextView)itemView.findViewById(R.id.note_title);
            noteFirstTime=(TextView)itemView.findViewById(R.id.note_date_first);
            root=(RelativeLayout) itemView.findViewById(R.id.note_root);
            trashImg=(CircleImageView)itemView.findViewById(R.id.icon_trash);
        }

        @Override
        protected void bindView(Note note) {
            title.setText(note.getContent());
            content.setText(note.getContent());
            noteFirstTime.setText(note.getFirstTime());
        }
    }
}
