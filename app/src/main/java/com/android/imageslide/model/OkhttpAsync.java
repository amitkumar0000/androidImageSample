package com.android.imageslide.model;

import android.os.AsyncTask;
import android.util.Log;

import com.android.imageslide.contract.INetworkInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.android.imageslide.Utils.Const.TAG;

public class OkhttpAsync extends AsyncTask<String, Void, Void> {

    OkHttpClient client;
    INetworkInterface networkInterface;
    public OkhttpAsync(OkHttpClient client, INetworkInterface networkInterface){
        this.client = client;
        this.networkInterface = networkInterface;

    }

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
                if (response.isSuccessful() &&
                        response.networkResponse() != null &&
                        response.networkResponse().code() ==
                                HttpURLConnection.HTTP_NOT_MODIFIED) {
                    Log.d(TAG,"Content not Modified");
                    networkInterface.onContentNotModified();
                    return;
                }
//                Log.d(Const.TAG,"Respon:: "+ response.body().string());
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