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
import android.widget.SimpleExpandableListAdapter;
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
import java.util.GregorianCalendar;
import java.util.HashMap;


public class ClientPage extends AppCompatActivity {

    RecyclerView clientRecyclerView;
    ArrayList<Client> clientData = new ArrayList<>();
    ClientAdapter adapter;
    ActionBar toolbar;
    TextView toolbarTitle;
    ImageView toolbarImage;
    String title,uid;
    static String pId;
    DatabaseReference reference;
    int notStarted=0,inProgress=0,delayed=0,completed=0;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_page);

        //clientData=populateData();


        title = getIntent().getStringExtra("title");
        pId=getIntent().getStringExtra("projectId");
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

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
         reference= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("projects").child(pId).child("tasks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String tName=dataSnapshot1.child("taskName").getValue(String.class);
                    String date=dataSnapshot1.child("startDate").getValue(String.class);
                    int days=dataSnapshot1.child("numOfDays").getValue(Integer.class);
                    int image=dataSnapshot1.child("taskImage").getValue(Integer.class);
                    String taskId=dataSnapshot1.child("taskId").getValue(String.class);
                    int progress=dataSnapshot1.child("progress").getValue(Integer.class);
                    String endDate=dateFinder(date,days);
                    Client data=new Client(endDate,progress,tName,image,taskId);
                    clientData.add(data);
                    adapter.notifyDataSetChanged();

                }
                setProjectStatus();
                tasksToBeDoneThisWeek();

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
    public static String getProjectId(){
        String a=pId ;
    return a;
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


    public void setProjectStatus(){

        final DatabaseReference reference1= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("projects").child(pId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inProgress=delayed=completed=0;

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(dataSnapshot1.child("progress").getValue(Integer.class)==R.drawable.ic_ellipse_45){
                        inProgress++;
                    }else if(dataSnapshot1.child("progress").getValue(Integer.class)==R.drawable.ic_ellipse_77){
                        delayed++;
                    }else if(dataSnapshot1.child("progress").getValue(Integer.class)==R.drawable.ic_checked){
                        completed++;
                    }
                }
                if(completed==clientData.size()){
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("progress",R.drawable.ic_checked);
                    reference1.updateChildren(map);
                }else if(delayed>0){
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("progress",R.drawable.ic_ellipse_77);
                    reference1.updateChildren(map);
                } else if(inProgress>0){
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("progress",R.drawable.ic_ellipse_45);
                    reference1.updateChildren(map);
                }else{
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("progress",R.drawable.ic_panorama_fish_eye_black_24dp);
                    reference1.updateChildren(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void tasksToBeDoneThisWeek(){
        String a="";
        final DatabaseReference reference1= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("projects").child(pId);
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
