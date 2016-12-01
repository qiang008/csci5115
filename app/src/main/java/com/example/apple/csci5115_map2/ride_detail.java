package com.example.apple.csci5115_map2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class ride_detail extends AppCompatActivity implements OnMapReadyCallback{

    private static final String TAG = "ride_detail";
    private GoogleMap mMap;
    private int mylocationCount = 0;
    private boolean check=false;
    List<Marker> markerList = new ArrayList<Marker>();
    String myzoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);


        Button button= (Button) this.findViewById(R.id.button2);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setTitle("Ride Detail");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), tags.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap=map;
        LatLng l;
        final SharedPreferences myprefs= getSharedPreferences("LatLng",MODE_PRIVATE);
        mylocationCount = myprefs.getInt("locationCount", MODE_PRIVATE);

        // Getting stored zoom level if exists else return 0
        myzoom = myprefs.getString("zoom", "0");
        if(mylocationCount!=0) {
            String mylat = "";
            String mylng = "";
            {
                for (int i = 0; i < mylocationCount; i++) {
                    mylat = myprefs.getString("Lat" + i, "0");
                    mylng = myprefs.getString("Lng" + i, "0");
                    l = new LatLng(Double.parseDouble(mylat), Double.parseDouble(mylng));
                    Marker marker ;
                    // Creating an instance of MarkerOptions
                    MarkerOptions markerOptions = new MarkerOptions();
                    // Setting latitude and longitude for the marker
                    markerOptions.position(l)
                                 .title(Integer.toString(i));
                    // Adding marker on the Google Map
                    marker= mMap.addMarker(markerOptions);

                    mMap.animateCamera(CameraUpdateFactory.newLatLng(l));
                    markerList.add(marker);
                }
            }
        }
        mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < mylocationCount; i++) {
                    //String mylat2 = myprefs.getString("Lat" + i, "0");
                    //String mylng2 = myprefs.getString("Lng" + i, "0");
                    //LatLng l2 = new LatLng(Double.parseDouble(mylat2), Double.parseDouble(mylng2));
                    String tagnumber = Integer.toString(i);
                    if (marker.getTitle().equals(tagnumber)) {
                        Intent jumpToReport = new Intent(getApplicationContext(), report_check.class);
                        Bundle send = new Bundle();
                        Log.d(TAG, "GOT!!!!!");
                        send.putParcelable("mark_position", marker.getPosition());
                        jumpToReport.putExtra("title", marker.getTitle());
                        jumpToReport.putExtra("Location", mylocationCount);
                        jumpToReport.putExtra("send", send);
                        startActivity(jumpToReport);
                        return true;
                    }
                    else {
                        Log.d(TAG, "NOT CHOOSE!!!!!");
                    }
                }
                    return false;
            }
        });

            //mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);




    }
    private void drawMarker(LatLng point){
        Marker marker ;
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        // Adding marker on the Google Map
        marker= mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
        markerList.add(marker);
    }


    /*private void ShareToFacebook(){
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
    }*/
}
