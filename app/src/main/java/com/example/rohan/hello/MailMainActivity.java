package com.example.rohan.hello;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MailMainActivity extends AppCompatActivity {

    private Button send;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mail_main);



        send = (Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {



            public void onClick(View v) {

                // TODO Auto-generated method stub
                new Thread(new Runnable() {

                    public void run() {

                        try {

                            GMailSender sender = new GMailSender(

                                    "vithackathon2018@gmail.com",

                                    "hack2018@#");

                            Log.d("sign in status","sign in success");

                            sender.addAttachment(Environment.getExternalStorageDirectory().getAbsolutePath()+"/bus/currentpass.jpg");

                            sender.sendMail("Test mail", "This mail has been sent from android app along with attachment",

                                    "vithackathon2018@gmail.com",

                                    "kanchan.chaudhari17@vit.edu");






                            Log.d("success or not","SUCCESSFULLY SENT EMAIL");





                        } catch (Exception e) {

                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();



                        }

                    }

                }).start();

            }

        });



    }


}
