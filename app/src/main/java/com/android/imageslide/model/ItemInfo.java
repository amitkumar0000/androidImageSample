package com.android.imageslide.model;

import okhttp3.Call;

public class ItemInfo {
    String id;
    String path;
    int position;
    Call call;
    public ItemInfo(String id,String path, int position,Call call){
        this.id = id;
        this.path = path;
        this.position = position;
        this.call = call;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public int getPosition() {
        return position;
    }

    public Call getCall() {
        return call;
    }
}
