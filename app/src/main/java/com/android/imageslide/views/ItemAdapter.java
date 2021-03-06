package com.android.imageslide.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.android.imageslide.R;
import com.android.imageslide.Utils.Const;
import com.android.imageslide.Utils.Utils;
import com.android.imageslide.contract.IItemPresenter;
import com.android.imageslide.model.Item;

import okhttp3.Call;

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

    IItemPresenter iItemPresenter;


    public ItemAdapter(IItemPresenter iItemPresenter){
        this.iItemPresenter = iItemPresenter;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        final View view = layoutInflater.inflate(R.layout.item,viewGroup,false);
        final ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {
        Item item = iItemPresenter.getItem(position);
        itemHolder.name.setText(item.getName());
        itemHolder.desc.setText(item.getDesc());
        if(item.getBitmap()!=null) {
            itemHolder.imageView.setImageBitmap(item.getBitmap());
        }else{
            itemHolder.imageView.setImageResource(R.mipmap.ic_launcher);
            iItemPresenter.loadImage(item.getId(),item.getThumbnail(),position);
        }
//        Log.d(Const.TAG," Item name::" + item.getName()
//                +" Item Position::"+ position);
    }


    @Override
    public void onViewRecycled(@NonNull ItemHolder holder) {
        super.onViewRecycled(holder);
        iItemPresenter.canceloadImage(holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return iItemPresenter.getItemCount();
    }
}
