package com.theprotectors.theprotectors;

import android.Manifest;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

public class dangerous_place extends AppCompatActivity implements OnMapReadyCallback, ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = new_tag.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleMap mMap;

    SharedPreferences closeTags = null;
    Location current= new Location(" ");
    List<TagDataBase> list = new ArrayList<>();
    List<ImageView> imagesview =new ArrayList<>();

    //SharedPreferences tagList =null;

    private LatLng loc_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangerous_place);

        ImageView dPic1= (ImageView) findViewById(R.id.imageView10);
        ImageView dPic2= (ImageView) findViewById(R.id.imageView11);
        ImageView dPic3= (ImageView) findViewById(R.id.imageView12);

        imagesview.add(dPic1);
        imagesview.add(dPic2);
        imagesview.add(dPic3);

        TabHost tabHost = (TabHost)findViewById(R.id.tabhost); // initiate TabHost
        tabHost.setup();

        TabSpec spec1=tabHost.newTabSpec("TAG1");
        TabSpec spec2=tabHost.newTabSpec("TAB 2");
        TabSpec spec3=tabHost.newTabSpec("TAB 2");

        spec1.setContent(R.id.tab1);
        spec2.setContent(R.id.tab2);
        spec3.setContent(R.id.tab3);

        spec1.setIndicator("TAG1");
        spec2.setIndicator("TAG2");
        spec3.setIndicator("TAG3");

        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map6);
        mapFragment.getMapAsync(this);

        mLocationRequest = new LocationRequest();
        closeTags = getSharedPreferences("LatLng", MODE_PRIVATE);
        //int t= closeTags.getInt("locationCount", MODE_PRIVATE);



        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                2);

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setTitle("Dangerous Place");
        }


    }

    private void handleNewLocation(Location location) throws IOException {
        Log.d(TAG, location.toString());
        Geocoder geocoder;

        geocoder = new Geocoder(this, Locale.getDefault());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        current.setLatitude(location.getLatitude());
        current.setLongitude(location.getLongitude());


        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        loc_now = latLng;
        //EditText EditAdr = (EditText) findViewById(R.id.address);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


    }

    private void SetPic(List<TagDataBase> tList)
    {
        int length = tList.size();
        for(int j=0; j<length; j++)
        {
            List<String> picPath= tList.get(j).getPath();
            int pathlength= picPath.size();
            if(! picPath.get(0).isEmpty())
            {
                Bitmap bitmap = BitmapFactory.decodeFile(picPath.get(0));
                imagesview.get(j).setImageBitmap(bitmap);
            }
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        int tagnum=closeTags.getInt("locationCount", MODE_PRIVATE);
        //closeTags.edit().putString("Lat"+ Integer.toString(tagnum),Double.toString(37.4220)).commit();
        //closeTags.edit().putString("Lng"+ Integer.toString(tagnum),Double.toString(-122.0800)).commit();
        //closeTags.edit().putInt("locationCount", tagnum+1).commit();
        LatLng l;

        //double currentLatitude = location.getLatitude();
        //double currentLongitude = location.getLongitude();

        if(tagnum!=0) {
            String mylat = "";
            String mylng = "";
            {
                for (int i = 0; i < tagnum; i++) {
                    mylat = closeTags.getString("Lat" + i, "0");
                    mylng = closeTags.getString("Lng" + i, "0");
                    Location newplace= new Location("");
                    newplace.setLatitude(Double.parseDouble(mylat));
                    newplace.setLongitude(Double.parseDouble(mylng));

                    float distance = newplace.distanceTo(current)/10000;
                    Log.d(TAG,Float.toString(distance)+"      "+"YYYYYYYYYYYY");
                    SharedPreferences t = getSharedPreferences("taglist",MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = t.getString("TagDataBass"+i,"0");
                    Log.d(TAG,"TST     "+json);
                    TagDataBase tagdanger = gson.fromJson(json,TagDataBase.class);

                    if(Math.abs(distance)<=1500)
                    {
                        if(list.size()<4) {
                            list.add(tagdanger);
                        }
                        l = new LatLng(Double.parseDouble(mylat), Double.parseDouble(mylng));
                        double l1=Double.parseDouble(mylat);
                        double l2=Double.parseDouble(mylng);

                        /*if(!tagdander.getPath().isEmpty()) {
                            List<String> Path = tagdander.getPath();
                            //Path = tag_dander.getPath();
                            if (!Path.get(0).isEmpty()) {
                                Bitmap bitmap = BitmapFactory.decodeFile(Path.get(0));
                                ImageView image = (ImageView) findViewById(R.id.imageView10);
                                if (image.getDrawable() == null) {
                                    image.setImageBitmap(bitmap);
                                }
                            }
                        }*/

                        MarkerOptions markerOptions = new MarkerOptions();
                        // Setting latitude and longitude for the marker
                        markerOptions.position(l)
                                .title(Integer.toString(i));
                        // Adding marker on the Google Map
                        mMap.addMarker(markerOptions);

                        mMap.animateCamera(CameraUpdateFactory.newLatLng(l));

                    }

                }
            }
        }
        if(list.size()!=0) {
            SetPic(list);
        }


    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        current=location;
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
        } else {
            try {
                handleNewLocation(location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        mGoogleApiClient.disconnect();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("dangerous_place Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


        }
    }
}
