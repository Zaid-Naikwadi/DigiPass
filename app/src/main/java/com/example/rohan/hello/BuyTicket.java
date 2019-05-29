package com.example.rohan.hello;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.time.*;
import java.util.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuyTicket extends AppCompatActivity implements View.OnClickListener{

    private CardView daily_pass,weekly_pass,monthly_pass,quaterly_pass,halfyr_pass,yearly_pass;
    private TextView text_daily,text_weekly,text_monthly,text_quaterly,text_halfyr,text_yearly;
    final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        //check usertype and set usertype

        //Variables.UserType=2;

        //card objects
        daily_pass = (CardView)findViewById(R.id.daily);
        weekly_pass = (CardView)findViewById(R.id.weekly);
        monthly_pass = (CardView)findViewById(R.id.monthly);
        quaterly_pass = (CardView)findViewById(R.id.quaterly);
        halfyr_pass = (CardView)findViewById(R.id.halfyearly);
        yearly_pass = (CardView)findViewById(R.id.yearly);

        //add listeners to the cards
        daily_pass.setOnClickListener(this);
        weekly_pass.setOnClickListener(this);
        monthly_pass.setOnClickListener(this);
        quaterly_pass.setOnClickListener(this);
        halfyr_pass.setOnClickListener(this);
        yearly_pass.setOnClickListener(this);

        //text objects
        text_daily= (TextView)findViewById(R.id.dailypass);
        text_weekly = (TextView)findViewById(R.id.weeklypass);
        text_monthly = (TextView)findViewById(R.id.monthlypass);
        text_quaterly = (TextView)findViewById(R.id.quaterlypass);
        text_halfyr = (TextView)findViewById(R.id.halfyearlypass);
        text_yearly = (TextView)findViewById(R.id.yearlypass);


        //access user properties
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference childDatabaseReference1 = databaseReference.child("Users");
        DatabaseReference childDatabaseReference = childDatabaseReference1.child(user.getUid());
        childDatabaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //access and store user properties
                String utype = dataSnapshot.child("Type").getValue().toString();
                Variables.usertypestr=utype;
                Variables.UserEmail= user.getEmail().toString();
                Variables.UserMobileNumber=dataSnapshot.child("Mobile").getValue().toString();
                Variables.UserAdharNumber=dataSnapshot.child("Aadhaar").getValue().toString();
                Variables.UserDOB=dataSnapshot.child("Dob").getValue().toString();

                //store the cuurent date
                // Variables.Current_date = LocalDate.now().toString();
                // create a calendar


                //current date
                Variables.Current_date = cal.getTime().toString();


                // add 20 days to the calendar
                //cal.add(Calendar.DATE, 20);
                // Toast.makeText(BuyTicket.this, ""+cal.getTime(), Toast.LENGTH_SHORT).show();



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


        //initialize set ticket text based on the current user

        if(Variables.UserType == 1){
            //student ahe
            text_daily.setText("Ticket Price RS="+ Variables.Student_daily_rate);
            text_weekly.setText("Ticket Price RS="+ Variables.Student_weekly_rate);
            text_monthly.setText("Ticket Price RS="+ Variables.Student_monthly_rate);
            text_quaterly.setText("Ticket Price RS="+ Variables.Student_quaterly_rate);
            text_halfyr.setText("Ticket Price RS="+ Variables.Student_halfyr_rate);
            text_yearly.setText("Ticket Price RS="+ Variables.Student_yearly_rate);
        }else if(Variables.UserType == 2){
            //regular ahe
            text_daily.setText("Ticket Price RS="+ Variables.Regular_daily_rate);
            text_weekly.setText("Ticket Price RS="+ Variables.Regular_weekly_rate);
            text_monthly.setText("Ticket Price RS="+ Variables.Regular_monthly_rate);
            text_quaterly.setText("Ticket Price RS="+ Variables.Regular_quaterly_rate);
            text_halfyr.setText("Ticket Price RS="+ Variables.Regular_halfyr_rate);
            text_yearly.setText("Ticket Price RS="+ Variables.Regular_yearly_rate);

        }else if(Variables.UserType == 3){
            //Old-Age ahe
            text_daily.setText("Ticket Price RS="+ Variables.Old_daily_rate);
            text_weekly.setText("Ticket Price RS="+ Variables.Old_weekly_rate);
            text_monthly.setText("Ticket Price RS="+ Variables.Old_monthly_rate);
            text_quaterly.setText("Ticket Price RS="+ Variables.Old_quaterly_rate);
            text_halfyr.setText("Ticket Price RS="+ Variables.Old_halfyr_rate);
            text_yearly.setText("Ticket Price RS="+ Variables.Old_yearly_rate);
        }

    }


    @Override
    public void onClick(View view){
        //called when card is clicked

        switch (view.getId()){

            case R.id.daily :

                if(Variables.UserType == 1){
                    Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
                    // cal.add(Calendar.DATE, 0);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Student_daily_rate);
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                    Variables.Current_Price=Variables.Student_daily_rate;
                    i.putExtra("p_type","daily");
                    startActivity(i);
                }
                else if(Variables.UserType == 2){
                    Toast.makeText(this, "Regular", Toast.LENGTH_SHORT).show();
                    //cal.add(Calendar.DATE, 0);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Regular_daily_rate);
                    Variables.Current_Price=Variables.Regular_daily_rate;
                    i.putExtra("p_type","daily");
                    startActivity(i);
                }
                else{
                    Toast.makeText(this, "Old", Toast.LENGTH_SHORT).show();
                    //cal.add(Calendar.DATE, 0);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Old_daily_rate);
                    Variables.Current_Price=Variables.Old_daily_rate;
                    i.putExtra("p_type","daily");
                    startActivity(i);
                }
                Toast.makeText(this, "clicked daily", Toast.LENGTH_SHORT).show();
                break;

            case R.id.weekly :
                if(Variables.UserType == 1){
                    Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.DAY_OF_MONTH, 6);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Student_weekly_rate);
                    Variables.Current_Price=Variables.Student_weekly_rate;
                    i.putExtra("p_type","weekly");
                    startActivity(i);
                }
                else if(Variables.UserType == 2){
                    Toast.makeText(this, "Regular", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.DAY_OF_MONTH, 6);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Regular_weekly_rate);
                    Variables.Current_Price=Variables.Regular_weekly_rate;
                    i.putExtra("p_type","weekly");
                    startActivity(i);
                }
                else{
                    Toast.makeText(this, "Old", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.DAY_OF_MONTH, 6);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Old_weekly_rate);
                    Variables.Current_Price=Variables.Old_weekly_rate;
                    i.putExtra("p_type","weekly");
                    startActivity(i);
                }
                Toast.makeText(this, "clicked weekly", Toast.LENGTH_SHORT).show();
                break;

            case R.id.monthly :
                if(Variables.UserType == 1){
                    Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.MONTH, 1);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Student_monthly_rate);
                    Variables.Current_Price=Variables.Student_monthly_rate;
                    i.putExtra("p_type","monthly");
                    startActivity(i);
                }
                else if(Variables.UserType == 2){
                    Toast.makeText(this, "Regular", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.MONTH, 1);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Regular_monthly_rate);
                    i.putExtra("p_type","monthly");
                    Variables.Current_Price=Variables.Regular_monthly_rate;
                    startActivity(i);
                }
                else{
                    Toast.makeText(this, "Old", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.MONTH, 1);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Old_monthly_rate);
                    i.putExtra("p_type","monthly");
                    Variables.Current_Price=Variables.Old_monthly_rate;
                    startActivity(i);
                }
                Toast.makeText(this, "clicked monthly", Toast.LENGTH_SHORT).show();
                break;

            case R.id.quaterly :
                if(Variables.UserType == 1){
                    Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.MONTH, 3);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Student_quaterly_rate);
                    Variables.Current_Price=Variables.Student_quaterly_rate;
                    i.putExtra("p_type","quaterly");
                    startActivity(i);
                }
                else if(Variables.UserType == 2){
                    Toast.makeText(this, "Regular", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.MONTH, 3);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Regular_quaterly_rate);
                    Variables.Current_Price=Variables.Regular_quaterly_rate;
                    i.putExtra("p_type","quaterly");
                    startActivity(i);
                }
                else{
                    Toast.makeText(this, "Old", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.MONTH, 3);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Old_quaterly_rate);
                    i.putExtra("p_type","quaterly");
                    Variables.Current_Price=Variables.Old_quaterly_rate;
                    startActivity(i);
                }             Toast.makeText(this, "clicked quaterly", Toast.LENGTH_SHORT).show();
                break;

            case R.id.halfyearly :
                if(Variables.UserType == 1){
                    Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.MONTH, 6);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Student_halfyr_rate);
                    Variables.Current_Price=Variables.Student_halfyr_rate;
                    i.putExtra("p_type","half yearly");
                    startActivity(i);
                }
                else if(Variables.UserType == 2){
                    Toast.makeText(this, "Regular", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.MONTH, 6);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Regular_halfyr_rate);
                    Variables.Current_Price=Variables.Regular_halfyr_rate;
                    i.putExtra("p_type","half yearly");
                    startActivity(i);
                }
                else{
                    Toast.makeText(this, "Old", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.MONTH, 6);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Old_halfyr_rate);
                    Variables.Current_Price=Variables.Old_halfyr_rate;
                    i.putExtra("p_type","half yearly");
                    startActivity(i);
                }                Toast.makeText(this, "clicked half yearly", Toast.LENGTH_SHORT).show();
                break;

            case R.id.yearly :
                if(Variables.UserType == 1){
                    Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.YEAR, 1);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Student_yearly_rate);
                    Variables.Current_Price=Variables.Student_yearly_rate;
                    i.putExtra("p_type","yearly");
                    startActivity(i);
                }
                else if(Variables.UserType == 2){
                    Toast.makeText(this, "Regular", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.YEAR, 1);
                    Variables.Expiration_date = cal.getTime().toString();
                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Regular_yearly_rate);
                    Variables.Current_Price=Variables.Regular_yearly_rate;
                    i.putExtra("p_type","yearly");
                    startActivity(i);
                }
                else{
                    Toast.makeText(this, "Old", Toast.LENGTH_SHORT).show();
                    cal.add(Calendar.YEAR, 1);
                    Variables.Expiration_date = cal.getTime().toString();

                    Intent i = new Intent(BuyTicket.this, PaymentGatewayMainActivity.class);
                    i.putExtra("price", Variables.Old_yearly_rate);
                    Variables.Current_Price=Variables.Old_yearly_rate;
                    i.putExtra("p_type","yearly");
                    startActivity(i);
                }
                Toast.makeText(this, "clicked yearly", Toast.LENGTH_SHORT).show();
                break;

            default:break;

        }

    }

}