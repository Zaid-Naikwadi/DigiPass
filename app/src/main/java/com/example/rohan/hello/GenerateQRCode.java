package com.example.rohan.hello;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;

public class GenerateQRCode extends AppCompatActivity {
    public final static int WIDTH=500;
    private Button gohome;
    FirebaseUser user;

    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        user= FirebaseAuth.getInstance().getCurrentUser();

        //assign values used for qr code from intent
       // String Email = "Email="+getIntent().getStringExtra("email")+"\n";
        String Email = "User Email Address is ="+ Variables.UserEmail;
        String AdharNum = "Aadhaar Number is ="+ Variables.UserAdharNumber;
        String Dob = "DOB ="+ Variables.UserDOB;
        String usertypestr = "User Type is ="+ Variables.usertypestr;
        String mobilenum = "Mobile Number is ="+ Variables.UserMobileNumber;
        int Ticket_price ;// getIntent().getExtras().getInt("price");
        Ticket_price=Variables.Current_Price;
        String price_str= "Price ="+String.valueOf(Ticket_price)+"\n";
        String issuedate="Pass Issue date is ="+ Variables.Current_date;
        String expiredate="Pass Expire date is ="+ Variables.Expiration_date;


        gohome = (Button)findViewById(R.id.gohome);
        img=(ImageView)findViewById(R.id.qr);
        Bitmap bitmap = null;
        try {
            StringBuffer all = new StringBuffer();
            String alldata;
            String text = "Ticket Details Are\n";

            all.append(text+"\n");
            all.append(Email+"\n");
            all.append(AdharNum+"\n");
            all.append(mobilenum+"\n");
            all.append(Dob+"\n");
            all.append(usertypestr+"\n");
            all.append(price_str+"\n");
            all.append(issuedate+"\n");
            all.append(expiredate+"\n");
            alldata=all.toString();
            Toast.makeText(this, "COMPLETE DATA IS "+alldata, Toast.LENGTH_LONG).show();

            bitmap = encodeAsBitmap(alldata);

            img.setImageBitmap(bitmap);

            FileOutputStream outStream = null;
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/bus");
            dir.mkdirs();
            //String fileName = String.format("%d.jpg", System.currentTimeMillis());
            String fileName = "currentpass.jpg";
            File outFile = new File(dir, fileName);
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();


            //write logic to access image from storage and send via email

        }catch (Exception e)
        {

        }

        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {

                    public void run() {

                        try {

                            GMailSender sender = new GMailSender(

                                    "vithackathon2018@gmail.com",

                                    "hack2018@#");

                            Log.d("sign in status","sign in success");

                            sender.addAttachment(Environment.getExternalStorageDirectory().getAbsolutePath()+"/bus/currentpass.jpg");

                            sender.sendMail("Your Bus Pass QRCode", "This generated QR Code is valid.Regards-DigiPass App inc.",

                                    "vithackathon2018@gmail.com",

                                    user.getEmail().toString());






                            Log.d("success or not","SUCCESSFULLY SENT EMAIL");





                        } catch (Exception e) {

                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();



                        }

                    }

                }).start();
                Intent go = new Intent(GenerateQRCode.this,AfterLogin.class);
                startActivity(go);
            }
        });
    }


    Bitmap encodeAsBitmap(String str) throws WriterException
    {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);

        return bitmap;


    }
}

