package com.theprotectors.theprotectors;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class new_tag extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    private static final String TAG = new_tag.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleMap mMap;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;
    private Uri imageUri;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private File photoFile = null;
    private EditText EditAdr;
    private int count;
    private int imageC;
    private LatLng loc;
    private int pass=0;
    private int locnum;
    private int N=0;

    List<ImageView> views = new ArrayList<>();
    SharedPreferences new_loc = null;
    SharedPreferences discrip = null;
    //SharedPreferences taglist = null;
    SharedPreferences images = null;
    List<String> path= new ArrayList<>();
    TagDataBase tag = new TagDataBase();
    List<TagDataBase> tagL = new ArrayList<>();
    SharedPreferences reasonSpinner = null;
    private String choice;
    private String text;
    //TagDBHandler tagManage = new TagDBHandler(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                2);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map5);
        mapFragment.getMapAsync(this);
        final Button savebutton2 = (Button) this.findViewById(R.id.save_cur);

        EditAdr = (EditText) findViewById(R.id.address2);
        new_loc = getSharedPreferences("LatLng",MODE_PRIVATE);
        images = getSharedPreferences("imagePass",MODE_PRIVATE);
        EditText desText= (EditText) findViewById(R.id.editText4);
        reasonSpinner =getSharedPreferences("reason",MODE_PRIVATE);
        discrip=getSharedPreferences("dis",MODE_PRIVATE);

        desText.setOnEditorActionListener(new OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==EditorInfo.IME_ACTION_DONE)
                {
                    text=textView.getText().toString();
                    //Toast.makeText(new_tag.this, textView.getText().toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG,text+"    --------DESC        ");

                    return true;
                }
                return false;
            }
        });
        Log.d(TAG,"YESSSSSS！  "+text);

        Spinner spinner =(Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                  String[] reasonSp = getResources().getStringArray(R.array.reason);
                                                  choice = adapterView.getItemAtPosition(i).toString();
                                                  Log.d(TAG, choice+ "   is reason");
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> adapterView) {
                                                  //String[] reasonSp = getResources().getStringArray(R.array.reason);
                                                  choice = adapterView.getItemAtPosition(0).toString();
                                                  Log.d(TAG, choice+ "   is reason");

                                              }
                                          });
                //text=desText.getText().toString();

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setTitle("Location Report");
        }


        imageView = (ImageView) findViewById(R.id.imageView6);
        imageView2 = (ImageView) findViewById(R.id.imageView5);
        imageView3 = (ImageView) findViewById(R.id.imageView7);
        views.add(imageView);
        views.add(imageView2);
        views.add(imageView3);

        ImageButton photoButton = (ImageButton) this.findViewById(R.id.imageButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this.imageView = (ImageView) findViewById(R.id.imageView);
                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go

                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        count++;
                        if(imageC<3)
                        {
                            imageC++;
                            //ImagesList.add(photoFile);
                            //images.edit().putString("imageURI"+Integer.toString((imageC-1)),mCurrentPhotoPath).commit();
                        }
                        Uri photoURI = FileProvider.getUriForFile(getBaseContext(),
                                "com.theprotectors.theprotectors.fileprovider",
                                photoFile);

                        Log.d(TAG, mCurrentPhotoPath);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                        if(count >3)
                        {
                            Log.d(TAG,"ONLY 3 !!!!!!");

                        }
                    }

                }
            }
        });
        savebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i= new_loc.getInt("locationCount",0);
                i++;
                N=i-1;

                new_loc.edit().putString("Lat"+Integer.toString((i-1)),Double.toString(loc.latitude)).commit();
                new_loc.edit().putString("Lng"+ Integer.toString((i-1)),Double.toString(loc.longitude)).commit();
                new_loc.edit().putInt("locationCount", i).commit();

                if(!path.get(0).isEmpty()) {
                    images.edit().putString("imagepath1", path.get(0)).commit();
                    // Log.d(TAG, "NEW_TAG!!!!!!!"+"  "+ images.getString("imagepath1" + (i - 1),"0"));
                }
                else if(!path.get(1).isEmpty()) {
                    images.edit().putString("imagepath2", path.get(1)).commit();
                }
                else if(!path.get(2).isEmpty()) {
                    images.edit().putString("imagepath3", path.get(2)).commit();
                }
                //TagDBHandler tagList= new TagDBHandler(getApplicationContext());
                //TagDataBase tagtemp = new TagDataBase()
                tag.setPath(path);
                SharedPreferences tagpass=getSharedPreferences("taglist",MODE_PRIVATE);
                Gson gson = new Gson();
                String json = gson.toJson(tag);
                tagpass.edit().putString("TagDataBass"+Integer.toString((i-1)), json).commit();
                reasonSpinner.edit().putString("SpinnerReasons"+Integer.toString((i-1)), choice).commit();
                discrip.edit().putString("edittext"+Integer.toString((i-1)),text).commit();
                Log.d(TAG,Integer.toString((i-1))+"  "+ "Integer TESTTTTTTT");


                Intent i2= new Intent(getApplicationContext(),ride_detail.class);
                Bundle args0 = new Bundle();
                args0.putParcelable("current", loc);
                i2.putExtra("bundle_current", args0);
                startActivity(i2);


            }
        });
        /*TagDBHandler tagRead = new TagDBHandler(getApplicationContext());
        SQLiteDatabase read = tagRead.getReadableDatabase();

        String[] T ={TagDBHandler.tagentry.ID_NOTE,
                TagDBHandler.tagentry.PATH_1,
                TagDBHandler.tagentry.PATH_2,
                TagDBHandler.tagentry.PATH_3,
                TagDBHandler.tagentry.LONTITUDE,
                TagDBHandler.tagentry.LATITUDE,
                TagDBHandler.tagentry.MARKER_OPTIONS};*/

        mLocationRequest = new LocationRequest();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //Cursor c= read.query(TagDBHandler.tagentry.TABLE_SHOPS,
          //      T,null, null, null,null, null);
        //Log.d(TAG, T[1]+"   "+T[2]+"   "+ T[3]);*/
    }


    private void handleNewLocation(Location location) throws IOException {
        Log.d(TAG, location.toString());
        Geocoder geocoder;
        locnum++;

        geocoder = new Geocoder(this, Locale.getDefault());
        double currentLatitude = location.getLatitude();

        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        loc = latLng;
        //EditText EditAdr = (EditText) findViewById(R.id.address);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(Integer.toString(N));
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        tag.setLat(currentLatitude);
        tag.setLong(currentLongitude);
        tag.setTitle(Integer.toString(locnum-1));
        List<Address> addresses;
        addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        EditAdr.setText(address + city + state + country, TextView.BufferType.EDITABLE);

    }


    @Override
    public void onConnected(Bundle bundle) {

        /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        2);
            }
        }*/
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);
        } else {
            try {
                handleNewLocation(location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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


    @Override
    public void onConnectionSuspended(int i) {

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null ) {
        ImageView image= views.get(imageC-1);
        if (image.getDrawable() == null) {
            pass++;
            path.add(mCurrentPhotoPath);
            //String path= images.getString("imgURI"+i, "0");
            int targetW = image.getWidth();
            int targetH = image.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);


            tag.setPath(path);
            Log.d(TAG, tag.getPath().get(pass-1));
            //Intent imagepass = new Intent(getApplicationContext(),loc_report.class);
            //imagepass.putExtra("imagepass"+Integer.toString(pass-1), mCurrentPhotoPath);
            //startActivity(imagepass);

            image.setImageBitmap(bitmap);
        }
        //imageView.setImageURI(Uri.fromFile(photoFile));

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
    public void onLocationChanged(Location location) {
        try {
            handleNewLocation(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }



}
