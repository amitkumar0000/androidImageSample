package com.android.imageslide.views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.imageslide.R;
import com.android.imageslide.Utils.Constants;
import com.android.imageslide.Utils.Utils;
import com.android.imageslide.contract.IViewInterface;
import com.android.imageslide.model.Item;
import com.android.imageslide.presenter.ItemPresenter;

import java.util.Vector;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements IViewInterface {

    RecyclerView imageList;
    ItemAdapter itemAdapter;
    ItemPresenter itemPresenter;
    GridLayoutManager gridLayoutManager;
    ProgressBar progressBar;
    ProgressBar progressBarMore;
    Drawable dividerDrawable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);

        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressbar);
        progressBarMore = findViewById(R.id.progressbarMore);
        imageList = findViewById(R.id.imagelist);
//        imageList.setNestedScrollingEnabled(false);

        dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider);


        itemPresenter = new ItemPresenter(getApplicationContext(),this);
        initImageList();
        loadItem();

        setScrollImageList();
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

    private void setScrollImageList() {
        imageList.setOnScrollListener(new RecyclerView.OnScrollListener(){
            boolean isScrolling = false;
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


//                for(int ind=0; ind < itemPresenter.getItemCount(); ind++){
//                    cancelUnVisible(ind,gridLayoutManager.findFirstVisibleItemPosition(),
//                            gridLayoutManager.findLastVisibleItemPosition());
//                }


            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int scrollItemCount = gridLayoutManager.findFirstVisibleItemPosition();
                int childItemCount = gridLayoutManager.getChildCount();
                if(scrollItemCount+childItemCount ==
                        gridLayoutManager.getItemCount()){
                    isScrolling = false;
                    progressBarMore.setVisibility(View.VISIBLE);
                    itemPresenter.fetchItem();
                }

                Log.d(Constants.TAG,"first Visible Position "+  gridLayoutManager.findFirstVisibleItemPosition()
                        +" last Visible Position "+ gridLayoutManager.findLastVisibleItemPosition());
            }
        });
    }

    private void cancelUnVisible(int ind, int firstVisiblePosition, int lastVisiblePosition) {
        if(ind < firstVisiblePosition || ind > lastVisiblePosition){
            if(Utils.enqueuMap.containsKey(ind)) {
                Call call = Utils.enqueuMap.get(ind);
                if(!call.isCanceled()) {
                    call.cancel();
                }
                Utils.enqueuMap.remove(ind);
                Log.d(Constants.TAG, "Cancelling Request at position:: " + ind);
            }
        }else {
            Item item = itemPresenter.getItemList().get(ind);
            if(ImageApplication.getMemCache().getBitmapFromMemCache(item.getThumbnail())==null) {
                itemPresenter.loadImage(item.getId(), item.getThumbnail(), ind);
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                progressBarMore.setVisibility(View.GONE);
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

    @Override
    public void onContentNotModified() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.TAG,"Hide the Progress Bar");
                progressBarMore.setVisibility(View.GONE);
                gridLayoutManager.scrollToPosition(itemPresenter.getItemCount());
            }
        });

    }
}
