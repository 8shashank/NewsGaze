package com.shashank_sharma.newsgaze2;

import android.content.Intent;
        import android.location.Location;
        import android.location.LocationManager;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;


import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    double longitude;
    double latitude;
    private static final long LOCATION_REFRESH_TIME = 500;
    private static final float LOCATION_REFRESH_DISTANCE = (float) 0.05;
    private static final String onStatusTemporary = "Location Temporarily Unavailable";
    private static final String onStatusNoService = "Out of Service";
    private static final String onStatusService = "Service Available";
    private static final int duration = Toast.LENGTH_SHORT;
    private static final String disabled = "Provider Disabled";
    private static final String enabled = "Provider enabled";
    LocationManager mLocationManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton b1 = (ImageButton) findViewById(R.id.imageButton);
        //b1.setOnClickListener(openGaze);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            new CountDownTimer(500, 50) {
                public void onFinish() {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }

                public void onTick(long millisUntilFinished) {
                    // millisUntilFinished    The amount of time until finished.
                }
            }.start();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String text;
            if (status == 0) {
                text = onStatusNoService;
            } else if (status == 1) {
                text = onStatusTemporary;
            } else {
                text = onStatusService;
            }
            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast toast = Toast.makeText(getApplicationContext(), enabled, duration);
            toast.show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast toast = Toast.makeText(getApplicationContext(), disabled, duration);
            toast.show();
        }
    };
    View.OnClickListener openGaze = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d("LOG", "I'm in OpenGaze!");
            Log.d("LAT AND LOG: ", "is" + longitude + "and" + latitude);
            Intent intent = new Intent(getApplicationContext(), Gaze.class);
            intent.putExtra("Longitude", longitude);
            intent.putExtra("Latitude", latitude);
            Include.arraySort(longitude, latitude);
            startActivity(intent);
        }
    };

}

