package com.cs160.joseph.prog_02_represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("NAME");
        final String party = intent.getStringExtra("PARTY");
        final String email = intent.getStringExtra("EMAIL");
        final String website = intent.getStringExtra("WEBSITE");
        final String tweet = intent.getStringExtra("TWEET");

        //        ImageView imageView = (ImageView) rowView.findViewById(R.id.rep_portrait);
        TextView repNameView = (TextView) findViewById(R.id.rep_name);
        TextView repPartyView = (TextView) findViewById(R.id.rep_party);
        TextView repEmailView = (TextView) findViewById(R.id.rep_email);
        TextView repWebsiteView = (TextView) findViewById(R.id.rep_website);
        TextView repTweetView = (TextView) findViewById(R.id.rep_tweet);
        TextView repDetailedInfoView = (TextView) findViewById(R.id.detailed_info);
        TextView repBillsView = (TextView) findViewById(R.id.bills_sponsored);
        TextView repCommitteesView = (TextView) findViewById(R.id.committees);

//        imageView.setImageResource(imgid[position]);
        repNameView.setText(name);
        repPartyView.setText(party);
        repEmailView.setText(email);
        repWebsiteView.setText(website);
        repTweetView.setText(tweet);
        repDetailedInfoView.setText("Full Name: ABC DEF HIJ\nBirthday: 1/1/1987\nGender:Female\nTitle: US Representative");
        repBillsView.setText("Bills Sponsored\nBill A\nBill B");
        repCommitteesView.setText("Committees\nCommittee A\nCommittee B");
    }
}
