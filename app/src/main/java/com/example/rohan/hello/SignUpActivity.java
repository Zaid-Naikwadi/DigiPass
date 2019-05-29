
package com.example.rohan.hello;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ybs.passwordstrengthmeter.PasswordStrength;

import dmax.dialog.SpotsDialog;

public class SignUpActivity extends AppCompatActivity {
    private EditText editTextAadhaarCardNumber,editTextEmailId,editTextPassword,editTextConfirmPassword,editTextInstitueName;
    private TextView textViewName,textViewDob,textViewGender,textViewMobileNumber;
    private int flagValidate;
    private Button buttonGetDetails;
    private Button buttonSignUp;
    private String name,dob,gender,userType;
    private  String mobilenumber;
    private Spinner spinnerUserSelect;
    private String[] arrayUserType={"Regular","Student","Old-Age","Select User Type"};
    private LinearLayout linearLayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "UserRegistration";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SQLiteDatabase sqLiteDatabase;
    private int clg;
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.setTitle("Sign Up");


        //initialization goes here
        mobilenumber=new String();
        textViewMobileNumber=(TextView)findViewById(R.id.mobile_number);
        buttonGetDetails=(Button) findViewById(R.id.getd);
        editTextAadhaarCardNumber=(EditText)findViewById(R.id.aadharcard);
        editTextEmailId=(EditText)findViewById(R.id.emailaddress);
        textViewDob=(TextView)findViewById(R.id.dob);
        textViewName=(TextView)findViewById(R.id.name);
        textViewGender=(TextView)findViewById(R.id.gender);
        editTextInstitueName=(EditText)findViewById(R.id.instituteName);
        buttonSignUp=(Button) findViewById(R.id.signupbutton);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayoutSignUp);
        editTextPassword=(EditText)findViewById(R.id.signuppassword);
        editTextConfirmPassword=(EditText)findViewById(R.id.signuppasswordconfirm);
        spinnerUserSelect=(Spinner)findViewById(R.id.usertypeselect) ;
        ArrayAdapter<String> arrayAdapterUserType = new ArrayAdapter<String>(SignUpActivity.this, R.layout.spinner_item, arrayUserType);
        spinnerUserSelect.setAdapter(arrayAdapterUserType);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        clg=0;
        sqLiteDatabase=openOrCreateDatabase("stud1", Context.MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("Create table  if not exists stud(roll varchar);");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        HintAdapter hintAdapter=new HintAdapter(this, R.layout.spinner_item,arrayUserType);
        spinnerUserSelect.setAdapter(hintAdapter);
        // show hint
        spinnerUserSelect.setSelection(hintAdapter.getCount());
        View v = spinnerUserSelect.getSelectedView();




        //Set the listener for when each option is clicked.
        spinnerUserSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                userType=parent.getItemAtPosition(position).toString();
                if(userType.contains("Student"))
                {
                    editTextInstitueName.setVisibility(View.VISIBLE);
                }
                else
                {
                    editTextInstitueName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });




        buttonGetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final SpotsDialog spotsDialog = new SpotsDialog(SignUpActivity.this, R.style.Custom1);
                    spotsDialog.show();
                    FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference1 = firebaseDatabase1.getReference();
                    DatabaseReference childDatabaseReference2 = databaseReference1.child("Adhaar_Card_Used");
                    //DatabaseReference childDatabaseReference3 = childDatabaseReference2.child(editTextAadhaarCardNumber.getText().toString());
                    childDatabaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Toast.makeText(SignUpActivity.this, "" + dataSnapshot.child(editTextAadhaarCardNumber.getText().toString()).exists(), Toast.LENGTH_SHORT).show();
                            if (!dataSnapshot.child(editTextAadhaarCardNumber.getText().toString()).exists()) {
                                String email = editTextEmailId.getText().toString();
                                String aadharCard = editTextAadhaarCardNumber.getText().toString();

                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference();
                                DatabaseReference childDatabaseReference1 = databaseReference.child("Adhaar_Card");
                                DatabaseReference childDatabaseReference = childDatabaseReference1.child(aadharCard);
                                //Data is Valid so Upload on data-base

                                childDatabaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {

                                            name = new String();
                                            name += dataSnapshot.child("name").getValue().toString();
                                            dob = new String();
                                            dob += dataSnapshot.child("DOB").getValue().toString();
                                            gender = new String();
                                            gender += dataSnapshot.child("gender").getValue().toString();
                                            mobilenumber = dataSnapshot.child("mobile").getValue().toString();
                                            textViewName.setText("Name:" + name);
                                            textViewDob.setText("DOB:" + dob);
                                            textViewGender.setText("Gender:" + gender);
                                            textViewMobileNumber.setText(mobilenumber);
                                            textViewName.setVisibility(View.VISIBLE);
                                            textViewMobileNumber.setVisibility(View.VISIBLE);
                                            textViewDob.setVisibility(View.VISIBLE);
                                            textViewGender.setVisibility(View.VISIBLE);
                                            spotsDialog.dismiss();
                                            buttonSignUp.setEnabled(true);
                                        } catch (Exception e) {
                                            Toast.makeText(SignUpActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                                            spotsDialog.dismiss();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(SignUpActivity.this, "User Already exists", Toast.LENGTH_SHORT).show();
                                textViewMobileNumber.setVisibility(View.INVISIBLE);
                                textViewGender.setVisibility(View.INVISIBLE);
                                textViewName.setVisibility(View.INVISIBLE);
                                textViewDob.setVisibility(View.INVISIBLE);
                                spotsDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                catch (NullPointerException e)
                {
                    Toast.makeText(SignUpActivity.this, "u cansign up", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password=charSequence.toString();
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                TextView strengthView = (TextView) findViewById(R.id.password_strength);
                if (TextView.VISIBLE != strengthView.getVisibility())
                    return;

                if (password.isEmpty()) {
                    strengthView.setText("");
                    progressBar.setProgress(0);
                    return;
                }

                PasswordStrength str = PasswordStrength.calculateStrength(password);
                strengthView.setText(str.getText(SignUpActivity.this));
                strengthView.setTextColor(str.getColor());

                progressBar.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);

                if (str.getText(SignUpActivity.this).equals("Weak")) {
                    progressBar.setProgress(25);
                } else if (str.getText(SignUpActivity.this).equals("Medium")) {
                    progressBar.setProgress(50);
                } else if (str.getText(SignUpActivity.this).equals("Strong")) {
                    progressBar.setProgress(75);
                } else {
                    progressBar.setProgress(100);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=editTextEmailId.getText().toString();
                final String password=editTextPassword.getText().toString();
                String confirmPassword=editTextConfirmPassword.getText().toString();

                flagValidate =validateData(email,password,confirmPassword,userType);
                if(flagValidate==1 && clg==0)
                {
                    final SpotsDialog spotsDialog = new SpotsDialog(SignUpActivity.this, R.style.Custom1);
                    spotsDialog.show();

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (task.isSuccessful())
                                    {
                                        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                        final DatabaseReference myRef1 = database1.getReference("Adhaar_Card_Used");
                                        DatabaseReference childDBR3 = myRef1.child(editTextAadhaarCardNumber.getText().toString());
                                        childDBR3.child("Aadhaar").setValue(editTextAadhaarCardNumber.getText().toString());


                                        spotsDialog.dismiss();
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        final DatabaseReference myRef = database.getReference("Users");
                                        databaseReference=database.getReference();
                                        if(userType.contains("Regular"))
                                        {
                                           // DatabaseReference child1 = myRef.child("Regular");
                                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                            DatabaseReference childDBR = myRef.child(user.getUid());
                                            childDBR.child("Aadhaar").setValue(editTextAadhaarCardNumber.getText().toString());
                                            childDBR.child("Name").setValue(name);
                                            childDBR.child("Email").setValue(email);
                                            childDBR.child("Mobile").setValue(textViewMobileNumber.getText().toString());
                                            childDBR.child("Dob").setValue(textViewDob.getText().toString());
                                            childDBR.child("Type").setValue(userType);

                                            try {

                                                user.sendEmailVerification();
                                            }
                                            catch (Exception e)
                                            {
                                                Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            Toast.makeText(SignUpActivity.this, "User registered , Verification link has been sent to your Email-Id", Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(SignUpActivity.this,MainActivity.class);
                                            FirebaseAuth.getInstance().signOut();
                                            spotsDialog.dismiss();
                                            startActivity(i);
                                        }
                                        if(userType.contains("Old-Age"))
                                        {
                                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                            DatabaseReference childDBR = myRef.child(user.getUid());
                                            childDBR.child("Aadhaar").setValue(editTextAadhaarCardNumber.getText().toString());
                                            childDBR.child("Name").setValue(name);
                                            childDBR.child("Email").setValue(email);
                                            childDBR.child("Mobile").setValue(textViewMobileNumber.getText().toString());
                                            childDBR.child("Dob").setValue(textViewDob.getText().toString());
                                            childDBR.child("Type").setValue(userType);


                                            try {
                                               // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                user.sendEmailVerification();
                                            }
                                            catch (Exception e)
                                            {
                                                Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            Toast.makeText(SignUpActivity.this, "User registered , Verification link has been sent to your Email-Id", Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(SignUpActivity.this,MainActivity.class);
                                            FirebaseAuth.getInstance().signOut();
                                            spotsDialog.dismiss();
                                            startActivity(i);
                                        }
                                        if(userType.contains("Student"))
                                        {
                                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                            DatabaseReference childDBR = myRef.child(user.getUid());
                                            childDBR.child("Aadhaar").setValue(editTextAadhaarCardNumber.getText().toString());
                                            childDBR.child("Name").setValue(name);
                                            childDBR.child("Email").setValue(email);
                                            childDBR.child("Mobile").setValue(textViewMobileNumber.getText().toString());
                                            childDBR.child("Dob").setValue(textViewDob.getText().toString());
                                            childDBR.child("Type").setValue(userType);

                                            childDBR.child("InstName").setValue(editTextInstitueName.getText().toString());
                                            Toast.makeText(SignUpActivity.this, "Registration done please contact for Verification process", Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(SignUpActivity.this,MainActivity.class);
                                            FirebaseAuth.getInstance().signOut();
                                            spotsDialog.dismiss();
                                            startActivity(i);
                                        }
                                        ;



                                    }
                                    else
                                    {
                                        spotsDialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                }

            }
        });


    }
    int validateData(String email,String password,String confirmPassword,String userType)
    {

        final int flag=0;
        PasswordStrength str = PasswordStrength.calculateStrength(password);
        if(!email.contains("@"))
        {
            editTextEmailId.setError("Please enter valid email-id");
            return flag;
        }
        if(password.length()<=8)
        {
            Snackbar.make(linearLayout,"Password must be at least of length of 9",Snackbar.LENGTH_SHORT).show();
            return flag;
        }

        if(str.getText(SignUpActivity.this).equals("Weak") || str.getText(SignUpActivity.this).equals("Medium") )
        {
            Snackbar.make(linearLayout,"Password must contain combination of special characters and numbers",Snackbar.LENGTH_SHORT).show();
            return flag;
        }
        if(!password.equals(confirmPassword))
        {
            Snackbar.make(linearLayout,"Password doesn't match",Snackbar.LENGTH_SHORT).show();
            return flag;
        }

        if(userType.contains("Select User Type"))
        {
            Toast.makeText(this, "User type cannot be empty", Toast.LENGTH_SHORT).show();
            return flag;
        }
        if(userType.contains("Student"))
        {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            DatabaseReference childDatabaseReference1 = databaseReference.child("College_School");
            DatabaseReference childDatabaseReference = childDatabaseReference1.child(editTextInstitueName.getText().toString());
            childDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                   if(!dataSnapshot.exists())
                   {
                       clg=1;
                       Toast.makeText(SignUpActivity.this, "College haven't registered with us.", Toast.LENGTH_SHORT).show();
                       Intent intentToItself=new Intent(SignUpActivity.this,SignUpActivity.class);
                       startActivity(intentToItself);

                   }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        String[] yearOfBirth=textViewDob.getText().toString().split("/");
         int yob=Integer.parseInt(yearOfBirth[2]);
        yob=2018-yob;
        if(userType.contains("Old-Age") && yob<60)
        {
            Snackbar.make(linearLayout,"Select valid user type",Snackbar.LENGTH_SHORT).show();
            return flag;
        }
        return 1;
    }
}