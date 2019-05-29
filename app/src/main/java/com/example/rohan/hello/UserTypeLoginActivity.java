package com.example.rohan.hello;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class UserTypeLoginActivity extends AppCompatActivity {

   // private FloatingActionButton floatingActionButtonUserLogin,floatingActionButtonInstituteLogin;
    Button userLogin,instituteLogin;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private LinearLayout linearLayout;
    private int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_login);
        /*floatingActionButtonInstituteLogin=(FloatingActionButton)findViewById(R.id.instituteLoginButton);
        floatingActionButtonUserLogin=(FloatingActionButton)findViewById(R.id.userLoginButton);
        floatingActionButtonUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToMainActivity=new Intent(UserTypeLoginActivity.this,MainActivity.class);
                startActivity(intentToMainActivity);
            }
        });
        floatingActionButtonInstituteLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToCollegeLogin=new Intent(UserTypeLoginActivity.this,CollegeLogin.class);
                startActivity(intentToCollegeLogin);
            }
        });*/

      /*  if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }*/

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        userLogin = (Button)findViewById(R.id.userLoginButton);
        instituteLogin = (Button)findViewById(R.id.instituteLoginButton);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayoutSignUp);

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToMainActivity=new Intent(UserTypeLoginActivity.this,MainActivity.class);
                startActivity(intentToMainActivity);
            }
        });;
        instituteLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intentToCollegeLogin=new Intent(UserTypeLoginActivity.this,CollegeLogin.class);
                startActivity(intentToCollegeLogin);
            }
        });
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
