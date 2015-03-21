package com.example.vikas.newsgaze;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static android.hardware.SensorManager.getOrientation;


public class MainActivity extends ActionBarActivity implements SensorEventListener {
    private static final long LOCATION_REFRESH_TIME = 500;
    private static final float LOCATION_REFRESH_DISTANCE = (float) 0.05;
    private static final String onStatusTemporary = "Location Temporarily Unavailable";
    private static final String onStatusNoService = "Out of Service";
    private static final String onStatusService = "Service Available";
    private static final int duration = Toast.LENGTH_SHORT;
    private static final String disabled = "Provider Disabled";
    private static final String enabled = "Provider enabled";
    LocationManager mLocationManager = null;
    private final SensorManager mSensorManager;
    private final Sensor mAccelerometer;
    private static final float [] mData=null;
    private static final float [] gData = null;
    private static final float [] R = null;
    private static final float [] Imat = null;
    private static final float [] orientation = null;
    private static boolean haveData = false;
    private static final String TAG = "TAG";
    private static final double DEG = 180/Math.PI;

    public MainActivity(SensorManager mSensorManager) {
        this.mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        this.mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        long now = event.timestamp;     // ns

        switch( event.sensor.getType() ) {
            case Sensor.TYPE_ACCELEROMETER:
                gData[0] = event.values[0];
                gData[1] = event.values[1];
                gData[2] = event.values[2];
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mData[0] = event.values[0];
                mData[1] = event.values[1];
                mData[2] = event.values[2];
                haveData = true;
                break;
        }

        if( haveData ) {

            SensorManager.getRotationMatrix(R, Imat, gData, mData);
            getOrientation(R, orientation);


            Log.d(TAG, "yaw: " + (int)(orientation[0]*DEG));
            Log.d(TAG, "pitch: " + (int)(orientation[1]*DEG));
            Log.d(TAG, "roll: " + (int)(orientation[2]*DEG));

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
           double longitude = location.getLongitude();
           double latitude =  location.getLatitude();
           Log.d(TAG,"THE LATITUDE IS: "+latitude);
           Log.d(TAG,"THE LONGITUDE IS: "+longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String text;
            if(status==0){
                text = onStatusNoService;
            }
            else if(status==1){
                text = onStatusTemporary;
            }
            else {
                text = onStatusService;
            }
            Toast toast = Toast.makeText(getApplicationContext(),text,duration);
            toast.show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast toast = Toast.makeText(getApplicationContext(),enabled,duration);
            toast.show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast toast = Toast.makeText(getApplicationContext(),disabled,duration);
            toast.show();
        }
    };

}
