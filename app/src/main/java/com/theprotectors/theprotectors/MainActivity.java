package com.theprotectors.theprotectors;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ShakeEventManager.ShakeListener {

    private DrawerLayout drawer;
    final Context context = this;

    private ShakeEventManager shakeEventManager;

    private enum AppState {
        RIDE_ON,
        RIDE_OFF,
        CRASH_DETECTED
    }

    private AppState appState = AppState.RIDE_OFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);

        // Restore preferences
        SharedPreferences settings = getPreferences(MODE_PRIVATE);

        shakeEventManager = new ShakeEventManager();
        shakeEventManager.setListener(this);
        shakeEventManager.init(this);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView leftNavigationView = (NavigationView) findViewById(R.id.notifications_view);
        leftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle left navigation view item clicks here
                int id = item.getItemId();

//                if (id == R.id.nav_camera) {
//                    Toast.makeText(MainActivity.this, "Left Drawer - Import", Toast.LENGTH_SHORT).show();
//                } else if (id == R.id.nav_gallery) {
//                    Toast.makeText(MainActivity.this, "Left Drawer - Gallery", Toast.LENGTH_SHORT).show();
//                } else if (id == R.id.nav_slideshow) {
//                    Toast.makeText(MainActivity.this, "Left Drawer - Slideshow", Toast.LENGTH_SHORT).show();
//                } else if (id == R.id.nav_manage) {
//                    Toast.makeText(MainActivity.this, "Left Drawer - Tools", Toast.LENGTH_SHORT).show();
//                } else if (id == R.id.nav_share) {
//                    Toast.makeText(MainActivity.this, "Left Drawer - Share", Toast.LENGTH_SHORT).show();
//                } else if (id == R.id.nav_send) {
//                    Toast.makeText(MainActivity.this, "Left Drawer - Send", Toast.LENGTH_SHORT).show();
//                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_view);
        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Right navigation view item clicks here.
                int id = item.getItemId();

//                if (id == R.id.nav_settings) {
//                    Toast.makeText(MainActivity.this, "Right Drawer - Settings", Toast.LENGTH_SHORT).show();
//                } else if (id == R.id.nav_logout) {
//                    Toast.makeText(MainActivity.this, "Right Drawer - Logout", Toast.LENGTH_SHORT).show();
//                } else if (id == R.id.nav_help) {
//                    Toast.makeText(MainActivity.this, "Right Drawer - Help", Toast.LENGTH_SHORT).show();
//                } else if (id == R.id.nav_about) {
//                    Toast.makeText(MainActivity.this, "Right Drawer - About", Toast.LENGTH_SHORT).show();
//                }

                drawer.closeDrawer(GravityCompat.END); /*Important Line*/
                return true;
            }
        });

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_notifications);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }

        // Set primary contact name

        // TODO: Set up a listener for primary contact changes

        int primaryContactId = settings.getInt("primaryContactId", -1);
        if (primaryContactId == -1) { // No primary contact set
//            View noPrimContactText = findViewById(R.id.no_primcontact);
//            View addFirstContactButton = findViewById(R.id.add_first_contact);
//
//            noPrimContactText.setVisibility(View.VISIBLE);
//            addFirstContactButton.setVisibility(View.VISIBLE);
//
//            View primContactLabel = findViewById(R.id.primcontact_label);
//            View primContactName = findViewById(R.id.primcontact_name);
//            View primContactChangeButton = findViewById(R.id.primcontact_change);
//
//            primContactLabel.setVisibility(View.GONE);
//            primContactName.setVisibility(View.GONE);
//            primContactChangeButton.setVisibility(View.GONE);
        }
        else {
            TextView primContactName = (TextView) findViewById(R.id.primcontact_name);
            primContactName.setText(settings.getString("primaryContactName", ""));
        }

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startRide();
            }
        });

        View stopButton = findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                endRide();
            }
        });

        Button test =(Button) findViewById(R.id.button6);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jumpToDanger = new Intent(getApplicationContext(),dangerous_place.class );
                startActivity(jumpToDanger);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {  /*Closes the Appropriate Drawer*/
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_navigation) {
            drawer.openDrawer(GravityCompat.END); /*Opens the Right Drawer*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startRide() {
        appState = AppState.RIDE_ON;

        shakeEventManager.register();

        View startButtonView = findViewById(R.id.start_button_layout);
        View stopButtonView = findViewById(R.id.stop_button_layout);
        startButtonView.setVisibility(View.GONE);
        stopButtonView.setVisibility(View.VISIBLE);

        onShake();// for testing
    }

    public void resumeRide() {
        appState = AppState.RIDE_ON;

        shakeEventManager.register();
    }

    public void endRide() {
        appState = AppState.RIDE_OFF;

        shakeEventManager.deregister();

        View startButtonView = findViewById(R.id.start_button_layout);
        View stopButtonView = findViewById(R.id.stop_button_layout);
        stopButtonView.setVisibility(View.GONE);
        startButtonView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onShake() {
        if (appState == AppState.RIDE_ON) {
            appState = AppState.CRASH_DETECTED;
            shakeEventManager.deregister();

            // invoke emergency contact text after the set amount of time
            final Handler emergencyContactHandler = new Handler();
            final Runnable textEmergencyContact = new Runnable() {
                @Override
                public void run() {
                    Log.i("", "Send the text");
                    sendEmergencyText();
                    // if unsuccessful
                    // if successful
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle(R.string.crash_detected_title);

                    String message = "We detected a crash and sent a text to " + "Lilly Smith" + " to let her know that you may need help. If you'd like, we can send another text to let them know you're okay.";

                    alertDialogBuilder
                            .setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton("Send the text", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    sendFollowUpText();
                                    dialogInterface.cancel();
                                }
                            })
                            .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            };
            emergencyContactHandler.postDelayed(textEmergencyContact, 30000);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(R.string.crash_detected_title);

            String message = "We've detected a crash. If you haven't told us you're okay in the next " + "30 seconds" + ", we'll text your emergency contact to let them know you need help.";

            alertDialogBuilder
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.im_okay, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            resumeRide();
                            emergencyContactHandler.removeCallbacks(textEmergencyContact);
                            dialogInterface.cancel();
                        }
                    })
                    .setNegativeButton(R.string.i_need_help, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            endRide();
                            sendEmergencyText();
                            dialogInterface.cancel();
                        }
                    });

            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            // dismiss dialog if no response
            Handler dismissHandler = new Handler();
            Runnable dismissDialog = new Runnable() {
                @Override
                public void run() {
                    endRide();
                    alertDialog.dismiss();
                }
            };
            dismissHandler.postDelayed(dismissDialog, 30000);
        }
    }

    private boolean sendEmergencyText() {
        String phoneNo = "5554"; // use port number for testing
        String message = "emergency!!";
        sendSMS(phoneNo, message);
        return true;
    }

    private boolean sendFollowUpText() {
        String phoneNo = "5554";
        String message = "follow-up text";
        sendSMS(phoneNo, message);
        return true;
    }

    public void sendSMS(String phoneNo, String message) {
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNo, null, message, pi, null);
    }

    public void openAct(View view) {
        Intent intent = new Intent(MainActivity.this, new_tag.class);
        startActivity(intent);
    }
}
