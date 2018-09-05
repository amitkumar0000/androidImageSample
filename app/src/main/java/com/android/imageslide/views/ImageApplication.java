package com.android.imageslide.views;

import android.app.Application;
import android.graphics.Bitmap;

import com.android.imageslide.model.DiskLruImageCache;
import com.android.imageslide.model.MemCache;

public class ImageApplication extends Application{
    static MemCache memCache = new MemCache();
    static DiskLruImageCache diskCache;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private int mCompressQuality = 70;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    @Override
    public void onCreate() {
        super.onCreate();

        diskCache = new DiskLruImageCache(getApplicationContext(),DISK_CACHE_SUBDIR,DISK_CACHE_SIZE,
                mCompressFormat,mCompressQuality);
    }



    public static MemCache getMemCache() {
        return memCache;
    }

    public static DiskLruImageCache getDiskCache() {
        return diskCache;
    }
}
