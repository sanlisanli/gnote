package me.mikasa.gnote.adapter;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import me.mikasa.gnote.R;
import me.mikasa.gnote.bean.Category;
import me.mikasa.gnote.listener.OnItemDrag;

/**
 * Created by mikasacos on 2018/9/26.
 */

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ViewHolder>
        implements OnItemDrag{
    private Context mContext;
    private List<Category> list;

    public CategoryItemAdapter(Context context, List<Category> list){
        this.mContext = context;
        this.list = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_category,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position==0){
            holder.iv_cate.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.createtask_fill));
            holder.tv_cate.setText(list.get(0).getCategory());
        }else {
            holder.tv_cate.setText(list.get(position).getCategory());
            holder.move.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onMove(int fromPosition,int toPosition){
        //??
        if (fromPosition==0||toPosition==0){
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
    @Override
    public void onSwiped(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_cate;
        TextView tv_cate;
        ImageView move;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_cate=(TextView)itemView.findViewById(R.id.tv_cate);
            iv_cate=(ImageView)itemView.findViewById(R.id.iv_cate);
            move=(ImageView)itemView.findViewById(R.id.move);
        }
    }
}
