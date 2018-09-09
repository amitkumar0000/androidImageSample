package com.android.imageslide.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.imageslide.R;
import com.android.imageslide.contract.IItemPresenter;

public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView imageView;
    TextView name;
    TextView desc;
    MainActivity mainActivity;

    public ItemHolder(@NonNull View itemView) {
        super(itemView);
        mainActivity=(MainActivity)itemView.getContext();
        imageView = itemView.findViewById(R.id.imageView);
        name = itemView.findViewById(R.id.name);
        desc = itemView.findViewById(R.id.desc);

        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView:{
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                mainActivity.setBitmapImage(bitmap);
                break;
            }
        }
    }
}
