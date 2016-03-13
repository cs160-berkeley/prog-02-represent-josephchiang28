package com.cs160.joseph.prog_02_represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.wearable.view.GridPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
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
    public static final HashMap<String, String> partyColorMap = new HashMap<String, String>() {{
        put("Democrat", "#0000ff");
        put("Republican", "#cc0000");
        put("Independent", "#00ff00");
    }};


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
            TextView repPartyView = (TextView) view.findViewById(R.id.party);
            repNameView.setText(repsNames[col]);
            repPartyView.setText(repsParties[col]);
            repPartyView.setTextColor(Color.parseColor(partyColorMap.get(repsParties[col])));
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
            TextView obamaBarView = (TextView) view.findViewById(R.id.obama_bar);
            TextView romneyView = (TextView) view.findViewById(R.id.romney);
            TextView romneyBarView = (TextView) view.findViewById(R.id.romney_bar);
            TextView countyView = (TextView) view.findViewById(R.id.county);
            TextView stateView = (TextView) view.findViewById(R.id.state);

            Random ran = new Random();
            obamaView.setText("Obama: " + electionPercentages[0] + "%");
//            obamaBarView.setWidth((int) Math.round(70 * Double.parseDouble(electionPercentages[0]) / 100));
//            obamaBarView.setWidth(10);
            ViewGroup.LayoutParams obamaBarParams = obamaBarView.getLayoutParams();
            obamaBarParams.width = (int) Math.round(70 * Double.parseDouble(electionPercentages[0]) / 100);
            obamaBarView.setLayoutParams(obamaBarParams);
            romneyView.setText("Romney: " + electionPercentages[1] + "%");
            ViewGroup.LayoutParams romneyBarParams = romneyBarView.getLayoutParams();
            romneyBarParams.width = (int) Math.round(70 * Double.parseDouble(electionPercentages[1]) / 100);
            romneyBarView.setLayoutParams(romneyBarParams);
//            romneyBarView.setWidth((int) Math.round(70 * Double.parseDouble(electionPercentages[1]) / 100));
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
