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
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AdminPage extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ProjectList> mProjectListData = new ArrayList<>();
    ArrayList<String> thisWeekTasks=new ArrayList<>();
    ProjectListAdapter adapter;
    Button button;
    Toolbar toolBar;
    ActionBar toolbar;
    TextView toolbarTitle;
    ImageView toolbarImage,signOut;
    String uid,b;
    int a=0;

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {

            finish();

        } else {
            super.onBackPressed();
        }


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
        signOut=findViewById(R.id.smar_imageview_signout);
        signOut.setVisibility(View.VISIBLE);
        this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_background));

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



        /*toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toolBar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);*/


        button= findViewById(R.id.smar_button_addproject);

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
                  tasksToBeDoneThisWeek(pId);
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
    public void tasksToBeDoneThisWeek(String a){
        final DatabaseReference reference1= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("projects").child(a);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("projects").child(a).child("tasks");
        Calendar calendar=Calendar.getInstance();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String b="";
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String date=dataSnapshot1.child("startDate").getValue(String.class);
                    if(checkDateInThisWeek(date)){

                        b=b+", "+dataSnapshot1.child("taskName").getValue(String.class);

                    }
                }
                b=b.replaceFirst("(?:, )+","");
                HashMap<String,Object> map=new HashMap<>();
                map.put("thisWeekTasks",b);
                reference1.updateChildren(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public boolean checkDateInThisWeek(String date){
        Date date2= new Date();
        try {
            date2 = new SimpleDateFormat("MMM dd yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date monday = c.getTime();

        Date nextMonday= new Date(monday.getTime()+7*24*60*60*1000);

        boolean isThisWeek = date2.after(monday) && date2.before(nextMonday);

        return isThisWeek;
    }
}
