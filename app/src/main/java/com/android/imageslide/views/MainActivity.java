package com.android.imageslide.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.imageslide.R;
import com.android.imageslide.Utils.Const;
import com.android.imageslide.model.FireBasedMessagingService;
import com.android.imageslide.Utils.Utils;
import com.android.imageslide.contract.IViewInterface;
import com.android.imageslide.model.Item;
import com.android.imageslide.presenter.ItemPresenter;

import java.io.ByteArrayOutputStream;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements IViewInterface {

    RecyclerView imageList;
    ItemAdapter itemAdapter;
    ItemPresenter itemPresenter;
    GridLayoutManager gridLayoutManager;
    ProgressBar progressBar;
    ProgressBar progressBarMore;
    Drawable dividerDrawable;
    ImageView imageViewPreview;
    FrameLayout imageFrame;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressbar);
        progressBarMore = findViewById(R.id.progressbarMore);
        imageList = findViewById(R.id.imagelist);
        imageViewPreview = findViewById(R.id.itemImagePreview);
        imageFrame = findViewById(R.id.ImageFrame);

        dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider);


        itemPresenter = new ItemPresenter(getApplicationContext(),this);
        initImageList();
        loadItem();

        setScrollImageList();

        imageViewPreview.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageActivity.class);

                Bitmap bmp=((BitmapDrawable)imageViewPreview.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                intent.putExtra(Const.IMAGESRC,byteArray);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this,
                                imageViewPreview,
                                ViewCompat.getTransitionName(imageViewPreview));
                startActivity(intent, options.toBundle());
            }
        });

        new FireBasedMessagingService(this);

    }

    @Override
    public void onBackPressed() {
        if(imageViewPreview.getVisibility() == View.VISIBLE){
            imageViewPreview.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }

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
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                imageViewPreview.setVisibility(View.GONE);
                isImageClick = false;
                int scrollItemCount = gridLayoutManager.findFirstVisibleItemPosition();
                int childItemCount = gridLayoutManager.getChildCount();
                if(scrollItemCount+childItemCount ==
                        gridLayoutManager.getItemCount()){
                    isScrolling = false;
                    progressBarMore.setVisibility(View.VISIBLE);
                    itemPresenter.fetchItem();
                }
            }
        });
    }



    @Override
    public void notifyDataSetChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                progressBarMore.setVisibility(View.GONE);
                imageList.setVisibility(View.VISIBLE);
                itemAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void notifyItemRangeInserted(final int position, final int count) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                itemAdapter.notifyItemRangeInserted(position,count);
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
                Log.d(Const.TAG,"Hide the Progress Bar");
                progressBarMore.setVisibility(View.GONE);
                gridLayoutManager.scrollToPosition(itemPresenter.getItemCount());
            }
        });

    }

    @Override
    public void onNewContent(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
            }
        });
    }

    boolean isImageClick = false;
    public void setBitmapImage(Bitmap bitmapImage){
        if(!isImageClick) {
            imageViewPreview.setVisibility(View.VISIBLE);
            imageViewPreview.setImageBitmap(bitmapImage);
            isImageClick = true;
        }else{
            imageViewPreview.setVisibility(View.GONE);
            isImageClick = false;
        }
    }
}
