package com.example.rohan.hello;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PastTransactionActivity extends AppCompatActivity {
        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        String alltrans;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_past_transaction);

            Toolbar toolbar = findViewById(R.id.toolbar_recycler_view);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            mRecyclerView = (RecyclerView) findViewById(R.id.transaction_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            try{
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                final DatabaseReference childDatabaseReference1 = databaseReference.child("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Transaction");

                childDatabaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("transaction")){
                             alltrans = ""+dataSnapshot.child("transaction").getValue().toString();
                            Toast.makeText(PastTransactionActivity.this, "Fetched are :"+alltrans, Toast.LENGTH_SHORT).show();
                            String array1[] = alltrans.split("#");
                            for(String temp:array1){
                                Toast.makeText(PastTransactionActivity.this, temp, Toast.LENGTH_SHORT).show();
                            }
                            mAdapter = new PastTransactionAdapter(array1);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                        else
                        {
                            Toast.makeText(PastTransactionActivity.this, "No Transaction done yet!!Buy your first ticket", Toast.LENGTH_SHORT).show();
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

           // String test = "this#is#a#test";
            String myDataset[]=null;

        }
        // ...//
 }


