package com.android.imageslide.contract;

import android.graphics.Bitmap;

import org.json.JSONArray;

public interface INetworkInterface {
    void onSuccess(JSONArray jsonArray);
    void onFailure(int error);

    void onImageDownloadResponse(Bitmap bitmap, int position);
}
