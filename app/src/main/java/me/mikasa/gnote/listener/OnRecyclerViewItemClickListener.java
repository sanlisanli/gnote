package me.mikasa.gnote.listener;

/**
 * Created by mikasacos on 2018/9/7.
 */

public interface OnRecyclerViewItemClickListener {
    void onItemClick(int pos);
    boolean onItemLongClick(int pos);
}
