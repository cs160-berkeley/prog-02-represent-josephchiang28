package com.cs160.joseph.prog_02_represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CongressionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);

        Intent intent = getIntent();
        final String[] repsNames = intent.getStringArrayExtra("REPS_NAMES");
        final String[] repsParties = intent.getStringArrayExtra("REPS_PARTIES");
        final String[] repsEmails = intent.getStringArrayExtra("REPS_EMAILS");
        final String[] repsWebsites = intent.getStringArrayExtra("REPS_WEBSITES");
        final String[] repsTweets = intent.getStringArrayExtra("REPS_TWEETS");

        CongressionalListAdapter congressionalListAdapter = new CongressionalListAdapter(this, repsNames, repsParties, repsEmails, repsWebsites, repsTweets);
        ListView congressionalList = (ListView) findViewById(R.id.congressional_list_view);
        congressionalList.setAdapter(congressionalListAdapter);

        congressionalList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent detailedIntent = new Intent(CongressionalActivity.this, DetailedActivity.class);
                detailedIntent.putExtra("NAME", repsNames[position]);
                detailedIntent.putExtra("PARTY", repsParties[position]);
                detailedIntent.putExtra("EMAIL", repsEmails[position]);
                detailedIntent.putExtra("WEBSITE", repsWebsites[position]);
                detailedIntent.putExtra("TWEET", repsTweets[position]);

                startActivity(detailedIntent);
            }
        });
    }
}
