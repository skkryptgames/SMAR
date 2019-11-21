package com.example.smar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class ClientPage extends AppCompatActivity {
    ActionBar toolbar;
    TextView toolbarTitle;
    ImageView toolbarImage,signOut;
    ArrayList<Client> clientData=new ArrayList<>();
    ClientAdapterNoClick clientAdapter;
    String adminUid,cProjectId,uId;
    RecyclerView clientRecyclerView;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_page);


        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
        getSupportActionBar().getCustomView();

        toolbar = getActionBar();
        toolbarTitle = findViewById(R.id.smar_toolbar_title);
        toolbarImage = findViewById(R.id.smar_toolbar_image);
        signOut=findViewById(R.id.smar_imageview_signout);
        this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_background));
        clientRecyclerView = findViewById(R.id.clientRecyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(ClientPage.this,
                LinearLayoutManager.VERTICAL, false);
        clientRecyclerView.setLayoutManager(mLinearLayoutManager);

        clientAdapter=new ClientAdapterNoClick(ClientPage.this,clientData);

        signOut.setImageResource(R.drawable.signout_demo);
        signOut.setVisibility(View.VISIBLE);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> a =new HashMap<>();
                a.put("signInStatus","signedOut");
                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("info").updateChildren(a);
                Intent intent=new Intent(getApplicationContext(),AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        });
        clientRecyclerView.setAdapter(clientAdapter);

        clientRecyclerView.addItemDecoration(new DividerItemDecoration(ClientPage.this,
                DividerItemDecoration.VERTICAL));



        toolbarImage.setVisibility(View.GONE);

        String number=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("clients").child(number);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    cProjectId=dataSnapshot.child("projectId").getValue(String.class);
                    adminUid=dataSnapshot.child("adminUid").getValue(String.class);
                    toolbarTitle.setText("smartnest");

                    DatabaseReference dareference= FirebaseDatabase.getInstance().getReference().child("users").child(adminUid).child("projects").child(cProjectId).child("tasks");
                    dareference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            clientData.clear();

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String tName = dataSnapshot1.child("taskName").getValue(String.class);
                                String date = dataSnapshot1.child("startDate").getValue(String.class);
                                int days = dataSnapshot1.child("numOfDays").getValue(Integer.class);
                                String image = dataSnapshot1.child("taskImage").getValue(String.class);
                                String taskId = dataSnapshot1.child("taskId").getValue(String.class);
                                int progress = dataSnapshot1.child("progress").getValue(Integer.class);
                                String endDate = dateFinder(date, days);
                                Client data = new Client(endDate,progress, tName, image, taskId);
                                clientData.add(data);
                                clientAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    FirebaseDatabase.getInstance().getReference("projects").child(cProjectId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            uId=dataSnapshot.child("userId").getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else {
                    Toast.makeText(getApplicationContext(),"Your project has not been added yet",Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private String dateFinder(String a,int days){
        Calendar f =new GregorianCalendar();
        Date date1=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("MMM dd yyyy");
        try {
            date1=sdf.parse(a);
            System.out.println(date1);
            f.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        f.add(Calendar.DAY_OF_MONTH,days);
        String newDate=sdf.format(f.getTime());

        return newDate;
    }
}
