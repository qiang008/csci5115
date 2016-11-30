package com.example.apple.csci5115_map2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ride_detail extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

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
        LatLng point=recieveData2();
        if(point != null) {
            MarkerOptions options2 = new MarkerOptions()
                    .position(point)
                    .title("new Mark!");
            mMap.addMarker(options2);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        }


    }
    private LatLng recieveData2()
    {
        LatLng l;
        SharedPreferences myprefs= getSharedPreferences("LatLng",MODE_PRIVATE);
        if((myprefs.contains("Lat")) && (myprefs.contains("Lng")))
        {
            String lat = myprefs.getString("Lat","");
            String lng = myprefs.getString("Lng","");
            l =new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
            //drawMarker(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
            return l;
        }

        return null;
    }



    /*private void ShareToFacebook(){
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
    }*/
}
