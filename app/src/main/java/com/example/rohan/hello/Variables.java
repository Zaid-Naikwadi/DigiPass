package com.example.rohan.hello;

/**
 * Created by Admin on 16-03-2018.
 */

public class Variables {

    static String UserAdharNumber ;//= "123456789007";
    static String UserDOB,usertypestr,Current_date,Expiration_date;
    static int UserType;   //1-student 2-regular 3-old 4.conductor
    static String UserEmail,UserMobileNumber;// = "kanchan.chaudhari17@vit.edu";//get current user email id and assign dynamically to this

    static String UserType_Regular = "Regular";
    static String UserType_OldAge = "Old-Age";
    static String UserType_Student = "Student";
    /*
    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
    String userSignedIn=user.getEmail();
    */
    static int Current_Price;
    final static int Student_daily_rate = 10;
    final static int Student_weekly_rate = 20;
    final static int Student_monthly_rate = 30;
    final static int Student_quaterly_rate = 40;
    final static int Student_halfyr_rate = 50;
    final static int Student_yearly_rate = 60;

    final static int Regular_daily_rate = 70;
    final static int Regular_weekly_rate = 80;
    final static int Regular_monthly_rate = 90;
    final static int Regular_quaterly_rate = 100;
    final static int Regular_halfyr_rate = 110;
    final static int Regular_yearly_rate = 120;

    final static int Old_daily_rate = 1;
    final static int Old_weekly_rate = 2;
    final static int Old_monthly_rate = 3;
    final static int Old_quaterly_rate = 4;
    final static int Old_halfyr_rate = 5;
    final static int Old_yearly_rate = 6;

}
