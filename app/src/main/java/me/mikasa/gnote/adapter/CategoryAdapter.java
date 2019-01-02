package me.mikasa.gnote.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import me.mikasa.gnote.R;
import me.mikasa.gnote.bean.Category;

/**
 * Created by mikasacos on 2018/9/26.
 */

public class CategoryAdapter extends CommonAdapter<Category> {
    //private List<Category>categoryList=new ArrayList<>();
    public CategoryAdapter(final Context context, final int layoutId, List<Category> datas){
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Category category, int position) {
        if (position==0)return;//header
        if (position==1){//有header
            holder.setImageResource(R.id.iv_cate,R.mipmap.createtask_fill);
            holder.setText(R.id.tv_cate,"所有");
        }else {
            holder.setText(R.id.tv_cate,category.getCategory());
        }
    }
}
