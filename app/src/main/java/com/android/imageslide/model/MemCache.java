package com.android.imageslide.model;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.google.gson.JsonObject;

public class MemCache {
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = maxMemory / 8;
    private LruCache<String, Bitmap> imageMemoryCache;
    private LruCache<String, JsonObject> itemMemoryCache;

    public MemCache(){
        imageMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            imageMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return imageMemoryCache.get(key);
    }


}
