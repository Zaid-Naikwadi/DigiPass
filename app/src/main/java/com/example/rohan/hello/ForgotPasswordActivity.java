package com.example.rohan.hello;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private Button ButtonForgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        this.setTitle("Forgot Password");
        editTextEmail=(EditText)findViewById(R.id.forgotEmail1);
        ButtonForgotPassword=(Button)findViewById(R.id.forgotSubmit1);
        ButtonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextEmail.getText().toString().isEmpty() && editTextEmail.getText().toString().contains("@") && editTextEmail.getText().toString().length()>4)
                {
                    sendLink(editTextEmail.getText().toString());
                    Intent intentToMainActivity=new Intent(ForgotPasswordActivity.this,MainActivity.class);
                    startActivity(intentToMainActivity);
                }
                else
                {
                    editTextEmail.setError("Enter valid email-id");
                }
            }
        });
    }
    void sendLink(String email)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final SpotsDialog spotsDialog = new SpotsDialog(ForgotPasswordActivity.this, R.style.Custom1);
        spotsDialog.show();

        try {


            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Reset link sent", Toast.LENGTH_LONG).show();
                                Intent intentToMain=new Intent(ForgotPasswordActivity.this,MainActivity.class);
                                startActivity(intentToMain);
                                spotsDialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(ForgotPasswordActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                spotsDialog.dismiss();
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(ForgotPasswordActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            spotsDialog.dismiss();
        }
    }
}
