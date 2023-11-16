package com.realappraiser.gharvalue.convenyancereport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.adapter.CustomViewPager;
import com.realappraiser.gharvalue.convenyancereport.adapter.ConvenyanceReportTabAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConvenyanceReport extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @SuppressLint("StaticFieldLeak")
    CustomViewPager pager;

    public static ConvenyanceReportTabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convenyance_report);
        ButterKnife.bind(this);
        initToolBar();

        adapter = new ConvenyanceReportTabAdapter(this.getSupportFragmentManager());

        pager = (CustomViewPager)findViewById(R.id.pager);
        //Adding adapter to pager
        pager.setAdapter(adapter);
        pager.setPagingEnabled(true);
        pager.setOffscreenPageLimit(0);

        tabLayout.setupWithViewPager(pager);
        //Adding onTabSelectedListener to swipe views
        //noinspection deprecation
        tabLayout.setOnTabSelectedListener(this);
    }


    private  void initToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Conveyance Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}