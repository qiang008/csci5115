package com.example.apple.csci5115_map2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class report_check extends AppCompatActivity implements OnMapReadyCallback{
    private static final String TAG = "report_check";
    private GoogleMap mMap;
    boolean markerClicked;
    private String title;
    private int locationC;
    SharedPreferences ridepref=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_check);

        Button remove =(Button) this.findViewById(R.id.button4);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setTitle("Lane is closed");
        }

        final ImageButton edition= (ImageButton) this.findViewById(R.id.edit);
        final EditText text= (EditText) this.findViewById(R.id.editText3);
        text.setMovementMethod(new ScrollingMovementMethod());

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map4);
        mapFragment.getMapAsync( this);

        text.setFocusable(false);
        edition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setFocusableInTouchMode(true);
            }
        });

        text.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    Log.d(TAG, "DONE!!!!!");
                    //text.setFocusable(false);
                    return true;
                }
                return false;
            }

        });

        remove.setOnClickListener( new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                mMap.clear();
                int i= Integer.valueOf(title);
                SharedPreferences ridepref = getSharedPreferences("LatLng",0);
                ridepref.edit().remove("Lat"+i).commit();
                ridepref.edit().remove("Lng"+i).commit();
                ridepref.edit().putInt("locationCount",(locationC-1)).commit();
                Intent back = new Intent(getApplicationContext(), tags.class);
                startActivity(back);
            }
        });
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
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        //mMap.setOnMarkerClickListener(this);
        Bundle bundle = getIntent().getParcelableExtra("send");
        LatLng fromPosition = bundle.getParcelable("mark_position");
        title = getIntent().getStringExtra("title");
        locationC = getIntent().getIntExtra("Location",0);
        drawMarker(fromPosition);
    }

    private void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        // Setting latitude and longitude for the marker
        markerOptions.position(point)
                     .title(title);
        // Adding marker on the Google Map
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
    }

}
