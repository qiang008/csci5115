package com.theprotectors.theprotectors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
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

public class loc_report extends AppCompatActivity
        implements  OnMapReadyCallback {

    private static final String TAG = loc_report.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleMap mMap;

    private static final int REQUEST_TAKE_PHOTO = 1888;
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
    private String tText;

    private int tagNum;
    private String choice;

    List<File> ImagesList = new ArrayList<>();
    List<ImageView> views= new ArrayList<>();
    List<String> TagPath = new ArrayList<>();

    SharedPreferences spin = null;
    SharedPreferences images=null;
    SharedPreferences tags=null;
    SharedPreferences disc = null;

    TagDataBase NewTag = new TagDataBase();
    //private LatLng point1;
    //private double point2

    //Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_report);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final Button savebutton = (Button) this.findViewById(R.id.button3);

        images= getSharedPreferences("image_path", 0);
        tags = getSharedPreferences("LatLng",MODE_PRIVATE);
        EditAdr = (EditText) findViewById(R.id.address);
        spin =getSharedPreferences("reason",MODE_PRIVATE);
        disc=getSharedPreferences("dis",MODE_PRIVATE);

        EditText textdis= (EditText) findViewById(R.id.description_text) ;
        /*textdis.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE)
                {
                    tText=textView.getText().toString();
                    //Toast.makeText(new_tag.this, textView.getText().toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG,tText+"    --------DESC        ");

                    return true;
                }
                return false;
            }
        });*/
        textdis.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    tText=textView.getText().toString();
                    Log.d(TAG,tText+"    --------DESC        ");
                    return true;
                }
                return false;
            }
        });


        //tag=tags.getInt("fromTag", 0);
        //geocoder = new Geocoder(this, Locale.getDefault());

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setTitle("Location Report");
        }

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView4);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        views.add(imageView);
        views.add(imageView2);
        views.add(imageView3);
        //getimage();

        Spinner spinner =(Spinner) findViewById(R.id.stop_reason);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //String[] reasonSp = getResources().getStringArray(R.array.reason);
                choice = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, choice+ "   is reason");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String[] reasonSp = getResources().getStringArray(R.array.reason);
                choice = reasonSp[0];
                Log.d(TAG, choice+ "   is reason");

            }
        });

        ImageButton photoButton = (ImageButton) this.findViewById(R.id.addingPic);
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
                            ImagesList.add(photoFile);
                            images.edit().putString("imageURI"+Integer.toString((imageC-1)),mCurrentPhotoPath).commit();
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

        savebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(), tags.class);
                if(!TagPath.isEmpty()) {
                    NewTag.setPath(TagPath);
                }
                else if (TagPath.isEmpty())
                {
                    TagPath=null;
                }

                SharedPreferences tagpass=getSharedPreferences("taglist",MODE_PRIVATE);
                Gson gson = new Gson();
                String json = gson.toJson(NewTag);
                tagpass.edit().putString("TagDataBass"+Integer.toString((tagNum)), json).commit();
                disc.edit().putString("edittext"+Integer.toString((tagNum)),tText).commit();
                spin.edit().putString("SpinnerReasons"+Integer.toString((tagNum)),choice).commit();
                startActivity(i);
            }
        });


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Geocoder geocoder2;
        LatLng point = recieveData();
        geocoder2 = new Geocoder(this, Locale.getDefault());
        MarkerOptions options2 = new MarkerOptions()
                    .position(point)
                    .title("new Mark!");
            mMap.addMarker(options2);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        NewTag.setLong(point.longitude);
        NewTag.setLat(point.latitude);
        NewTag.setTitle(Integer.toString(tagNum));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            List<Address> addresses1 = new ArrayList<>();
            try {
                addresses1 = geocoder2.getFromLocation(point.latitude, point.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String address = addresses1.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses1.get(0).getLocality();
            String state = addresses1.get(0).getAdminArea();
            String country = addresses1.get(0).getCountryName();
            EditAdr.setText(address + city + state + country, TextView.BufferType.EDITABLE);

    }

    private File createImageFile() throws IOException {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null ) {
             ImageView image= views.get(imageC-1);
            if (image.getDrawable() == null) {
                //String path= images.getString("imgURI"+i, "0");
                int targetW = image.getWidth();
                int targetH = image.getHeight();
                TagPath.add(mCurrentPhotoPath);

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
                image.setImageBitmap(bitmap);
            }

            //imageView.setImageURI(Uri.fromFile(photoFile));

    }


    private LatLng recieveData()
    {
            Bundle bundle = getIntent().getParcelableExtra("bundle");
            LatLng fromPosition = bundle.getParcelable("from_position");
            tagNum = bundle.getInt("number");

            return fromPosition;

    }

    /*public void getimage()
    {
        for(int i=0; i<3; i++) {
            ImageView imageSet = views.get(i);
            if(imageSet.getDrawable()==null)
            {
                String bit= getIntent().getStringExtra("imagepass"+i);
                Bitmap bitmap = BitmapFactory.decodeFile(bit);
                imageSet.setImageBitmap(bitmap);
            }
        }
    }*/




}