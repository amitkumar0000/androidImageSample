package com.android.imageslide.contract;

import com.android.imageslide.model.Item;

public interface IItemPresenter {
    int getItemCount();

    Item getItem(int position);

    void loadImage(String thumbnail, int position);
}
