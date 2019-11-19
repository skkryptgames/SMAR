package com.example.smar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static com.example.smar.PhotoDisplay.RC_IMAGE_GALLERY;


public class ClientPage extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    RecyclerView clientRecyclerView;
    ArrayList<Client> clientData = new ArrayList<>();
    ClientAdapter adapter;
    ClientAdapterNoClick clientAdapter;
    ActionBar toolbar;
    FirebaseUser fbUser;
    TextView toolbarTitle;
    ImageView toolbarImage,signOut;
    String title,uid;
    static String pId;
    DatabaseReference reference;
    int notStarted=0,inProgress=0,delayed=0,completed=0;
    String cProjectId,adminUid;
    RelativeLayout relativeLayout;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbUser=FirebaseAuth.getInstance().getCurrentUser();
        if (requestCode == RC_IMAGE_GALLERY && resultCode == RESULT_OK) {
            if(data.getClipData()!=null){
                int count=data.getClipData().getItemCount();
                for(int i=0;i<count;i++){
                    Uri uri=data.getClipData().getItemAt(i).getUri();
                    //Uri uri = data.getData();
                    final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    final StorageReference imagesRef = FirebaseStorage.getInstance().getReference().child("images");
                    StorageReference userRef = imagesRef.child(fbUser.getUid());
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String filename = fbUser.getUid() + "_" + timeStamp;
                    StorageReference fileRef = userRef.child(filename);

                    UploadTask uploadTask = fileRef.putFile(uri);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(getApplicationContext(), "Upload failed!\n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            PhotoDisplay a=(PhotoDisplay)getSupportFragmentManager().findFragmentByTag("photoDisplay");
                            String tId=a.taskId;
                            final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(fbUser.getUid()).child("projects").child(pId).child("tasks").child(tId);


                            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    String key = database.child("images").push().getKey();
                                    AddPhotos addPhotos = new AddPhotos(key, fbUser.getUid(), downloadUrl);
                                    database.child("images").child(key).setValue(addPhotos);
                                    Log.i("Upload Finished",downloadUrl );
                                }
                            });


                        }
                    });
                }
            }

        }
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_page);

        relativeLayout=findViewById(R.id.fragment_container);
        title = getIntent().getStringExtra("title");
        pId = getIntent().getStringExtra("projectId");
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

        adapter = new ClientAdapter(ClientPage.this, clientData);
        clientAdapter=new ClientAdapterNoClick(ClientPage.this,clientData);



        if(title==null){
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
                        toolbarTitle.setText(dataSnapshot.child("projectName").getValue(String.class));

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
                                    Client data = new Client(endDate, progress, tName, image, taskId);
                                    clientData.add(data);
                                    clientAdapter.notifyDataSetChanged();
                                }
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

        //clientData=populateData();

else {
            clientRecyclerView.setAdapter(adapter);

            clientRecyclerView.addItemDecoration(new DividerItemDecoration(ClientPage.this,
                    DividerItemDecoration.VERTICAL));

            toolbarImage.setVisibility(View.VISIBLE);
            toolbarImage.setImageResource(R.drawable.ic_home);
            toolbarTitle.setText(title);


            toolbarImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent homeIntent = new Intent(getApplicationContext(), AdminPage.class);
                    startActivity(homeIntent);
                }
            });




            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("projects").child(pId).child("tasks");
            reference.addValueEventListener(new ValueEventListener() {
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
                        Client data = new Client(endDate, progress, tName, image, taskId);
                        clientData.add(data);
                        adapter.notifyDataSetChanged();

                    }
                    setProjectStatus();
                    //tasksToBeDoneThisWeek();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }
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
                }else if(completed>0&&completed<clientData.size()&&delayed==0){
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("progress",R.drawable.ic_ellipse_45);
                    reference1.updateChildren(map);
                }
                else if(delayed>0){
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
