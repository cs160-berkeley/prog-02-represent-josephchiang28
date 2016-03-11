package com.cs160.joseph.prog_02_represent;

import android.content.Intent;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import io.fabric.sdk.android.Fabric;

public class CongressionalActivity extends AppCompatActivity {

    private static final String TWITTER_KEY = "Tt33x4fL3E6WXxywGHQqD1hKk";
    private static final String TWITTER_SECRET = "0Ie09s2S0LqAEDFwiOTRNtKN6f9ksIhsqI2IK2G3no85RXWGT2";
    private int tweetRequired;
    private HashMap<Integer, String> tweetMap;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);

        intent = getIntent();
        final String[] repsTwitterIds = intent.getStringArrayExtra("REPS_TWITTER_IDS");

        tweetRequired = repsTwitterIds.length;
        tweetMap = new HashMap<Integer, String>();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                AppSession session = appSessionResult.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                for (int i = 0; i < repsTwitterIds.length; i++) {
                    final int index = i;
                    twitterApiClient.getStatusesService().userTimeline(null, repsTwitterIds[i], 1, null, null, false, false, false, true, new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> listResult) {
                            String tweet = listResult.data.get(0).text;
                            Log.d("GOT TWEET: ", tweet);
                            incrementTweetCountAndUpdate(tweet, index);
                        }
                        @Override
                        public void failure(TwitterException e) {
                            e.printStackTrace();
                        }
                    });
                }

            }
            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }

    public void incrementTweetCountAndUpdate(String tweet, int index) {
        tweetMap.put(index, tweet);
        if (tweetMap.size() >= tweetRequired) {
            setViewApapter();
        }
    }

    public void setViewApapter() {
        final String[] repsNames = intent.getStringArrayExtra("REPS_NAMES");
        final String[] repsParties = intent.getStringArrayExtra("REPS_PARTIES");
        final String[] repsEmails = intent.getStringArrayExtra("REPS_EMAILS");
        final String[] repsWebsites = intent.getStringArrayExtra("REPS_WEBSITES");
        final String[] repsBioguideIds = intent.getStringArrayExtra("REPS_BIOGUIDE_IDS");
        final String[] repsTwitterIds = intent.getStringArrayExtra("REPS_TWITTER_IDS");
        final String[] repsTweets = new String[tweetMap.size()];
        for (int i = 0; i < tweetMap.size(); i++) {
            repsTweets[i] = tweetMap.get(i);
        }

        CongressionalListAdapter congressionalListAdapter = new CongressionalListAdapter(this, repsNames, repsParties, repsEmails, repsWebsites, repsTweets, repsTwitterIds);
        ListView congressionalList = (ListView) findViewById(R.id.congressional_list_view);
        congressionalList.setAdapter(congressionalListAdapter);

        congressionalList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailedIntent = new Intent(CongressionalActivity.this, DetailedActivity.class);
                detailedIntent.putExtra("TWEET", repsTweets[position]);
                detailedIntent.putExtra("TWITTER_ID", repsTwitterIds[position]);
                detailedIntent.putExtra("BIOGUIDE_ID", repsBioguideIds[position]);
                startActivity(detailedIntent);
            }
        });
    }
}
