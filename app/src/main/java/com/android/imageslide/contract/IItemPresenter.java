package com.android.imageslide.contract;

import android.graphics.Bitmap;

import com.android.imageslide.model.Item;

public interface IItemPresenter {
    int getItemCount();

    Item getItem(int position);

    void loadImage(String id,String url,int position);
    void canceloadImage(int position);
}
