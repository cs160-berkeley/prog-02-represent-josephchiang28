package com.cs160.joseph.prog_02_represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by JOSEPH on 3/3/16.
 */
public class WatchListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        String data = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        Intent intent = new Intent(this, MainActivity.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Need to add this flag since you're starting a new activity from a service
        intent.putExtra("DATA", data);
        Log.d("T", "about to start watch MainActivity with DATA: " + data);
        startActivity(intent);
    }
}
