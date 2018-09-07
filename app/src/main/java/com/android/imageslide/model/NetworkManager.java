package com.android.imageslide.model;

import android.content.Context;

import com.android.imageslide.Utils.Const;
import com.android.imageslide.contract.INetworkInterface;

import java.util.HashMap;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class NetworkManager {
    OkHttpClient client;
    INetworkInterface networkInterface;
    String itemUrl;


    public NetworkManager(Context context,INetworkInterface networkInterface){
        this.networkInterface=networkInterface;
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.cache(new Cache(context.getCacheDir(), Const.CACHE_SIZE_BYTES));
        client = builder.build();
//        itemUrl = Const.url+":"+Const.port;
        itemUrl = "https://rocky-caverns-52177.herokuapp.com/";
    }

    public void loadItem() {
        new OkhttpAsync(client,networkInterface).execute(itemUrl);
    }

    public void loadImage(String id, String path, int position) {

        HashMap<String,String> map = new HashMap<>();
        map.put(Const.PATH,path);
        map.put(Const.POS,String.valueOf(position));
        map.put(Const.ID,id);
        new OkImageAsync(client,networkInterface).execute(map);
    }

}
