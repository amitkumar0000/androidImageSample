package com.android.imageslide.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PreferencesManager {

    public static void storeItemArray(Context context, JSONArray data){
        SharedPreferences.Editor editor = context
                .getSharedPreferences(
                        Const.PREF_NAME,
                        Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();

        editor.putInt(Const.ITEMLIST,data.length());
        for(int i=0; i<data.length(); i++){
            try {
                JSONObject jsonObject = data.getJSONObject(i);
                editor.putString(Const.ITEMLIST+i,gson.toJson(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        editor.commit();
    }

    public static JSONArray getItemArray(Context context){
        JSONArray data;
        SharedPreferences prefs = context
                .getSharedPreferences(
                        Const.PREF_NAME,
                        Context.MODE_PRIVATE);

        Gson gson = new Gson();

        int sz = prefs.getInt(Const.ITEMLIST,0);
        data = new JSONArray();
        for(int i=0 ; i<sz; i++){
            JSONObject jsonObject = gson.fromJson(prefs.getString(Const.ITEMLIST+i,""),JSONObject.class);
            data.put(jsonObject);
        }

        return data;
    }

}
