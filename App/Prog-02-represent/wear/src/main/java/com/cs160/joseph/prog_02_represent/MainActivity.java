package com.cs160.joseph.prog_02_represent;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity {

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String data = intent.getStringExtra("DATA");
            String[] categories = data.split(";");
            String[] repsNames = categories[0].split(",");
            String[] repsParties = categories[1].split(",");
            String[] repsBioguideIds = categories[2].split(",");
            String[] electionPercentages = categories[3].split(",");
            String[] countyState = categories[4].split(",");
            Log.d("DATA", data);
            Log.d("repsNames", categories[0]);
            final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
            RepGridPagerAdapter rpager = new RepGridPagerAdapter(this, repsNames, repsParties, repsBioguideIds, electionPercentages, countyState);
            pager.setAdapter(rpager);
        }
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            float mAccelprev = mAccelLast;
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccelCurrent > 50) {
                Context context = getApplicationContext();
                Random ran = new Random();
                int newZipcode = ran.nextInt(90000) + 10000;
                Intent shakeIntent = new Intent(context, WatchToPhoneService.class);
                shakeIntent.putExtra("DATA", Integer.toString(newZipcode));
                startService(shakeIntent);

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, "Zipcode changed to " + Integer.toString(newZipcode), duration);
                toast.show();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
