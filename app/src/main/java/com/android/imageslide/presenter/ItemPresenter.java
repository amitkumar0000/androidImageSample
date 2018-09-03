package com.android.imageslide.presenter;

import android.graphics.Bitmap;

import com.android.imageslide.contract.IItemPresenter;
import com.android.imageslide.contract.INetworkInterface;
import com.android.imageslide.contract.IViewInterface;
import com.android.imageslide.model.Item;
import com.android.imageslide.model.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemPresenter implements IItemPresenter,INetworkInterface {

    ArrayList<Item> itemList;
    IViewInterface iViewInterface;
    NetworkManager networkManager;

    public ItemPresenter(IViewInterface iViewInterface) {
        this.iViewInterface = iViewInterface;
        networkManager = new NetworkManager();
        itemList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public Item getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public void loadImage(String path, int position) {
        networkManager.loadImage(path,position);

    }

    public void loadItem() {
        networkManager.loadItem(this);
    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
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

                    itemList.add(item);
                    loadImage(item.getThumbnail(),indx);

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
        iViewInterface.notifyItemChanged(position);
    }
}

