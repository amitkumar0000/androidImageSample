package com.android.imageslide.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.imageslide.Utils.Const;
import com.android.imageslide.Utils.PreferencesManager;
import com.android.imageslide.Utils.Utils;
import com.android.imageslide.contract.IItemPresenter;
import com.android.imageslide.contract.INetworkInterface;
import com.android.imageslide.contract.IViewInterface;
import com.android.imageslide.model.Item;
import com.android.imageslide.model.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import okhttp3.Call;

import static com.android.imageslide.Utils.Const.TAG;

public class ItemPresenter implements IItemPresenter, INetworkInterface {

    Vector<Item> itemList;
    IViewInterface iViewInterface;
    NetworkManager networkManager;
    Context context;

    public ItemPresenter(Context context, IViewInterface iViewInterface) {
        this.context = context;
        this.iViewInterface = iViewInterface;
        networkManager = new NetworkManager(context, this);
        itemList = new Vector<>();
        register();
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
        networkManager.loadImage(id, path, position);

    }

    @Override
    public void canceloadImage(int position) {
        Call call = Utils.enqueuMap.get(position);
        if(call!=null && !call.isCanceled()) {
            call.cancel();
            Utils.enqueuMap.remove(position);
            Log.d(Const.TAG, "onViewRecycled Cancelling Request at position:: " + position);
        }
    }

    public void loadItem() {
        JSONArray jsonArray = PreferencesManager.getItemArray(context);
        if (jsonArray != null && jsonArray.length() > 0) {
            setItemListfromPref(jsonArray);
            loadItem(jsonArray);
        }
        networkManager.loadItem();
    }

    public void fetchItem() {
        networkManager.loadItem();
    }

    private void setItemListfromPref(JSONArray jsonArray) {
        for (int indx = 0; indx < jsonArray.length(); indx++) {
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
        PreferencesManager.storeItemArray(context, jsonArray);
    }

    public void loadItem(JSONArray jsonArray) {
        boolean moreItemAdded = false;
        int position = itemList.size();
        int count = 0;
        if (jsonArray != null) {
            for (int indx = 0; indx < jsonArray.length(); indx++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(indx);
                    Item item = new Item.Builder()
                            .setId(jsonObject.getString("_id"))
                            .setName(jsonObject.getString("name"))
                            .setDesc(jsonObject.getString("desc"))
                            .setThumbnail(jsonObject.getString("link"))
                            .build();

                    itemList.add(item);

//                    if (!itemList.contains(item)) {
//                        Log.d(TAG, "Delta Item:: " + item.getName());
//                        moreItemAdded = true;
//                        count++;
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (moreItemAdded) {
                iViewInterface.notifyItemRangeInserted(position, count);
            } else {
                iViewInterface.notifyDataSetChanged();
            }

        }
    }

    private void register() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.push");
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                iViewInterface.onNewContent(intent.getStringExtra("Message"));
            }
        }, intentFilter);
    }

    @Override
    public void onFailure(int error) {

    }

    @Override
    public void onImageDownloadResponse(Bitmap bitmap, int position) {

        Log.d(Const.TAG, "Image Load at position:: " + position + " size::" + itemList.size() +
                " Image Size::" + BitmapCompat.getAllocationByteCount(bitmap) / 1024 + "KB");
            Item item = itemList.get(position);
            item.setBitmap(bitmap);
            iViewInterface.notifyItemChanged(position);
    }

    @Override
    public void onContentNotModified() {
        iViewInterface.onContentNotModified();
    }
}

