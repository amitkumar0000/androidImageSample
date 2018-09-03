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
        new OkhttpAsync().execute(itemUrl);
    }

    public void loadImage(String path, int position) {
        HashMap<String,String> map = new HashMap<>();
        map.put(Constants.PATH,path);
        map.put(Constants.POS,String.valueOf(position));
        new okImageAsync().execute(map);
    }

    class okImageAsync extends AsyncTask<HashMap<String,String>,Void,Void>{

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


    class OkhttpAsync extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... param) {
            Request request = new Request.Builder().url(param[0]).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    networkInterface.onFailure(404);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JSONArray responseArray = null;
                    try {
                        responseArray = new JSONArray(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    networkInterface.onSuccess(responseArray);
                }
            });
            return null;
        }
    }


}
