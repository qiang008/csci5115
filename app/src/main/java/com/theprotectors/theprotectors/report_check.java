package com.theprotectors.theprotectors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class report_check extends AppCompatActivity implements OnMapReadyCallback{
    private static final String TAG = "report_check";
    private GoogleMap mMap;
    boolean markerClicked;
    private String title;
    private int locationC;
    private int number;
    SharedPreferences ridepref=null;
    SharedPreferences imageGet=null;
    SharedPreferences get=null;
    List<ImageView> pic = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_check);
        imageGet=getSharedPreferences("imagePass",0);

        ImageView image1=(ImageView) findViewById(R.id.imageView9);
        ImageView image2=(ImageView) findViewById(R.id.imageView8);
        ImageView image3=(ImageView) findViewById(R.id.imageView2);
        pic.add(image1);
        pic.add(image2);
        pic.add(image3);

        //get= getSharedPreferences("taglist",MODE_PRIVATE);
        //Gson gson = new Gson();
        //String json = get.getString("TagDataBase", "0");
        //TagDataBase tag=gson.fromJson(json, TagDataBase.class);


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
                locationC --;
                int i= Integer.valueOf(title);
                SharedPreferences ridepref = getSharedPreferences("LatLng",0);
                ridepref.edit().remove("Lat"+i).commit();
                ridepref.edit().remove("Lng"+i).commit();
                ridepref.edit().putInt("locationCount",locationC).commit();
                imageGet.edit().remove("imagepass"+i).commit();
                Intent back = new Intent(getApplicationContext(), tags.class);
                startActivity(back);
            }
        });

        //image();
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
        number = Integer.parseInt(title);
        locationC = getIntent().getIntExtra("Location",0);
        drawMarker(fromPosition);
        putImage();
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

    private void putImage() {

        for(int n=0; n<3; n++)
        {
            if(pic.get(n).getDrawable() != null)
            {
                pic.get(n).setImageDrawable(null);
            }
        }
        List<String> paths= getIntent().getExtras().getStringArrayList("pathList");

        int size= paths.size();
        for(int l= 0; l<size; l++) {
            if (!paths.get(l).isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(paths.get(l));
                pic.get(l).setImageBitmap(bitmap);
            }
        }

    }



}
