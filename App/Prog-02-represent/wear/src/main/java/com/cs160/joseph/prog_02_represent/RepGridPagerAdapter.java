package com.cs160.joseph.prog_02_represent;

import android.content.Context;
import android.content.Intent;
import android.support.wearable.view.GridPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

/**
 * Created by JOSEPH on 3/3/16.
 */
public class RepGridPagerAdapter extends GridPagerAdapter {
    final Context mContext;
    private final String[] repsNames;
    private final String[] repsParties;
    private final String[] repsBioguideIds;
    private final String[] electionPercentages;
    private final String[] countyState;


    public RepGridPagerAdapter(final Context context, String[] repsNames, String[] repsParties, String[] repsBioguideIds, String[] electionPercentages, String[] countyState) {
        mContext = context;
        this.repsNames = repsNames;
        this.repsParties = repsParties;
        this.repsBioguideIds = repsBioguideIds;
        this.electionPercentages = electionPercentages;
        this.countyState = countyState;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int arg0) {
        return repsNames.length + 1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int row, final int col) {
        final View view;
        if (col < repsNames.length) {
            view = LayoutInflater.from(mContext).inflate(R.layout.rep_card_frame, container, false);
            TextView repNameView = (TextView) view.findViewById(R.id.name);
            TextView repTitleView = (TextView) view.findViewById(R.id.title);
            repNameView.setText(repsNames[col]);
            repTitleView.setText(repsParties[col]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CLICKED: ", repsNames[col]);
                    Intent phoneIntent = new Intent(mContext, WatchToPhoneService.class);
                    String data = repsBioguideIds[col];
                    phoneIntent.putExtra("DATA", data);
                    mContext.startService(phoneIntent);
                }
            });
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.vote_card_frame, container, false);
            TextView obamaView = (TextView) view.findViewById(R.id.obama);
            TextView romneyView = (TextView) view.findViewById(R.id.romney);
            TextView countyView = (TextView) view.findViewById(R.id.county);
            TextView stateView = (TextView) view.findViewById(R.id.state);

            Random ran = new Random();
//            int obamaVotes = ran.nextInt(20) + 50;
//            String[] californiaCounties = new String[] { "Alameda", "Alpine", "Amador", "Butte","Calaveras", "Colusa", "Fresno", "Glenn"};
            obamaView.setText("Obama: " + electionPercentages[0] + "%");
            romneyView.setText("Romney: " + electionPercentages[1] + "%");
            countyView.setText(countyState[0]);
            stateView.setText(countyState[1]);
        }
        container.addView(view);
        return view;
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
