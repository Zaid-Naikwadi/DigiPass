package com.example.rohan.hello;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import com.example.rohan.hello.RecyclerViewActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static com.example.rohan.hello.RecyclerViewActivity.sharedPrefs1;
import static com.example.rohan.hello.RecyclerViewActivity.sharedPrefs2;

public class AfterLogin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private int PREMISSION_LOC = 2;
     FirebaseUser user1=FirebaseAuth.getInstance().getCurrentUser();

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static double currentLatitude;
    public static double currentLongitude;
    TextView usermail;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_ticket:
                Toast.makeText(this, "my ticket", Toast.LENGTH_SHORT).show();
                Intent intentshowpass = new Intent(AfterLogin.this,MyTicket.class);
                startActivity(intentshowpass);
                break;
            case R.id.quick_charge:
                Toast.makeText(this, "quick charge", Toast.LENGTH_SHORT).show();
                break;
            case R.id.new_pass:

                Intent buypass = new Intent(AfterLogin.this,BuyTicket.class);
                startActivity(buypass);

                Toast.makeText(this, "new pass", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sos:
                final String NO_CONTACTS = "NoContacts";
                SharedPreferences no_contact = getSharedPreferences(NO_CONTACTS, MODE_PRIVATE);
                Toast.makeText(this, "SOS", Toast.LENGTH_SHORT).show();

                if(no_contact.getBoolean("noContacts",true))
                requestToSendSms();
                else
                {
                    AlertDialog.Builder builder;

                        builder = new AlertDialog.Builder(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                    builder.setTitle("Send Message")
                            .setMessage("Are you sure you want to Send Emergency Messages to added contacts?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with msg
                                    String CONTACTS_NO = "ContactsNo";
                                    SharedPreferences contact_number=getSharedPreferences(CONTACTS_NO,MODE_PRIVATE);

                                    String msg="S.O.S - I need help,my location:https://maps.google.com/maps?saddr=Current+Location&daddr="+currentLatitude+","+currentLongitude;
                                    Map<String, ?> allEntries1 = contact_number.getAll();
                                    for (Map.Entry<String, ?> entry : allEntries1.entrySet()) {
                                        Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                                        String phoneNo = entry.getValue().toString();
                                        sendSMS(phoneNo, msg);
                                    }

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
                break;

            case R.id.explore:
                Intent intent = new Intent(this, DisplayImagesActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Explore", Toast.LENGTH_SHORT).show();
                break;

            case R.id.near_by:
                Toast.makeText(this, "Near by", Toast.LENGTH_SHORT).show();
                break;

            case R.id.transaction:
                Intent intenttransaction = new Intent(this,PastTransactionActivity.class);
                startActivity(intenttransaction);
                Toast.makeText(this, "Past Transaction", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private CardView myTicketCard, quickChargeCard, newPassCard, sosCard,exploreCard,nearByCard,transactionCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        usermail = (TextView)findViewById(R.id.textView);
       user1=FirebaseAuth.getInstance().getCurrentUser();
        //String mail
        //

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        //access user properties
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference childDatabaseReference1 = databaseReference.child("Users");
        DatabaseReference childDatabaseReference = childDatabaseReference1.child(user.getUid());
        childDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(AfterLogin.this, ""+dataSnapshot.child("Aadhaar").getValue().toString(), Toast.LENGTH_SHORT).show();

                String utype = dataSnapshot.child("Type").getValue().toString();

                Variables.UserEmail= user.getEmail().toString();
                if(utype.equals(Variables.UserType_Student)){
                    Variables.UserType = 1;
                }else if(utype.equals(Variables.UserType_Regular)){
                    Variables.UserType = 2;
                }else if(utype.equals(Variables.UserType_OldAge)){
                    Variables.UserType = 3;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //  setFragment(new dataFragment());//init


        myTicketCard = (CardView) findViewById(R.id.my_ticket);
        newPassCard = (CardView) findViewById(R.id.new_pass);
        quickChargeCard = (CardView) findViewById(R.id.quick_charge);
        sosCard = (CardView) findViewById(R.id.sos);
        exploreCard = (CardView) findViewById(R.id.explore);
        nearByCard = (CardView) findViewById(R.id.near_by);
        transactionCard = (CardView) findViewById(R.id.transaction);



        myTicketCard.setOnClickListener(this);
        quickChargeCard.setOnClickListener(this);
        newPassCard.setOnClickListener(this);
        sosCard.setOnClickListener(this);
        exploreCard.setOnClickListener(this);
        nearByCard.setOnClickListener(this);
        transactionCard.setOnClickListener(this);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            //  setFragment(new dataFragment());
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_sos) {
            requestToSendSms();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_log_out) {
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fAuth.signOut();
            Intent intentToLogin = new Intent(this, MainActivity.class);
            startActivity(intentToLogin);
            Toast.makeText(this, "User signed out", Toast.LENGTH_SHORT).show();
        }


        return true;
    }

    public void setFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main1, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        usermail.setText(user1.getEmail());
        drawer.closeDrawer(GravityCompat.START);
    }

    void requestToSendSms() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else {
            Intent intentToSos = new Intent(this, RecyclerViewActivity.class);
            startActivity(intentToSos);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.

                //contact picker
                Intent intentToSos = new Intent(this, RecyclerViewActivity.class);
                startActivity(intentToSos);

            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            return;

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PREMISSION_LOC);
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
         /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}

