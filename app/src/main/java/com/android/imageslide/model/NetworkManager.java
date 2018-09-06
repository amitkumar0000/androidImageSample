package com.android.imageslide.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;

import com.android.imageslide.Utils.Constants;
import com.android.imageslide.contract.INetworkInterface;
import com.android.imageslide.contract.IViewInterface;
import com.android.imageslide.presenter.ItemPresenter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkManager {
    OkHttpClient client;
    INetworkInterface networkInterface;
    String itemUrl;


    public NetworkManager(Context context,INetworkInterface networkInterface){
        this.networkInterface=networkInterface;
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.cache(new Cache(context.getCacheDir(), Constants.CACHE_SIZE_BYTES));
        client = builder.build();
//        itemUrl = Constants.url+":"+Constants.port;
        itemUrl = "https://rocky-caverns-52177.herokuapp.com/";
    }

    public void loadItem() {
        new OkhttpAsync(client,networkInterface).execute(itemUrl);
    }

    public void loadImage(String id, String path, int position) {

        HashMap<String,String> map = new HashMap<>();
        map.put(Constants.PATH,path);
        map.put(Constants.POS,String.valueOf(position));
        map.put(Constants.ID,id);
        new OkImageAsync(client,networkInterface).execute(map);
    }

}
