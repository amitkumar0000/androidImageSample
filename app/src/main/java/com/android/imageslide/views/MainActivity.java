package com.android.imageslide.views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.imageslide.R;
import com.android.imageslide.Utils.Constants;
import com.android.imageslide.contract.IViewInterface;
import com.android.imageslide.presenter.ItemPresenter;

public class MainActivity extends AppCompatActivity implements IViewInterface {

    RecyclerView imageList;
    ItemAdapter itemAdapter;
    ItemPresenter itemPresenter;
    GridLayoutManager gridLayoutManager;
    ProgressBar progressBar;
    Drawable dividerDrawable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressbar);
        imageList = findViewById(R.id.imagelist);

        dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider);


        itemPresenter = new ItemPresenter(getApplicationContext(),this);
        initImageList();
        loadItem();
    }

    private void loadItem() {
        itemPresenter.loadItem();
    }

    private void initImageList() {
        itemAdapter = new ItemAdapter(itemPresenter);
        gridLayoutManager =new GridLayoutManager(this,2);
        imageList.setLayoutManager(gridLayoutManager);
        imageList.addItemDecoration(new SpacesItemDecoration(dividerDrawable,15));
        imageList.setAdapter(itemAdapter);
        if(itemAdapter.getItemCount()>0){
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void notifyDataSetChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                itemAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void notifyItemChanged(final int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                itemAdapter.notifyItemChanged(position);

            }
        });
    }
}
