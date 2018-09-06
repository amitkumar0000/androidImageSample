package com.android.imageslide.model;

import android.graphics.Bitmap;

public class Item {
    String id;
    String name;
    String desc;
    String thumbnail;
    Bitmap bitmap;

    public Item(Builder builder) {
        id = builder.id;
        name = builder.name;
        desc = builder.desc;
        thumbnail = builder.thumbnail;
        bitmap = builder.bitmap;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public boolean equals(Object obj) {
        Item item = (Item) obj;
        return thumbnail.equals(item.thumbnail)?true:false;
    }

    public static class Builder{
        String id;
        String name;
        String desc;
        String thumbnail;
        Bitmap bitmap;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDesc(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        public Item build(){
            return new Item(this);
        }
    }
}
