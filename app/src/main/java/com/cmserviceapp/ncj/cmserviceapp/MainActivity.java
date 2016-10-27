package com.cmserviceapp.ncj.cmserviceapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by ncj on 2016-10-24.
 */

public class MainActivity extends AppCompatActivity {

    public String TAG = "ncj";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private RelativeLayout mMainLayout;
    private Button mCallButton;
    private Button mMapButton;
    private String mPhoneNum;
    private double mLatitude;
    private double mLongitude;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mMainLayout = (RelativeLayout) findViewById(R.id.content_main);
        mCallButton = (Button) findViewById(R.id.callbutton);
        mMapButton = (Button) findViewById(R.id.mapbutton);
        //setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initView();
            }
        });

        final Intent intent = getIntent();
        mPhoneNum = intent.getStringExtra("phonenum");
        if (mPhoneNum == null) {
            mPhoneNum = "01090688021";
        }
        String latitudeValue = intent.getStringExtra("latitude");
        String longitudeValue = intent.getStringExtra("longitude");

        Log.d("ncj", "mPhoneNum : " + mPhoneNum);
        Log.d("ncj", "latitudeValue : " + latitudeValue);
        Log.d("ncj", "longitudeValue : " + longitudeValue);

        if (latitudeValue != null && longitudeValue != null) {
            mLatitude = Double.parseDouble(latitudeValue);
            mLongitude = Double.parseDouble(longitudeValue);
        } else {
            mLatitude = Double.parseDouble("37.5106943");
            mLongitude = Double.parseDouble("126.76633");
        }

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mPhoneNum));
                Log.d("ncj", "intent - mPhoneNum : " + mPhoneNum);
                startActivity(intent);
            }
        });


        mMapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(mContext, MapsActivity.class);
                mapIntent.putExtra("latitude", mLatitude);
                mapIntent.putExtra("longitude", mLongitude);
                Log.d("ncj", "intent - latitudeValue : " + mLatitude);
                Log.d("ncj", "intent longitudeValue : " + mLongitude);
                startActivity(mapIntent);
            }
        });

        getInstanceIdToken();
    }

    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        mMainLayout.setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}