package com.example.rohan.hello;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.xml.datatype.Duration;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity
{
    private AutoCompleteTextView editTextEmail;
    private EditText editTextPassword;
    private LinearLayout linearLayout;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "SignInActivity";
    private String email,password;
    private Button login_button,forgot_password,sign_up;
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        this.setTitle("Login");

        //Initialize Logic

        login_button = (Button) findViewById(R.id.signinbutton) ;
        forgot_password = (Button)findViewById(R.id.forgotpassword);
        sign_up=(Button) findViewById(R.id.signup);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        editTextEmail=(AutoCompleteTextView) findViewById(R.id.signinemail);
        editTextPassword=(EditText)findViewById(R.id.signinpassword);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!= null)
        {
            Intent intentSignInSuccessful=new Intent(MainActivity.this,AfterLogin.class);
            Toast.makeText(this, ""+firebaseUser.getUid(), Toast.LENGTH_SHORT).show();

            startActivity(intentSignInSuccessful);


        }


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

      /*  textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               Intent intentToForgotScreen=new Intent(MainActivity.this,ForgotPasswordActivity.class);
               startActivity(intentToForgotScreen);
            }
        });*/

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToForgotScreen=new Intent(MainActivity.this,ForgotPasswordActivity.class);
                startActivity(intentToForgotScreen);
            }
        }
        );



      /*  textViewSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Sign up logic goes here

                Intent intentToSignUp = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intentToSignUp);

            }
        });*/

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSignUp = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intentToSignUp);
            }
        }


        );

        /*floatingActionButtonSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                    email=editTextEmail.getText().toString();
                    password=editTextPassword.getText().toString();
                   int flagSignIn=validate(email,password);
                   if(flagSignIn==1)
                   {
                       signIn(email,password);
                   }
            }
        });*/

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                int flagSignIn = validate(email,password);
                if(flagSignIn == 1)
                    signIn(email,password);
            }
        });


    }
    int validate(String email,String password)
    {
        if(email.isEmpty() )
        {

            editTextEmail.setError("Email cannot be empty");
            return 0;
        }

        if (password.isEmpty())
        {
            Snackbar.make(linearLayout,"Password cannot be empty",Snackbar.LENGTH_SHORT).show();

            return 0;
        }
        return 1;
    }
    void signIn(String email,String password)
    {
        final SpotsDialog spotsDialog = new SpotsDialog(MainActivity.this, R.style.Login);
        spotsDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful())
                        {
                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()) {
                                Toast.makeText(MainActivity.this, "User Signed in",
                                        Toast.LENGTH_SHORT).show();
                                Intent intentToLoginSuccess =new Intent(MainActivity.this,AfterLogin.class);
                                startActivity(intentToLoginSuccess);
                                spotsDialog.dismiss();
                            }
                            else

                            {
                                Toast.makeText(MainActivity.this, "User not verified", Toast.LENGTH_SHORT).show();
                                spotsDialog.dismiss();
                            }
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            spotsDialog.dismiss();
                        }

                        // ...
                    }
                });
    }
}
