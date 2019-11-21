package com.example.smar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckSignInStatus extends AppCompatActivity {
    String uid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

       try{ uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
           DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users").child(uid);
           databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if(dataSnapshot.exists()){
                       DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(uid).child("info");
                       reference.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               if(dataSnapshot.child("userType").getValue(String.class).equals("admin")) {

                                   if (dataSnapshot.child("signInStatus").getValue(String.class).equals("signedIn")) {
                                       Intent intent = new Intent(getApplicationContext(), AdminPage.class);
                                       startActivity(intent);
                                       finish();
                                   } else {
                                       Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                                       startActivity(intent);
                                       finish();
                                   }
                               }
                               else {
                                   if (dataSnapshot.child("signInStatus").getValue(String.class).equals("signedIn")) {
                                       Intent intent = new Intent(getApplicationContext(), ClientTasks.class);
                                       startActivity(intent);
                                       finish();
                                   } else {
                                       Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                                       startActivity(intent);
                                       finish();
                                   }

                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });

                   }else {
                       Intent intent=new Intent(getApplicationContext(),AuthenticationActivity.class);
                       startActivity(intent);
                       finish();
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }
       catch (NullPointerException e){
           Intent intent=new Intent(getApplicationContext(),AuthenticationActivity.class);
           startActivity(intent);
           finish();
       }



    }
}
