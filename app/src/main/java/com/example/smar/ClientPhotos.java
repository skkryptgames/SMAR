package com.example.smar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ClientPhotos extends AppCompatActivity {
    Button button;
    ActionBar toolbar;
    TextView toolbarTitle;
    ImageView toolbarImage,signOut;
    String userId,pId,taskId;
    private StorageReference mStorageRef;
    FirebaseUser fbUser;
    DatabaseReference database;
    RecyclerView photos;
    RecyclerView.LayoutManager mLayoutManager;
    PhotoAdapter mAdapter;
    ArrayList<AddPhotos> pictures = new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        signOut.setVisibility(View.VISIBLE);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        button = (Button) findViewById(R.id.button);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
        getSupportActionBar().getCustomView();

        toolbar = getActionBar();
        toolbarTitle = findViewById(R.id.smar_toolbar_title);
        toolbarTitle.setText("smartnest");
        toolbarImage = findViewById(R.id.smar_toolbar_image);
        signOut=findViewById(R.id.smar_imageview_signout);
        this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_background));

        button.setVisibility(View.GONE);
        userId=getIntent().getStringExtra("adminUid");
        pId=getIntent().getStringExtra("projectId");
        taskId=getIntent().getStringExtra("taskId");
        signOut.setImageResource(R.drawable.ic_141_chat_1);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new AdminMessageActivity();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(R.anim.r2l_slide_in, R.anim.r2l_slide_out, R.anim.l2r_slide_in, R.anim.l2r_slide_out);
                Bundle bundle=new Bundle();
                bundle.putString("taskId",taskId);
                bundle.putString("projectId",pId);
                bundle.putString("adminUid",userId);
                bundle.putString("login","client");
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                signOut.setVisibility(View.GONE);
            }
        });


        fbUser = FirebaseAuth.getInstance().getCurrentUser();



        database = FirebaseDatabase.getInstance().getReference("users").child(userId).child("projects").child(pId).child("tasks").child(taskId);
        // Setup the RecyclerView
        photos =findViewById(R.id.photos);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        photos.setHasFixedSize(true);
        photos.setLayoutManager(mLayoutManager);
        mAdapter = new PhotoAdapter(ClientPhotos.this, pictures);
        photos.setAdapter(mAdapter);

        Query imagesQuery = database.child("images").orderByKey().limitToFirst(100);
        pictures.clear();
        imagesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                // A new image has been added, add it to the displayed list
                final  AddPhotos addPhotos = dataSnapshot.getValue(AddPhotos.class);
                // get the image DBUser
                database.child("users/" + addPhotos.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DBUser dbUser = dataSnapshot.getValue(DBUser.class);
                        addPhotos.dbUser = dbUser;
                        //images.add(image);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                pictures.add(addPhotos);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
