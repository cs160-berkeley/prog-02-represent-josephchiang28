package com.cs160.joseph.prog_02_represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private int httpResponseCount;
    private int httpResponseRequired;
    private HashMap<String, JSONObject> httpResponses;
    private static final String mSunlightFoundationLegislatorsURL = "http://congress.api.sunlightfoundation.com/legislators";
    private static final String mSunlightFoundationCommitteesURL = "http://congress.api.sunlightfoundation.com/committees";
    private static final String mSunlightFoundationBillsURL = "http://congress.api.sunlightfoundation.com/bills";
    public static final HashMap<String, String> genderMap = new HashMap<String, String>() {{
        put("M", "Male");
        put("F", "Female");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        mRequestQueue = Volley.newRequestQueue(this);
        httpResponseCount = 0;
        httpResponseRequired = 3;
        httpResponses = new HashMap<String, JSONObject>();

        Intent intent = getIntent();
        final String bioguideId = intent.getStringExtra("BIOGUIDE_ID");

        String legislatorsRequestUrl = String.format("%s?bioguide_id=%s&&apikey=%s", mSunlightFoundationLegislatorsURL, bioguideId, MainActivity.getSunlightFoundationAPIKey());
        JsonObjectRequest legislatorsRequest = new JsonObjectRequest
                (Request.Method.GET, legislatorsRequestUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        httpResponses.put("LEGISLATORS_RESPONSE", response);
                        incrementResponseCountAndUpdate();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        String committeesRequestUrl = String.format("%s?member_ids=%s&&apikey=%s", mSunlightFoundationCommitteesURL, bioguideId, MainActivity.getSunlightFoundationAPIKey());
        JsonObjectRequest committeesRequest = new JsonObjectRequest
                (Request.Method.GET, committeesRequestUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        httpResponses.put("COMMITTEES_RESPONSE", response);
                        incrementResponseCountAndUpdate();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        String billsRequestUrl = String.format("%s?sponsor_id=%s&&apikey=%s", mSunlightFoundationBillsURL, bioguideId, MainActivity.getSunlightFoundationAPIKey());
        JsonObjectRequest billsRequest = new JsonObjectRequest
                (Request.Method.GET, billsRequestUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        httpResponses.put("BILLS_RESPONSE", response);
                        incrementResponseCountAndUpdate();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        mRequestQueue.add(legislatorsRequest);
        mRequestQueue.add(committeesRequest);
        mRequestQueue.add(billsRequest);
    }

    public void incrementResponseCountAndUpdate() {
        httpResponseCount++;
        if (httpResponseCount >= httpResponseRequired) {
            setTextViews();
        }
    }

    public void setTextViews() {
        Log.d("IN", "SET TEXT VIEWS");
        setProfileView(httpResponses.get("LEGISLATORS_RESPONSE"));
        setCommitteesView(httpResponses.get("COMMITTEES_RESPONSE"));
        setBillsView(httpResponses.get("BILLS_RESPONSE"));
    }

    public void setProfileView(JSONObject legislatorsResponse) {
        Log.d("IN", "SET PROFILE VIEW");
        //        ImageView imageView = (ImageView) rowView.findViewById(R.id.rep_portrait);
        TextView repNameView = (TextView) findViewById(R.id.rep_name);
        TextView repPartyView = (TextView) findViewById(R.id.rep_party);
        TextView repEmailView = (TextView) findViewById(R.id.rep_email);
        TextView repWebsiteView = (TextView) findViewById(R.id.rep_website);
        TextView repTweetView = (TextView) findViewById(R.id.rep_tweet);
        TextView repDetailedInfoView = (TextView) findViewById(R.id.detailed_info);

        try {
            if (legislatorsResponse.getInt("count") < 1) {
                return;
            }
            JSONObject repJsonObject = legislatorsResponse.getJSONArray("results").getJSONObject(0);

            // Set basic profile
            repNameView.setText(repJsonObject.getString("first_name") + " " + repJsonObject.getString("last_name"));
            repPartyView.setText(MainActivity.partyMap.get(repJsonObject.getString("party")));
            repEmailView.setText(repJsonObject.getString("oc_email"));
            repWebsiteView.setText(repJsonObject.getString("website"));
            repTweetView.setText(repJsonObject.getString("twitter_id"));

            // Set detailed profile
            StringBuffer profileSb = new StringBuffer();
            profileSb.append("Full Name: ");
            profileSb.append(repJsonObject.getString("first_name") + " " + repJsonObject.getString("last_name"));
            profileSb.append("\n");
            profileSb.append("Birthday: ");
            profileSb.append(repJsonObject.getString("birthday"));
            profileSb.append("\n");
            profileSb.append("gender: ");
            profileSb.append(genderMap.get(repJsonObject.getString("gender")));
            profileSb.append("\n");
            profileSb.append("Title: ");
            profileSb.append(repJsonObject.getString("title"));
            repDetailedInfoView.setText(profileSb.toString());

        } catch (JSONException e) {

        }
    }

    public void setCommitteesView(JSONObject committeesResponse) {
        Log.d("IN", "SET COMMITTEES VIEW");
        TextView repCommitteesView = (TextView) findViewById(R.id.committees);

        try {
            JSONArray committeesJsonArray = committeesResponse.getJSONArray("results");
            String[] committeesNameArray = new String[committeesJsonArray.length()];
            for (int i = 0; i < committeesJsonArray.length(); i++) {
                committeesNameArray[i] = committeesJsonArray.getJSONObject(i).getString("name");
            }
            repCommitteesView.setText("Committees: \n" + TextUtils.join("\n", committeesNameArray));
        } catch (JSONException e) {
            Log.d("JSONEXCEPTION: ", e.getMessage());
        }
    }

    public void setBillsView(JSONObject billsResponse) {
        Log.d("IN", "SET BILLS VIEW");
        TextView repBillsView = (TextView) findViewById(R.id.bills_sponsored);

        try {
            JSONArray billsJsonArray = billsResponse.getJSONArray("results");
            String[] billsNameArray = new String[billsJsonArray.length()];
            for (int i = 0; i < billsJsonArray.length(); i++) {
                if (billsJsonArray.getJSONObject(i).getString("short_title") == null) {
                    billsNameArray[i] = billsJsonArray.getJSONObject(i).getString("short_title");
                } else {
                    billsNameArray[i] = billsJsonArray.getJSONObject(i).getString("official_title");
                }
            }
            repBillsView.setText("Bills: \n" + TextUtils.join("\n", billsNameArray));
        } catch (JSONException e) {
            Log.d("JSONEXCEPTION: ", e.getMessage());
        }

    }
}
