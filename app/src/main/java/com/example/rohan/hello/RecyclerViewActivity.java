package com.example.rohan.hello;

import android.content.ContentUris;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rohan.hello.RecyclerViewAdapter;
import com.example.rohan.hello.ItemTouchHelperCallback;
import com.example.rohan.hello.AfterLogin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.rohan.hello.AfterLogin.currentLatitude;
import static com.example.rohan.hello.AfterLogin.currentLongitude;

public class RecyclerViewActivity extends AppCompatActivity{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private RecyclerViewAdapter adapter;
    private int color = 0;
    private List<String> data,numbers;
    private String insertData;
    private boolean loading;
    private int loadTimes;
    private int numberOfContactsAdded=0;

    public static SharedPreferences sharedPrefs,sharedPrefs1,sharedPrefs2;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String CONTACTS_NO = "ContactsNo";
    public static final String NO_CONTACTS = "NoContacts";

    //declare globally, this can be any int
    public final int PICK_CONTACT = 2015;

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    public RecyclerViewActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);


        Toolbar toolbar = findViewById(R.id.toolbar_recycler_view);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initData();
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                String number = "";
                String name = "";
                String id = "";
                Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                cursor.moveToFirst();
                String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                if (hasPhone.equals("1")) {
                    Cursor phones = getContentResolver().query
                            (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        number = phones.getString(phones.getColumnIndex
                                (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
                        name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).replaceAll("[-()]","");
                        id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

                    }
                    phones.close();
                    //Do something with number
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    SharedPreferences.Editor editor1 = getSharedPreferences(CONTACTS_NO, MODE_PRIVATE).edit();
                    SharedPreferences.Editor editor2 = getSharedPreferences(NO_CONTACTS, MODE_PRIVATE).edit();
                    Log.d("Number:ID",number+":"+id);
                    editor.putString(id, name);
                    editor1.putString(id, number);
                    editor2.putBoolean("noContacts",false);

                    editor.apply();
                    editor1.apply();
                    editor2.apply();

                    Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    adapter.addItem(linearLayoutManager.findFirstVisibleItemPosition() + 1, name,number);
                } else {
                    Toast.makeText(getApplicationContext(), "This contact has no phone number", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        }
    }

    private void initData() {
        sharedPrefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        sharedPrefs1 = getSharedPreferences(CONTACTS_NO, MODE_PRIVATE);
        sharedPrefs2 = getSharedPreferences(NO_CONTACTS, MODE_PRIVATE);

        data = new ArrayList<>();
        numbers = new ArrayList<>();
        String phoneNo="";

        String msg="S.O.S - I need help,my location:https://maps.google.com/maps?saddr=Current+Location&daddr="+currentLatitude+","+currentLongitude;
        if(sharedPrefs2.getBoolean("noContacts",true)){
            //none contacts are added
            Toast.makeText(this, "There r no contacts", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Contacts", Toast.LENGTH_SHORT).show();
            Map<String, ?> allEntries = sharedPrefs.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                    data.add(entry.getValue().toString());
            }

            Map<String, ?> allEntries1 = sharedPrefs1.getAll();
            for (Map.Entry<String, ?> entry : allEntries1.entrySet()) {
                Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                    numbers.add(entry.getValue().toString());
                    phoneNo = entry.getValue().toString();
                   // sendSMS(phoneNo,msg);

            }


        }

       // data = new ArrayList<>();
       // for (int i = 1; i <= 1; i++) {
       //     data.add(i + "");
       // }

        insertData = "0";
        loadTimes = 0;
    }

    private void initView() {
        fab = findViewById(R.id.fab_recycler_view);
        mRecyclerView = findViewById(R.id.recycler_view_recycler_view);

        if (getScreenWidthDp() >= 1200) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else if (getScreenWidthDp() >= 800) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }

        adapter = new RecyclerViewAdapter(this);
        mRecyclerView.setAdapter(adapter);
        adapter.setItems(data,numbers);

       // adapter.addFooter();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
               // adapter.addItem(linearLayoutManager.findFirstVisibleItemPosition() + 1, insertData);

                requestContactPermission();


                    }});



        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_recycler_view);
        swipeRefreshLayout.setColorSchemeResources(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (color > 4) {
                            color = 0;
                        }
                        adapter.setColor(++color);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });

       // mRecyclerView.addOnScrollListener(scrollListener);
    }

  /*  RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (!loading && linearLayoutManager.getItemCount() == (linearLayoutManager.findLastVisibleItemPosition() + 1)) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (loadTimes <= 5) {
                          //  adapter.removeFooter();
                            loading = false;
                            adapter.addItems(data);
                            adapter.addFooter();
                            loadTimes++;
                        } else {
                        //    adapter.removeFooter();
                            Snackbar.make(mRecyclerView, getString(R.string.no_more_data), Snackbar.LENGTH_SHORT).setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    super.onDismissed(transientBottomBar, event);
                                    loading = false;
                                    adapter.addFooter();
                                }
                            }).show();
                        }
                    }
                }, 1500);

                loading = true;
            }
        }
    };*/


    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }

    void requestContactPermission()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        else {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    //contact picker
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            // other 'case' lines to check for other
            // permissions this app might request.
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

}
