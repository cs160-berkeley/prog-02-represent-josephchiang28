package com.cs160.joseph.prog_02_represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class CongressionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);

        Intent intent = getIntent();
        String[] repsNames = intent.getStringArrayExtra("REPS_NAMES");
        String[] repsParties = intent.getStringArrayExtra("REPS_PARTIES");
        String[] repsEmails = intent.getStringArrayExtra("REPS_EMAILS");
        String[] repsWebsites = intent.getStringArrayExtra("REPS_WEBSITES");
        String[] repsTweets = intent.getStringArrayExtra("REPS_TWEETS");

        CongressionalListAdapter congressionalListAdapter = new CongressionalListAdapter(this, repsNames, repsParties, repsEmails, repsWebsites, repsTweets);
        ListView congressionalList = (ListView) findViewById(R.id.congressional_list_view);
        congressionalList.setAdapter(congressionalListAdapter);

    }
}
