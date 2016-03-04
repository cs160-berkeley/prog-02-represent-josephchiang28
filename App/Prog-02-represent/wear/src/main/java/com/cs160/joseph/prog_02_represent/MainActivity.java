package com.cs160.joseph.prog_02_represent;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated(WatchViewStub stub) {
//                mTextView = (TextView) stub.findViewById(R.id.text);
//            }
//        });


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String data = intent.getStringExtra("DATA");
            String[] categories = data.split(";");
            String[] repsNames = categories[0].split(",");
            String[] repsTitles = categories[1].split(",");
            Log.d("DATA", data);
            Log.d("repsNames", categories[0]);
            Log.d("repsNames", categories[0]);
            final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
            RepGridPagerAdapter rpager = new RepGridPagerAdapter(this, repsNames, repsTitles);
            pager.setAdapter(rpager);
        }

    }
}
