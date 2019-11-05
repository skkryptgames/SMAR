package com.example.smar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.smar.AdminPage.populateData;

public class ClientPage extends AppCompatActivity {

    RecyclerView clientRecyclerView;
    ArrayList<Client> clientData = new ArrayList<>();
    ClientAdapter adapter;
    ActionBar toolbar;
    TextView toolbarTitle;
    ImageView toolbarImage;
    String title;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_page);

        clientData=populateData();


        title = getIntent().getStringExtra("title");
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
        getSupportActionBar().getCustomView();

        toolbar=getActionBar();
        toolbarTitle=findViewById(R.id.smar_toolbar_title);
        toolbarImage=findViewById(R.id.smar_toolbar_image);
        toolbarTitle.setText(title);
        this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_background));


        clientRecyclerView = findViewById(R.id.clientRecyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(ClientPage.this,
                LinearLayoutManager.VERTICAL, false);
        clientRecyclerView.setLayoutManager(mLinearLayoutManager);

        clientRecyclerView.setVisibility(View.VISIBLE);

        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("projects").child(title).child("tasks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String tName=dataSnapshot1.child("taskName").getValue(String.class);
                    String date=dataSnapshot1.child("startDate").getValue(String.class);
                    String days=dataSnapshot1.child("numOfDays").getValue(String.class);
                    int image=dataSnapshot1.child("taskImage").getValue(Integer.class);
                    Client data=new Client(date,R.drawable.ic_panorama_fish_eye_black_24dp,tName,image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter=new ClientAdapter(ClientPage.this,clientData);
        clientRecyclerView.setAdapter(adapter);

        clientRecyclerView.addItemDecoration(new DividerItemDecoration(ClientPage.this,
                DividerItemDecoration.VERTICAL));



    }

}
