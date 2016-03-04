package com.cs160.joseph.prog_02_represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        int zipCode = Integer.parseInt(zip_code_view.getText().toString());
        HashMap<String, String[]> repsInfo = queryWithZip(zipCode);
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

    public void lookupWithCurrentLocation(View view) {
        // Dummy for now
        lookupWithZip(view);
    }
}
