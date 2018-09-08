package com.android.imageslide.contract;

import com.google.gson.JsonObject;


public interface IViewInterface {
    void notifyDataSetChanged();

    void notifyItemRangeInserted(int position, int count);

    void notifyItemChanged(int position);

    void onContentNotModified();

    void onNewContent(String jsonObject);
}
