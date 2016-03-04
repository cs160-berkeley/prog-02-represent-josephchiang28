package com.cs160.joseph.prog_02_represent;

import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by JOSEPH on 3/3/16.
 */
public class VoteFragment extends CardFragment {
    @Override
    public View onCreateContentView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String name = this.getArguments().getString("NAME");
        String title = this.getArguments().getString("TITLE");

        View repView = inflater.inflate(R.layout.vote_card_frame, null, true);
//        TextView repNameView = (TextView) repView.findViewById(R.id.name);
//        TextView repTitleView = (TextView) repView.findViewById(R.id.title);
//
//        repNameView.setText(name);
//        repTitleView.setText(title);

        return repView;
    }
}
