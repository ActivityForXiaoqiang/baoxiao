package com.example.xiaoqiang.baoxiao.common.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.xiaoqiang.baoxiao.R;

public abstract class MyBaseActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    public Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initContentView(getViewId());
        initToolbar();
        init();
    }

    public abstract Integer getViewId();

    private void initContentView(@LayoutRes int layoutID) {
        parentLinearLayout = findViewById(R.id.content);
        View view = LayoutInflater.from(this).inflate(layoutID, null);
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.FILL_PARENT, LinearLayoutCompat.LayoutParams.FILL_PARENT);
        parentLinearLayout.addView(view, params);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public abstract void init();

    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    public void setToolbarTitlte(String titlte) {
        toolbar.setTitle(titlte);
    }
}
