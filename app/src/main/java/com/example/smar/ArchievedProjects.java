package com.example.smar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ArchievedProjects extends AppCompatActivity {
    LinearLayout button;
    ArrayList<ProjectList> archivedProjects=new ArrayList<>();
    ArchievedProjectListAdapter adapter;
    ActionBar toolbar;
    ImageView toolbarImage;
    TextView toolbarTitle;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toolbarTitle.setText("Archieved Projects");
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_page);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
        getSupportActionBar().getCustomView();

        toolbar=getActionBar();
        toolbarTitle=findViewById(R.id.smar_toolbar_title);
        toolbarTitle.setText("Archieved Projects");
        toolbarImage=findViewById(R.id.smar_toolbar_image);
        this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_background));
        toolbarImage.setImageResource(R.drawable.ic_home);


        toolbarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AdminPage.class);
                startActivity(intent);
                finish();

            }
        });
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        button=findViewById(R.id.smar_layout_buttonlinear);
        button.setVisibility(View.GONE);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("archievedprojects").child(uid);
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("users").child(uid).child("projects");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    final String pName= dataSnapshot1.child("projectName").getValue(String.class);
                    final String endDate=dataSnapshot1.child("endDate").getValue(String.class);
                    final String pId=dataSnapshot1.child("projectId").getValue(String.class);
                    final int progress=dataSnapshot1.child("progress").getValue(Integer.class);
                    String tasks=dataSnapshot1.child("thisWeekTasks").getValue(String.class);

                    archivedProjects.add(new ProjectList(pName,tasks,endDate,pId,progress));
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new ArchievedProjectListAdapter(ArchievedProjects.this,archivedProjects);

        recyclerView.setAdapter(adapter);


    }


}
