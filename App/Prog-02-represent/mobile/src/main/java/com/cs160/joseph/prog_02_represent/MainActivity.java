package com.cs160.joseph.prog_02_represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public HashMap<String, String[]> queryWithZip(int zipCode) {
        HashMap<String, String[]> repsMap = new HashMap<String, String[]>();

        // Fills in dummy info for now
        repsMap.put("REPS_NAMES", new String[]{"Barbara Lee", "Dianne Feinstein"});
        repsMap.put("REPS_PARTIES", new String[]{"Democrat", "Democrat"});
        repsMap.put("REPS_EMAILS", new String[]{"Email", "Email"});
        repsMap.put("REPS_WEBSITES", new String[]{"Website", "Website"});
        repsMap.put("REPS_TWEETS", new String[]{"This is tweet 1", "This is tweet 2"});
        return repsMap;
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
    }

    public void lookupWithCurrentLocation(View view) {

    }
}
