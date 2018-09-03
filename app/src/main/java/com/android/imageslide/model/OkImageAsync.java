package com.android.imageslide.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.android.imageslide.Utils.Constants;
import com.android.imageslide.contract.INetworkInterface;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkImageAsync extends AsyncTask<HashMap<String,String>,Void,Void> {

    OkHttpClient client;
    INetworkInterface networkInterface;
    public OkImageAsync(OkHttpClient client, INetworkInterface networkInterface){
        this.client = client;
        this.networkInterface = networkInterface;

    }

    @Override
    protected Void doInBackground(HashMap<String,String>... maps) {
        HashMap<String, String> map = maps[0];
        String path = map.get(Constants.PATH);
        int position = Integer.parseInt(map.get(Constants.POS));

        Request request = new Request.Builder().url(path).build();

        try {
            Response response = client.newCall(request).execute();
            byte[] image = response.body().bytes();
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);
            networkInterface.onImageDownloadResponse(bitmap, position);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}