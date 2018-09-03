package com.android.imageslide.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkManager {
    OkHttpClient client;
    INetworkInterface networkInterface;
    String itemUrl;

    public NetworkManager(){
        client = new OkHttpClient();
        itemUrl = Constants.url+":"+Constants.port;
    }

    public void loadItem(INetworkInterface networkInterface) {
        this.networkInterface=networkInterface;
        new OkhttpAsync(client,networkInterface).execute(itemUrl);
    }

    public void loadImage(String path, int position) {
        HashMap<String,String> map = new HashMap<>();
        map.put(Constants.PATH,path);
        map.put(Constants.POS,String.valueOf(position));
        new OkImageAsync(client,networkInterface).execute(map);
    }

}
