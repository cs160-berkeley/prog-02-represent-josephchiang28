package com.cs160.joseph.prog_02_represent;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Created by JOSEPH on 3/3/16.
 */
public class PhoneListenerService extends WearableListenerService {
    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String TOAST = "/send_toast";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        String data = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        if (data == "ZIPCODE") {
            Log.d("RECEIVED: ", "NEW ZIPCODE REQUEST");
            Intent congressionalIntent = new Intent(this, CongressionalActivity.class);
            congressionalIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Need to add this flag since you're starting a new activity from a service
            String usaZipCodesString = "";
            try {
                InputStream stream = getAssets().open("usa_zip_codes.txt");
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                usaZipCodesString = new String(buffer, "UTF-8");
            } catch (IOException e) {

            }
            String[] usaZipCodesList = usaZipCodesString.split("\n");
            Random ran = new Random();
            String newZipCode = usaZipCodesList[ran.nextInt(usaZipCodesList.length)];
            congressionalIntent.putExtra("ZIPCODE", newZipCode);

//            Intent watchIntent = new Intent(this, PhoneToWatchService.class);
//            String watchData = TextUtils.join(",", new String[]{"Person D", "Person E", "Person F"});
//            watchData += ";" + TextUtils.join(",", new String[]{"Representative D", "Representative E", "Representative F"});
//            watchIntent.putExtra("DATA", watchData);

            startActivity(congressionalIntent);
//            startService(watchIntent);
        } else {
            Log.d("RECEIVED DATA:", data);
            String[] dataArray = data.split(";");
            Intent intent = new Intent(this, DetailedActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Need to add this flag since you're starting a new activity from a service
            intent.putExtra("BIOGUIDE_ID", data);
            Log.d("T", "about to start phone DetailedActivity with DATA: " + data);
            startActivity(intent);
        }
    }
}
