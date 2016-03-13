package com.cs160.joseph.prog_02_represent;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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

    private Intent intent;
    private RequestQueue mRequestQueue;
    private HashMap<String, String[]> repsInfo;
    private HashMap<String, String> electionInfo;
    private int tweetRequired;
    private HashMap<Integer, String> tweetMap;
    private String[] mUsaZipcodeArray;
    private static final String mSunlightFoundationLegislatorsLocateURL = "http://congress.api.sunlightfoundation.com/legislators/locate";
    private static final String mSunlightFoundationAPIKey = "f7d96524dc8f4b9aa7ef8885500db58f";
    private static final String mGoogleMapsGeocodingURL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String mGoogleGeocodingAPIKey = "AIzaSyBf7pvbE1iCeZqZK5QGxGAM8_czZxrG8hk";
    private static final String TWITTER_KEY = "Tt33x4fL3E6WXxywGHQqD1hKk";
    private static final String TWITTER_SECRET = "0Ie09s2S0LqAEDFwiOTRNtKN6f9ksIhsqI2IK2G3no85RXWGT2";
    public static final HashMap<String, String> partyMap = new HashMap<String, String>() {{
        put("D", "Democrat");
        put("R", "Republican");
        put("I", "Independent");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        mRequestQueue = Volley.newRequestQueue(this);
        tweetRequired = -1;
        tweetMap = new HashMap<Integer, String>();
        repsInfo = new HashMap<String, String[]>();

        try {
            InputStream stream = getAssets().open("usa_zip_codes.txt");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String usaZipCodesString = new String(buffer, "UTF-8");
            mUsaZipcodeArray = usaZipCodesString.split("\n");
        } catch (IOException e) {

        }

        intent = getIntent();
        String zipcode = intent.getStringExtra("ZIPCODE");
        String latitude = intent.getStringExtra("LATITUDE");
        String longitude = intent.getStringExtra("LONGITUDE");
        requestRepInfo(zipcode, latitude, longitude);
        requestElectionInfo(zipcode, latitude, longitude);
    }

    public static String getSunlightFoundationAPIKey() {
        return mSunlightFoundationAPIKey;
    }

    public String generateRandomZipcode() {
        Random ran = new Random();
        return mUsaZipcodeArray[ran.nextInt(mUsaZipcodeArray.length)];
    }

    public void requestRepInfo(final String zip, String latitude, String longitude) {
        String url;
        if (zip == null) {
            url = String.format("%s?latitude=%s&longitude=%s&apikey=%s", mSunlightFoundationLegislatorsLocateURL, latitude, longitude, mSunlightFoundationAPIKey);
        } else {
            url = String.format("%s?zip=%s&apikey=%s", mSunlightFoundationLegislatorsLocateURL, zip, mSunlightFoundationAPIKey);
        }
        Log.d("GET REQUEST URL: ", url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("count") == 0) {
                                Log.d("ERROR: ", "DID NOT GET REP RESULT FOR ZIPCODE " + zip);
                                requestRepInfo(generateRandomZipcode(), null, null);
                                return;
                            }
                        } catch (JSONException e) {
                            return;
                        }
                        repsInfo = parseRepResponse(response);
                        Log.d("REP RESPONSE", response.toString());
                        requestTweets();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        mRequestQueue.add(jsObjRequest);
    }

    public void requestElectionInfo(String zip, String latitude, String longitude) {
        String url;
        if (zip == null) {
            url = String.format("%s?latlng=%s,%s&key=%s", mGoogleMapsGeocodingURL, latitude, longitude, mGoogleGeocodingAPIKey);
        } else {
            url = String.format("%s?address=%s&key=%s", mGoogleMapsGeocodingURL, zip, mGoogleGeocodingAPIKey);
        }
        Log.d("GET REQUEST URL: ", url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("COUNTY RESPONSE: ", response.toString());
                        String county = getCountyFromResponse(response);
                        Log.d("GOT COUNTY: ", county);
                        electionInfo = getElectionInfo(county);
                        setViewAndSendWatchIntentIfReady();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        mRequestQueue.add(jsObjRequest);
    }

    public HashMap<String, String[]> parseRepResponse(JSONObject response) {
        try {
            JSONArray repJsonArray = response.getJSONArray("results");
            int repCount = repJsonArray.length();
            repsInfo.put("REPS_NAMES", new String[repCount]);
            repsInfo.put("REPS_PARTIES", new String[repCount]);
            repsInfo.put("REPS_EMAILS", new String[repCount]);
            repsInfo.put("REPS_WEBSITES", new String[repCount]);
            repsInfo.put("REPS_TWITTER_IDS", new String[repCount]);
            repsInfo.put("REPS_TITLES", new String[repCount]);
            repsInfo.put("REPS_BIOGUIDE_IDS", new String[repCount]);
            for (int i = 0; i < repJsonArray.length(); i++) {
                JSONObject repJsonObject = repJsonArray.getJSONObject(i);
                repsInfo.get("REPS_NAMES")[i] = repJsonObject.getString("first_name") + " " + repJsonObject.getString("last_name");
                repsInfo.get("REPS_PARTIES")[i] = partyMap.get(repJsonObject.getString("party"));
                repsInfo.get("REPS_EMAILS")[i] = repJsonObject.getString("oc_email");
                repsInfo.get("REPS_WEBSITES")[i] = repJsonObject.getString("website");
                repsInfo.get("REPS_TWITTER_IDS")[i] = repJsonObject.getString("twitter_id");
                repsInfo.get("REPS_TITLES")[i] = repJsonObject.getString("title");
                repsInfo.get("REPS_BIOGUIDE_IDS")[i] = repJsonObject.getString("bioguide_id");
            }
        } catch (JSONException e) {

        }
        return repsInfo;
    }

    public String getCountyFromResponse(JSONObject response) {
        String county = "";
        try {
            JSONArray addressComponents = response.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
            for (int i = 0; i < addressComponents.length(); i++) {
                JSONArray types = addressComponents.getJSONObject(i).getJSONArray("types");
                for (int j = 0; j < types.length(); j++) {
                    if (types.getString(j).equals("administrative_area_level_2")) {
                        county = addressComponents.getJSONObject(i).getString("short_name").replace("County", "").trim();
                    }
                }
            }
        } catch (JSONException e) {
            Log.d("JSONEXCEPTION: ", e.getMessage());
        }
        Log.d("COUNTY: ", county);
        return county;
    }

    public HashMap<String, String> getElectionInfo(String county) {
        county = county.toLowerCase().trim();
        HashMap<String, String> elecInfo = new HashMap<String, String>();
        String jsonString = "";
        try {
            InputStream stream = getAssets().open("election-county-2012.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {

        }
        try {
            JSONArray electionJson = new JSONArray(jsonString);
            String thisCounty;
            for (int i = 0; i < electionJson.length(); i++) {
                JSONObject countyJson = electionJson.getJSONObject(i);
                thisCounty = countyJson.getString("county-name").toLowerCase().trim();
                if (county.equals(thisCounty)) {
                    elecInfo.put("state-postal", countyJson.getString("state-postal"));
                    elecInfo.put("county-name", countyJson.getString("county-name"));
                    elecInfo.put("obama-vote", Double.toString(countyJson.getDouble("obama-vote")));
                    elecInfo.put("obama-percentage", Double.toString(countyJson.getDouble("obama-percentage")));
                    elecInfo.put("romney-vote", Double.toString(countyJson.getDouble("romney-vote")));
                    elecInfo.put("romney-percentage", Double.toString(countyJson.getDouble("romney-percentage")));
                    return elecInfo;
                }
            }
        } catch (JSONException e) {

        }
        Log.e("ERROR: ", "elecInfo empty. Cannot find county '" + county + "' in json file");
        return elecInfo;
    }

    public void requestTweets() {
        final String[] repsTwitterIds = repsInfo.get("REPS_TWITTER_IDS");

        tweetRequired = repsTwitterIds.length;

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
                            tweetMap.put(index, tweet);
                            setViewAndSendWatchIntentIfReady();
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

    public void setViewAndSendWatchIntentIfReady() {
        if (electionInfo != null && tweetRequired > 0 && tweetMap.size() >= tweetRequired) {
            sendWatchIntent();
            setViewApapter();
        }
    }

    public void setViewApapter() {
        final String[] repsNames = repsInfo.get("REPS_NAMES");
        final String[] repsParties = repsInfo.get("REPS_PARTIES");
        final String[] repsEmails = repsInfo.get("REPS_EMAILS");
        final String[] repsWebsites = repsInfo.get("REPS_WEBSITES");
        final String[] repsBioguideIds = repsInfo.get("REPS_BIOGUIDE_IDS");
        final String[] repsTwitterIds = repsInfo.get("REPS_TWITTER_IDS");
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
                sendDetailedActivityIntent(position, repsTweets, repsTwitterIds, repsBioguideIds);
            }
        });
    }

    public void sendDetailedActivityIntent(int position, String[] repsTweets, String[] repsTwitterIds, String[] repsBioguideIds) {
        Intent detailedIntent = new Intent(CongressionalActivity.this, DetailedActivity.class);
        detailedIntent.putExtra("TWEET", repsTweets[position]);
        detailedIntent.putExtra("TWITTER_ID", repsTwitterIds[position]);
        detailedIntent.putExtra("BIOGUIDE_ID", repsBioguideIds[position]);
        startActivity(detailedIntent);
    }

    public void sendWatchIntent() {
        Intent watchIntent = new Intent(this, PhoneToWatchService.class);
        String data = TextUtils.join(",", repsInfo.get("REPS_NAMES"));
        data += ";" + TextUtils.join(",", repsInfo.get("REPS_PARTIES"));
        data += ";" + TextUtils.join(",", repsInfo.get("REPS_BIOGUIDE_IDS"));
        data += ";" + electionInfo.get("obama-percentage") + "," + electionInfo.get("romney-percentage");
        data += ";" + electionInfo.get("county-name") + "," + electionInfo.get("state-postal");
        data += ";" + intent.getStringExtra("ZIPCODE");
        watchIntent.putExtra("DATA", data);
        startService(watchIntent);
    }
}
