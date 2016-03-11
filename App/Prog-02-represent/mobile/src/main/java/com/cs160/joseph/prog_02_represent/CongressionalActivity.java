package com.cs160.joseph.prog_02_represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class CongressionalActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        mRequestQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        final String[] repsNames = intent.getStringArrayExtra("REPS_NAMES");
        final String[] repsParties = intent.getStringArrayExtra("REPS_PARTIES");
        final String[] repsEmails = intent.getStringArrayExtra("REPS_EMAILS");
        final String[] repsWebsites = intent.getStringArrayExtra("REPS_WEBSITES");
        final String[] repsTweets = intent.getStringArrayExtra("REPS_TWEETS");
        final String[] repsBioguideIds = intent.getStringArrayExtra("REPS_BIOGUIDE_IDS");

        CongressionalListAdapter congressionalListAdapter = new CongressionalListAdapter(this, repsNames, repsParties, repsEmails, repsWebsites, repsTweets);
        ListView congressionalList = (ListView) findViewById(R.id.congressional_list_view);
        congressionalList.setAdapter(congressionalListAdapter);

        congressionalList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent detailedIntent = new Intent(CongressionalActivity.this, DetailedActivity.class);
                detailedIntent.putExtra("NAME", repsNames[position]);
                detailedIntent.putExtra("PARTY", repsParties[position]);
                detailedIntent.putExtra("EMAIL", repsEmails[position]);
                detailedIntent.putExtra("WEBSITE", repsWebsites[position]);
                detailedIntent.putExtra("TWEET", repsTweets[position]);
                detailedIntent.putExtra("BIOGUIDE_ID", repsBioguideIds[position]);

                startActivity(detailedIntent);
            }
        });
    }
}
