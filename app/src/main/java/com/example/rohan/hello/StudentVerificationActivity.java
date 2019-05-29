package com.example.rohan.hello;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

public class StudentVerificationActivity extends AppCompatActivity {
    private EditText editTextEmail,editTextPassword;
    private Button ButtonVerify;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "UserVerificationAct";


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
        setContentView(R.layout.activity_student_verification);
        this.setTitle("Student Verification Protal");
        editTextEmail=(EditText)findViewById(R.id.verifyemail);
        editTextPassword=(EditText)findViewById(R.id.verifypassword);
        ButtonVerify=(Button)findViewById(R.id.verifybutton);
        mAuth = FirebaseAuth.getInstance();
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
        ButtonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int flag=validate(editTextEmail.getText().toString(),editTextPassword.getText().toString());
                if(flag==1)
                {
                    final SpotsDialog spotsDialog = new SpotsDialog(StudentVerificationActivity.this, R.style.Login);
                    spotsDialog.show();
                    mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(),editTextPassword.getText().toString())
                            .addOnCompleteListener(StudentVerificationActivity.this, new OnCompleteListener<AuthResult>() {
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
                                            Toast.makeText(StudentVerificationActivity.this, "User Already verified",
                                                    Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();
                                            spotsDialog.dismiss();
                                        }
                                        else

                                        {


                                            user.sendEmailVerification();
                                            FirebaseAuth.getInstance().signOut();
                                            Toast.makeText(StudentVerificationActivity.this, "Verification link has been sent.", Toast.LENGTH_SHORT).show();
                                            spotsDialog.dismiss();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(StudentVerificationActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                        spotsDialog.dismiss();
                                    }

                                    // ...
                                }
                            });
                }
            }
        });

    }
    int validate(String email,String password)
    {
        int f=1;
        if (email.isEmpty())
        {
            editTextEmail.setError("Email cannot be empty");
            f=0;
        }
        if(!email.contains("@"))
        {
            editTextEmail.setError("Enter valid email");
            f=0;
        }
        if(password.isEmpty())
        {
            editTextPassword.setError("Password cannot be empty");
            f=0;
        }
        return f;
    }
}
