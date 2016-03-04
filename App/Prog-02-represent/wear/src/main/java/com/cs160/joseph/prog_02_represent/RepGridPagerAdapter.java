package com.cs160.joseph.prog_02_represent;

import android.content.Context;
import android.content.Intent;
import android.support.wearable.view.GridPagerAdapter;
import android.text.TextUtils;
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
            repTitleView.setText(repsTitles[col]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent phoneIntent = new Intent(mContext, WatchToPhoneService.class);
                    String data = String.format("%s;%s;%s;%s;%s;%s", repsNames[col], "Democrat", "B@gmail.com", "www.B.com", "This is tweet B", repsTitles[col]);
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
            int obamaVotes = ran.nextInt(20) + 50;
            String[] californiaCounties = new String[] { "Alameda", "Alpine", "Amador", "Butte","Calaveras", "Colusa", "Fresno", "Glenn"};
            obamaView.setText("Obama: " + Integer.toString(obamaVotes) + "%");
            romneyView.setText("Obama: " + Integer.toString(100-obamaVotes) + "%");
            countyView.setText(californiaCounties[ran.nextInt(californiaCounties.length)] + " County");
            stateView.setText("California");
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
