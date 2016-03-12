package com.cs160.joseph.prog_02_represent;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private String mLatitude;
    private String mLongitude;
    public static final HashMap<String, String> partyMap = new HashMap<String, String>() {{
        put("D", "Democrat");
        put("R", "Republican");
        put("I", "Independent");
    }};

    // adb -s 192.168.59.101:5555 -d forward tcp:5601 tcp:5601
    // telnet localhost 5554
    // sensor set acceleration 100
    // Berkeley Coordinates (37.8717, -122.2728)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude = Double.toString(mLastLocation.getLatitude());
            mLongitude = Double.toString(mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void lookupWithZip(View view) {
        EditText zip_code_view = (EditText) findViewById(R.id.zip_code_input);
        String zipcode = zip_code_view.getText().toString();
        sendCongressionalIntent(zipcode, null, null);
    }

    public void lookupWithCurrentLocation(View view) {
        sendCongressionalIntent(null, mLatitude, mLongitude);
    }

    public void sendCongressionalIntent(String zipcode, String latitude, String longitude) {
        Intent congressionalIntent = new Intent(this, CongressionalActivity.class);
        congressionalIntent.putExtra("ZIPCODE", zipcode);
        congressionalIntent.putExtra("LATITUDE", latitude);
        congressionalIntent.putExtra("LONGITUDE", longitude);
        startActivity(congressionalIntent);
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
