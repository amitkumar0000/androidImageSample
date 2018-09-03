package com.android.imageslide.views;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.imageslide.R;
import com.android.imageslide.contract.IItemPresenter;
import com.android.imageslide.model.Item;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.android.imageslide.Utils.Constants.TAG;

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

    IItemPresenter iItemPresenter;

    public ItemAdapter(IItemPresenter iItemPresenter){
        this.iItemPresenter = iItemPresenter;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item,viewGroup,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {
        Item item = iItemPresenter.getItem(position);
        itemHolder.thumbnail.setText(item.getName());
        itemHolder.desc.setText(item.getDesc());
        if(item.getBitmap()!=null) {
            itemHolder.imageView.setImageBitmap(item.getBitmap());
        }
        String path = item.getThumbnail();
        Log.d(TAG,"Image Link:: "+path);

    }

    @Override
    public int getItemCount() {
        return iItemPresenter.getItemCount();
    }
}
