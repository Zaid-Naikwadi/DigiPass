package com.example.rohan.hello;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowTransactions extends AppCompatActivity {

    private String alltrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transactions);

        try{
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            final DatabaseReference childDatabaseReference1 = databaseReference.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Transaction");

            childDatabaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("transaction")){
                        alltrans = ""+dataSnapshot.child("transaction").getValue().toString();
                        Toast.makeText(ShowTransactions.this, "Fetched are :"+alltrans, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ShowTransactions.this, "No Transaction done yet!!Buy your first ticket", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show();
        }

    }
}
