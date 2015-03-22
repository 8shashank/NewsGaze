package com.shashank_sharma.newsgaze2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import static android.hardware.SensorManager.getOrientation;


public class Gaze extends ActionBarActivity implements SensorEventListener {
    private static final long LOCATION_REFRESH_TIME = 500;
    private static final float LOCATION_REFRESH_DISTANCE = (float) 0.05;
    private static final String onStatusTemporary = "Location Temporarily Unavailable";
    private static final String onStatusNoService = "Out of Service";
    private static final String onStatusService = "Service Available";
    private static final int duration = Toast.LENGTH_SHORT;
    private static final String disabled = "Provider Disabled";
    private static final String enabled = "Provider enabled";
    LocationManager mLocationManager = null;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private Sensor mMagneticField;

    private static float [] mData= new float[3];
    private static float [] gData = new float[3];
    private static final float [] R = new float[36];
    public static final float [] outR= new float[36];                //output Rotational Matrix
    private static final float [] Imat = new float [36];
    private static final float [] orientation = new float[3];
    private static boolean haveData = false;
    private static final String TAG = "TAG";
    private static final double DEG = 180/Math.PI;
    static final float ALPHA = 0.7f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Log.d(TAG,"Enters on Create()");
        this.mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        this.mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
    }
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gData = lowPass(event.values.clone(), gData);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mData = lowPass(event.values.clone(), mData);
        }
        if (gData != null && mData != null) {
            SensorManager.getRotationMatrix(R, Imat, gData, mData);
            Display display =
                    ((WindowManager)getSystemService(getApplicationContext().WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();
            if (rotation == 1) {
                SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_MINUS_Z, outR);
            } else {
                SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_Z, outR);
            }
            SensorManager.getOrientation(R, orientation);
            float azimuth = (float)(((orientation[0]*180)/Math.PI)+180);
            float pitch = (float)(((orientation[1]*180/Math.PI))+90);
            float roll = (float)(((orientation[2]*180/Math.PI)));


            Log.d(TAG, "yaw: " + azimuth);
            Log.d(TAG, "pitch: " + pitch);
            Log.d(TAG, "roll: " + roll);

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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

    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

}