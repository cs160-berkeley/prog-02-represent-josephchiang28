package com.cs160.joseph.prog_02_represent;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    private Geocoder geoCoder;
    private GoogleApiClient mGoogleApiClient;
    private double mLatitude;
    private double mLongitude;
    private RequestQueue mRequestQueue;
    private static final String mGoogleGeocodingAPIKey = "AIzaSyBf7pvbE1iCeZqZK5QGxGAM8_czZxrG8hk";
    private static final String mSunlightFoundationAPIKey = "f7d96524dc8f4b9aa7ef8885500db58f";
    private static final String mSunlightFoundationLegislatorsLocateURL = "http://congress.api.sunlightfoundation.com/legislators/locate";
    private static final HashMap<String, String> partyMap = new HashMap<String, String>() {{
        put("D", "Democrat");
        put("R", "Republican");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geoCoder = new Geocoder(getApplicationContext());
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mRequestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public HashMap<String, String[]> queryWithZip(int zipCode) {
        HashMap<String, String[]> repsInfo = new HashMap<String, String[]>();

        // Fills in dummy info for now
        repsInfo.put("REPS_NAMES", new String[]{"Barbara Lee", "Dianne Feinstein", "Person C"});
        repsInfo.put("REPS_PARTIES", new String[]{"Democrat", "Democrat", "Republican"});
        repsInfo.put("REPS_EMAILS", new String[]{"A@gmail.com", "B@gmail.com", "C@gmail.com"});
        repsInfo.put("REPS_WEBSITES", new String[]{"www.A.com", "www.B.com", "www.C.com"});
        repsInfo.put("REPS_TWEETS", new String[]{"This is tweet A", "This is tweet B", "This is tweet C"});
        repsInfo.put("REPS_TITLES", new String[]{"Representative A", "Representative B", "Representative C"});
        return repsInfo;
    }

    public void lookupWithZip(View view) {
        EditText zip_code_view = (EditText) findViewById(R.id.zip_code_input);
        String zip = zip_code_view.getText().toString();
        String url = String.format("%s?zip=%s&apikey=%s", mSunlightFoundationLegislatorsLocateURL, zip, mSunlightFoundationAPIKey);
        Log.d("GET REQUEST URL: ", url);
        final HashMap<String, String[]> repsInfo = new HashMap<String, String[]>();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        HashMap<String, String[]> repsInfo = parseRepResponse(response);
                        sendCongressionalIntent(repsInfo);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        mRequestQueue.add(jsObjRequest);

    }

    public void lookupWithCurrentLocation(View view) {
        // Dummy for now

//        geoCoder.getFromLocationName();
        lookupWithZip(view);
    }

    public HashMap<String, String[]> parseRepResponse(JSONObject response) {
        HashMap<String, String[]> repsInfo = new HashMap<String, String[]>();
        try {
            JSONArray repJsonArray = response.getJSONArray("results");
            int repCount = repJsonArray.length();
            repsInfo.put("REPS_NAMES", new String[repCount]);
            repsInfo.put("REPS_PARTIES", new String[repCount]);
            repsInfo.put("REPS_EMAILS", new String[repCount]);
            repsInfo.put("REPS_WEBSITES", new String[repCount]);
            repsInfo.put("REPS_TWEETS", new String[repCount]);
            repsInfo.put("REPS_TITLES", new String[repCount]);
            for (int i = 0; i < repJsonArray.length(); i++) {
                JSONObject repJsonObject = repJsonArray.getJSONObject(i);
                repsInfo.get("REPS_NAMES")[i] = repJsonObject.getString("first_name") + " " + repJsonObject.getString("last_name");
                repsInfo.get("REPS_PARTIES")[i] = partyMap.get(repJsonObject.getString("party"));
                repsInfo.get("REPS_EMAILS")[i] = repJsonObject.getString("oc_email");
                repsInfo.get("REPS_WEBSITES")[i] = repJsonObject.getString("website");
                repsInfo.get("REPS_TWEETS")[i] = repJsonObject.getString("twitter_id");
                repsInfo.get("REPS_TITLES")[i] = repJsonObject.getString("title");
            }
        } catch (JSONException e) {

        }
        return repsInfo;
    }

    public void sendCongressionalIntent(HashMap<String, String[]> repsInfo) {
        Intent congressionalIntent = new Intent(this, CongressionalActivity.class);
        for (String key : repsInfo.keySet()) {
            congressionalIntent.putExtra(key, repsInfo.get(key));
        }
        startActivity(congressionalIntent);

        Intent watchIntent = new Intent(this, PhoneToWatchService.class);
        String data = TextUtils.join(",", repsInfo.get("REPS_NAMES"));
        data += ";" + TextUtils.join(",", repsInfo.get("REPS_TITLES"));
        watchIntent.putExtra("DATA", data);
        startService(watchIntent);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
