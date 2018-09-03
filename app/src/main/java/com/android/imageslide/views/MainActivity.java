package com.android.imageslide.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.imageslide.R;
import com.android.imageslide.contract.IViewInterface;
import com.android.imageslide.presenter.ItemPresenter;

public class MainActivity extends AppCompatActivity implements IViewInterface {

    RecyclerView imageList;
    ItemAdapter itemAdapter;
    ItemPresenter itemPresenter;
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressbar);
        initImageList();

        loadItem();
    }

    private void loadItem() {
        itemPresenter.loadItem();
    }

    private void initImageList() {
        imageList = findViewById(R.id.imagelist);
        itemPresenter = new ItemPresenter(this);
        itemAdapter = new ItemAdapter(itemPresenter);

        imageList.setLayoutManager(new GridLayoutManager(this,2));
        imageList.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        imageList.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.HORIZONTAL));
        imageList.setAdapter(itemAdapter);
    }

    @Override
    public void notifyDataSetChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                itemAdapter.notifyDataSetChanged();
                imageList.setVisibility(View.VISIBLE);
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
