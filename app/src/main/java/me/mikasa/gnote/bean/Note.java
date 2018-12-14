package me.mikasa.gnote.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by mikasacos on 2018/9/18.
 */

public class Note implements Serializable {
    private String id;//这里数据类型应该改为int
    private String content;
    private String firstTime;//note生成时间
    private String lastTime;//note最后编辑时间
    private String category;
    private boolean isFlag=false;//当isFlag=true,note的背景drawable变色
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getContent(){
        return content;
    }
    public void setContent(String s){
        this.content=s;
    }
    public void setFirstTime(String s){
        this.firstTime=s;
    }
    public String getFirstTime(){
        return firstTime;
    }
    public void setLastTime(String s){
        this.lastTime=s;
    }
    public String getLastTime(){
        return lastTime;
    }
    public void setFlag(boolean isFlag){
        this.isFlag=isFlag;
    }
    public boolean isFlag(){
        return isFlag;
    }
    public void setCategory(String s){
        this.category=s;
    }
    public String getCategory(){
        return category;
    }
}
