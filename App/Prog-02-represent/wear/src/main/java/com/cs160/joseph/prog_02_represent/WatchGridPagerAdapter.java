package com.cs160.joseph.prog_02_represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;

import java.util.List;


/**
 * Created by JOSEPH on 3/3/16.
 */

public class WatchGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private List mRows;
    private final String[] repsNames;
    private final String[] repsTitles;

    public WatchGridPagerAdapter(Context ctx, FragmentManager fm, String[] repsNames, String[] repsTitles) {
        super(fm);
        mContext = ctx;
        this.repsNames = repsNames;
        this.repsTitles = repsTitles;
        // Add images
    }

    // Override methods in FragmentGridPagerAdapter
    @Override
    public Fragment getFragment(int row, int col) {
        if (col < repsNames.length) {
            Bundle bundle = new Bundle();
            bundle.putString("NAME", repsNames[col]);
            bundle.putString("TITLE", repsTitles[col]);
            RepFragment repFragment = new RepFragment();
            repFragment.setArguments(bundle);
            return repFragment;
        } else {
//            Bundle bundle = new Bundle();
//            VoteFragment voteFragment = new VoteFragment();
//            voteFragment.setArguments(bundle);
//            return voteFragment;
            Bundle bundle = new Bundle();
            bundle.putString("NAME", repsNames[0]);
            bundle.putString("TITLE", repsTitles[0]);
            RepFragment repFragment = new RepFragment();
            repFragment.setArguments(bundle);
            return repFragment;
        }
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return 1;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        Log.d("COLUMN COUNT", Integer.toString(repsNames.length));
        return repsNames.length;
    }
}
