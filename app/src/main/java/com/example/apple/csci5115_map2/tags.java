package com.example.apple.csci5115_map2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class tags extends AppCompatActivity implements OnMapReadyCallback{

    //Marker m = null;
    SharedPreferences prefs = null;

    private GoogleMap mMap;
    private LatLng point;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setTitle("Tags");
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map3);
        mapFragment.getMapAsync( this);

        prefs = this.getSharedPreferences("LatLng",MODE_PRIVATE);
         //Check whether your preferences contains any values then we get those values

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        mMap=map;

        if((prefs.contains("Lat")) && (prefs.contains("Lng")))
        {
            String lat = prefs.getString("Lat","");
            String lng = prefs.getString("Lng","");
            LatLng l =new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
            drawMarker(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
               // m= mMap.addMarker(new MarkerOptions().position(latLng));

                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(latLng);
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                mMap.addMarker(markerOptions);

                prefs.edit().putString("Lat",Double.toString(latLng.latitude)).commit();
                prefs.edit().putString("Lng",Double.toString(latLng.longitude)).commit();

                Intent intent = new Intent(getApplicationContext(), loc_report.class);
                Bundle args = new Bundle();
                args.putParcelable("from_position", latLng);
                intent.putExtra("bundle", args);
                startActivity(intent);

            }

        });



    }

    private void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
         // Setting latitude and longitude for the marker
        markerOptions.position(point);
         // Adding marker on the Google Map
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
        }


}
