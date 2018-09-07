package com.android.imageslide.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.android.imageslide.Utils.Const;
import com.android.imageslide.Utils.Utils;
import com.android.imageslide.contract.INetworkInterface;
import com.android.imageslide.views.ImageApplication;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
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
        String path = map.get(Const.PATH);
        int position = Integer.parseInt(map.get(Const.POS));
        String id =map.get(Const.ID);
        Thread.currentThread().setName("THD_IMG");
        if ((bitmap = memCache.getBitmapFromMemCache(path)) != null) {//1. Check in Memory Cache
            Log.d(Const.TAG, "Reading from MemCache");
            networkInterface.onImageDownloadResponse(bitmap, position);
        } else if ((bitmap = diskLruImageCache.getBitmap(id)) != null) { //2. Check in Disk Cache
            Log.d(Const.TAG, "Reading from DiskCache");
            memCache.addBitmapToMemoryCache(path,bitmap);
            networkInterface.onImageDownloadResponse(bitmap, position);

        } else {//3. Call Network
            Log.d(Const.TAG, "Reading from Network");

//            networkCallSync(id,path, position);
            networkCallAsync(id,path,position);
        }

        return null;
    }

    private void networkCallAsync(final String id,final String path,final int position) {
        Request request = new Request.Builder().url(path).build();

        Call call = client.newCall(request);

        Utils.enqueuMap.put(position,call);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] image = response.body().bytes();
                BitmapFactory.Options options = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);
                if(bitmap!=null) {
                    memCache.addBitmapToMemoryCache(path, bitmap);
                    diskLruImageCache.put(id, bitmap);
                    networkInterface.onImageDownloadResponse(bitmap, position);
                }
            }
        });
    }

    private void networkCallSync(String id,String path, int position) {
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