package com.android.imageslide.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.imageslide.R;

public class ItemHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView thumbnail;
    TextView desc;
    Context context;

    public ItemHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        imageView = itemView.findViewById(R.id.imageView);
        thumbnail = itemView.findViewById(R.id.name);
        desc = itemView.findViewById(R.id.desc);
    }
}
