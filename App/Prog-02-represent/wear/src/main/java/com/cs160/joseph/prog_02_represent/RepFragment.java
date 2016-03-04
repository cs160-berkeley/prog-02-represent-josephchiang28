package com.cs160.joseph.prog_02_represent;

import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by JOSEPH on 3/3/16.
 */
public class RepFragment extends CardFragment {
    @Override
    public View onCreateContentView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String name = this.getArguments().getString("NAME");
        String title = this.getArguments().getString("TITLE");
        Log.d("NAME", name);
        Log.d("TITLE", title);
        View repView = inflater.inflate(R.layout.rep_card_frame, null, true);
        TextView repNameView = (TextView) repView.findViewById(R.id.name);
        TextView repTitleView = (TextView) repView.findViewById(R.id.title);

        repNameView.setText(name);
        repTitleView.setText(title);

        Log.d("REP NAME UPDATED TO", repNameView.getText().toString());
        Log.d("REP NAME UPDATED TO", repTitleView.getText().toString());

        return repView;
    }
}
