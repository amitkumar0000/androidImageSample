package com.android.imageslide.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;

import com.android.imageslide.Utils.Constants;
import com.android.imageslide.Utils.PreferencesManager;
import com.android.imageslide.contract.IItemPresenter;
import com.android.imageslide.contract.INetworkInterface;
import com.android.imageslide.contract.IViewInterface;
import com.android.imageslide.model.Item;
import com.android.imageslide.model.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

import static com.android.imageslide.Utils.Constants.TAG;

public class ItemPresenter implements IItemPresenter,INetworkInterface {

    Vector<Item> itemList;
    IViewInterface iViewInterface;
    NetworkManager networkManager;
    Context context;

    public ItemPresenter(Context context, IViewInterface iViewInterface) {
        this.context = context;
        this.iViewInterface = iViewInterface;
        networkManager = new NetworkManager(context,this);
        itemList = new Vector<>();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public Item getItem(int position) {
        return itemList.get(position);
    }

    public Vector<Item> getItemList() {
        return itemList;
    }

    public void loadImage(String id, String path, int position) {
        networkManager.loadImage(id,path,position);

    }

    public void loadItem() {
        JSONArray jsonArray = PreferencesManager.getItemArray(context);
        if(jsonArray!=null){
            setItemListfromPref(jsonArray);
            loadItem(jsonArray);
        }
        networkManager.loadItem();
    }

    public void fetchItem(){
        networkManager.loadItem();
    }

    private void setItemListfromPref(JSONArray jsonArray) {
        for(int indx=0; indx < jsonArray.length(); indx++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(indx);
                Item item = new Item.Builder()
                        .setId(jsonObject.getString("_id"))
                        .setName(jsonObject.getString("name"))
                        .setDesc(jsonObject.getString("desc"))
                        .setThumbnail(jsonObject.getString("link"))
                        .build();
                itemList.add(item);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
       loadItem(jsonArray);
        PreferencesManager.storeItemArray(context,jsonArray);
    }

    public void loadItem(JSONArray jsonArray){
        if(jsonArray!=null){
            for(int indx=0; indx < jsonArray.length(); indx++){
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(indx);
                    Item item = new Item.Builder()
                            .setId(jsonObject.getString("_id"))
                            .setName(jsonObject.getString("name"))
                            .setDesc(jsonObject.getString("desc"))
                            .setThumbnail(jsonObject.getString("link"))
                            .build();
                    if(!itemList.contains(item)) {
                        Log.d(TAG,"Delta Item:: "+ item.getName());
                        itemList.add(item);
                    }
                    loadImage(item.getId(),item.getThumbnail(),indx);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            iViewInterface.notifyDataSetChanged();

        }
    }

    @Override
    public void onFailure(int error) {

    }

    @Override
    public void onImageDownloadResponse(Bitmap bitmap,int position) {
        Item item = itemList.get(position);
        item.setBitmap(bitmap);
//        Log.d(Constants.TAG,"Image Load at position:: "+ position +
//                " Image Size::"+ BitmapCompat.getAllocationByteCount(bitmap)/1024+"KB");
        iViewInterface.notifyItemChanged(position);
    }

    @Override
    public void onContentNotModified() {
        iViewInterface.onContentNotModified();
    }
}

