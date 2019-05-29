package com.example.rohan.hello;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class CollegeLogin extends AppCompatActivity {

   private EditText editTextUserId,editTextPassword;
   private Button ButtonSignin;
   private String inputuserid,inputpassword;
   private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_login);
        this.setTitle("College Login");
        editTextPassword=(EditText)findViewById(R.id.signinpassword1);
        editTextUserId=(EditText)findViewById(R.id.signinemail);
        ButtonSignin=(Button)findViewById(R.id.signinbutton1);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayoutSignUp);
        ButtonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int flag=validate(editTextUserId.getText().toString(),editTextPassword.getText().toString());
                try {
                    if (flag == 1) {
                        final SpotsDialog spotsDialog = new SpotsDialog(CollegeLogin.this, R.style.Custom1);
                        spotsDialog.show();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference();
                        DatabaseReference childDatabaseReference1 = databaseReference.child("College_School");
                        DatabaseReference childDatabaseReference = childDatabaseReference1.child(editTextUserId.getText().toString());
                        childDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                String userid = dataSnapshot.child("id").getValue().toString();
                                String password = dataSnapshot.child("password").getValue().toString();
                                inputpassword = editTextPassword.getText().toString();
                                inputuserid = editTextUserId.getText().toString();
                                if (userid.equals(inputuserid))
                                {
                                    if (password.equals(inputpassword))
                                    {
                                        spotsDialog.dismiss();
                                        Intent intentToStudentVerify=new Intent(CollegeLogin.this,StudentVerificationActivity.class);
                                        startActivity(intentToStudentVerify);
                                        //Toast.makeText(CollegeLogin.this, "Login success", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        spotsDialog.dismiss();
                                        Toast.makeText(CollegeLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                     }
                                }
                                else
                                {
                                    spotsDialog.dismiss();
                                    Toast.makeText(CollegeLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(CollegeLogin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    int validate(String username,String password)
    {
        int f=1;
        if(username.isEmpty())
        {
            editTextUserId.setError("User name empty");
            f=0;
        }
        if(password.isEmpty())
        {
            Snackbar.make(linearLayout,"Password",Snackbar.LENGTH_SHORT).show();
            f=0;
        }
        return f;
    }
}
