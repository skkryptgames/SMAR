package com.example.smar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminPage extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ProjectList> mProjectListData = new ArrayList<>();
    ArrayList<String> thisWeekTasks=new ArrayList<>();
    ProjectListAdapter adapter;
    Button button;
    Toolbar toolBar;
    ActionBar toolbar;
    TextView toolbarTitle;
    ImageView toolbarImage;
    String uid,b;
    int a=0;

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {

            button.setVisibility(View.VISIBLE);
            finish();

        } else {
            super.onBackPressed();}


      /* if(a==0){
           finish();
           System.exit(0);
       }else {
           super.onBackPressed();
       }*/

    }


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);



        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
        getSupportActionBar().getCustomView();

        toolbar=getActionBar();
        toolbarTitle=findViewById(R.id.smar_toolbar_title);
        toolbarImage=findViewById(R.id.smar_toolbar_image);
        this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_background));



        /*toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toolBar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);*/


        button= findViewById(R.id.smar_button_addproject);
        button.setVisibility(View.VISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CreateNewProjectFragment()).addToBackStack(null).commit();

            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(AdminPage.this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);


        recyclerView.addItemDecoration(new DividerItemDecoration(AdminPage.this,
                DividerItemDecoration.VERTICAL));

       /* ProjectList projectList = new ProjectList("Vihanga 1805", "This Week", "0ct 30 2019");
        ProjectList projectList1 = new ProjectList("Abhra 1504", "This Week", "Nov 7 2019");
        ProjectList projectList2 = new ProjectList("Atria E-1208", "This Week", "Dec 11 2019");
        ProjectList projectList3 = new ProjectList("Lodha A709", "This Week", "Dec 25 2019");

        mProjectListData.add(projectList);
        mProjectListData.add(projectList1);
        mProjectListData.add(projectList2);
        mProjectListData.add(projectList3);*/

       uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
       final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(uid).child("projects");
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               mProjectListData.clear();


               for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                  final String pName= dataSnapshot1.child("projectName").getValue(String.class);
                 final String endDate=dataSnapshot1.child("endDate").getValue(String.class);
                  final String pId=dataSnapshot1.child("projectId").getValue(String.class);
                  final int progress=dataSnapshot1.child("progress").getValue(Integer.class);
                  String tasks=dataSnapshot1.child("thisWeekTasks").getValue(String.class);

                  mProjectListData.add(new ProjectList(pName,tasks,endDate,pId,progress));
                  adapter.notifyDataSetChanged();

               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

        adapter = new ProjectListAdapter(AdminPage.this, mProjectListData);
        recyclerView.setAdapter(adapter);




    }

    public void addTasksToArrayList(){
        b="";
        for (int i=0;i<thisWeekTasks.size();i++){
            b=b+thisWeekTasks.get(i)+",";
            adapter.notifyDataSetChanged();
        }

    }
}
