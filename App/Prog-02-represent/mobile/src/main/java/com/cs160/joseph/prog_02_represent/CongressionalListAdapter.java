package com.cs160.joseph.prog_02_represent;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CongressionalListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] repsNames;
    private final String[] repsParties;
    private final String[] repsEmails;
    private final String[] repsWebsites;
    private final String[] repsTweets;
    private final String[] repsTwitterIds;

    public CongressionalListAdapter(Activity context, String[] repsNames, String[] repsParties, String[] repsEmails, String[] repsWebsites, String[] repsTweets, String[] repsTwitterIds) {
        super(context, R.layout.rep_list_row, repsNames);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.repsNames = repsNames;
        this.repsParties = repsParties;
        this.repsEmails = repsEmails;
        this.repsWebsites = repsWebsites;
        this.repsTweets = repsTweets;
        this.repsTwitterIds = repsTwitterIds;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.rep_list_row, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.rep_portrait);
        TextView repNameView = (TextView) rowView.findViewById(R.id.rep_name);
        TextView repPartyView = (TextView) rowView.findViewById(R.id.rep_party);
        TextView repEmailView = (TextView) rowView.findViewById(R.id.rep_email);
        TextView repWebsiteView = (TextView) rowView.findViewById(R.id.rep_website);
        TextView repTweetView = (TextView) rowView.findViewById(R.id.rep_tweet);

        Picasso.with(context).load("https://twitter.com/" + repsTwitterIds[position] + "/profile_image?size=original").into(imageView);
        repNameView.setText(repsNames[position]);
        repPartyView.setText(repsParties[position]);
        repEmailView.setText(repsEmails[position]);
        repWebsiteView.setText(repsWebsites[position]);
        repTweetView.setText(repsTweets[position]);
        return rowView;
    };
}
