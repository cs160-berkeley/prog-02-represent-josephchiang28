package com.cs160.joseph.prog_02_represent;

import android.content.Context;
import android.support.wearable.view.GridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by JOSEPH on 3/3/16.
 */
public class RepGridPagerAdapter extends GridPagerAdapter {
    final Context mContext;
    private final String[] repsNames;
    private final String[] repsTitles;


    public RepGridPagerAdapter(final Context context, String[] repsNames, String[] repsTitles) {
        mContext = context;
        this.repsNames = repsNames;
        this.repsTitles = repsTitles;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int arg0) {
        return repsNames.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int row, int col) {
        final View repView = LayoutInflater.from(mContext).inflate(R.layout.rep_card_frame, container, false);
        TextView repNameView = (TextView) repView.findViewById(R.id.name);
        TextView repTitleView = (TextView) repView.findViewById(R.id.title);

        repNameView.setText(repsNames[col]);
        repTitleView.setText(repsTitles[col]);
        container.addView(repView);
        return repView;
    }

    @Override
    public void destroyItem(ViewGroup container, int row, int col, Object view) {
        container.removeView((View)view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
