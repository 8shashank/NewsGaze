package com.shashank_sharma.newsgaze2;

        import android.content.Intent;
        import android.location.Location;
        import android.location.LocationManager;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;

        import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getLocation
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude =location.getLongitude();
        latitude = location.getLatitude();

        Log.d("LAT AND LOG: ", "is" + longitude + "and" + latitude);
    }

    public void openGaze() {
        Intent intent = new Intent(this, Gaze.class);

        City[] cities=new City[63];
        for(int i=0;i<cities.length;i++)
        {
            double dist=Math.pow((longitude-Include.city_coordinates[i][0]),2)+
                Math.pow(((latitude-Include.city_coordinates[i][1])),2);
            cities[i]=new City(Include.city_names[i],dist,Include.city_coordinates[i][0],
                    Include.city_coordinates[i][1]);
        }
        Arrays.sort(cities);
        double latlong[]={longitude,latitude};
        intent.putExtra("latlong",latlong);
        startActivity(intent);
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