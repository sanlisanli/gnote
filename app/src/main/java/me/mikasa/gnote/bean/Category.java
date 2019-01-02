package me.mikasa.gnote.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by mikasacos on 2018/9/25.
 */

public class Category implements Serializable,Comparable<Category> {
    private String id;//这里数据类型应该改为int
    private String category;
    private String pos;//依据pos对category重新排序
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setCategory(String s){
        this.category=s;
    }
    public String getCategory(){
        return category;
    }
    public void setPos(String s){
        this.pos=s;
    }
    public String getPos(){
        return pos;
    }

    @Override
    public int compareTo(@NonNull Category o) {
        if (Integer.parseInt(this.pos)==Integer.parseInt(o.getPos())){
            return 0;
        }else if (Integer.parseInt(this.pos)>Integer.parseInt(o.getPos())){
            return 1;
        }else {
            return -1;
        }
    }
}
