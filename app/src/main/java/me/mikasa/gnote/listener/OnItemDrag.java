package me.mikasa.gnote.listener;

/**
 * Created by mikasacos on 2018/9/26.
 */

public interface OnItemDrag {
    void onMove(int fromPosition,int toPosition);
    void onSwiped(int position);
}
