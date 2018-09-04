package com.android.imageslide.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;

import com.android.imageslide.Utils.Constants;
import com.android.imageslide.contract.INetworkInterface;
import com.android.imageslide.views.ImageApplication;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkImageAsync extends AsyncTask<HashMap<String, String>, Void, Void> {

    OkHttpClient client;
    INetworkInterface networkInterface;
    Bitmap bitmap;
    MemCache memCache;
    DiskLruImageCache diskLruImageCache;


    public OkImageAsync(OkHttpClient client, INetworkInterface networkInterface) {
        this.client = client;
        this.networkInterface = networkInterface;
        this.memCache = ImageApplication.getMemCache();
        this.diskLruImageCache = ImageApplication.getDiskCache();

    }

    @Override
    protected Void doInBackground(HashMap<String, String>... maps) {
        HashMap<String, String> map = maps[0];
        String path = map.get(Constants.PATH);
        int position = Integer.parseInt(map.get(Constants.POS));
        String id =map.get(Constants.ID);
        Thread.currentThread().setName("THD_IMG");
        if ((bitmap = memCache.getBitmapFromMemCache(path)) != null) {//1. Check in Memory Cache
            Log.d(Constants.TAG, "Reading from MemCache");
            networkInterface.onImageDownloadResponse(bitmap, position);
        } else if ((bitmap = diskLruImageCache.getBitmap(id)) != null) { //2. Check in Disk Cache
            Log.d(Constants.TAG, "Reading from DiskCache");
            memCache.addBitmapToMemoryCache(path,bitmap);
            networkInterface.onImageDownloadResponse(bitmap, position);

        } else {//3. Call Network
            networkCall(id,path, position);
        }

        return null;
    }

    private void networkCall(String id,String path, int position) {
        Request request = new Request.Builder().url(path).build();
        try {
            Response response = client.newCall(request).execute();
            byte[] image = response.body().bytes();
            BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);
            memCache.addBitmapToMemoryCache(path, bitmap);
            diskLruImageCache.put(id, bitmap);
            networkInterface.onImageDownloadResponse(bitmap, position);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}