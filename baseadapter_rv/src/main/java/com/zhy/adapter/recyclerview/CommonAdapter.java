package com.zhy.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T>
{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    //更新数据,refreshData
    public void updateData(List<T>dataList){
        mDatas.clear();//??
        appendData(dataList);
    }
    //分页加载，追加数据
    public void appendData(List<T>dataList){
        if (null!=dataList&&!dataList.isEmpty()){
            //addAll()追加
            mDatas.addAll(dataList);
            notifyDataSetChanged();
        }else if (dataList!=null&&dataList.isEmpty()){
            notifyDataSetChanged();//空更新？？
        }
    }
    public CommonAdapter(final Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);


}
