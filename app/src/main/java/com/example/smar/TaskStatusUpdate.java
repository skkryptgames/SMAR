package com.example.smar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class TaskStatusUpdate extends AppCompatActivity {
    Button button;
    TextView taskName,targetDate,photos,comments,numOfPhotos,numOfComments;
    ImageView taskImage,notStarted,inProgress,delayed,completed;
    String moduleName,date,title,taskId,uId,pId,image;
    int position,cProgress,a=R.drawable.ic_panorama_fish_eye_black_24dp;
    DatabaseReference reference;
    RelativeLayout not,in,del,comp,photosLayout,commentsLayout;
    ActionBar toolbar;
    TextView toolbarTitle;
    ImageView toolbarImage,signOut;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        button.setVisibility(View.VISIBLE);

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_status_update_layout);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
        getSupportActionBar().getCustomView();

        toolbar = getActionBar();
        toolbarTitle = findViewById(R.id.smar_toolbar_title);
        toolbarImage = findViewById(R.id.smar_toolbar_image);
        signOut=findViewById(R.id.smar_imageview_signout);
        signOut.setVisibility(View.GONE);
        this.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_background));


        title=getIntent().getStringExtra("title");
        toolbarTitle.setText(title);


        moduleName=getIntent().getStringExtra("moduleName");
        date=getIntent().getStringExtra("targetDate");
        position=getIntent().getIntExtra("position",0);
        image=getIntent().getStringExtra("image");
        taskId=getIntent().getStringExtra("taskId");
        pId=getIntent().getStringExtra("projectId");
        cProgress=getIntent().getIntExtra("progress",R.drawable.ic_panorama_fish_eye_black_24dp);
        uId=getIntent().getStringExtra("userId");


        not=findViewById(R.id.smar_layout_notstarted);
        in=findViewById(R.id.smar_layout_inprogress);
        del=findViewById(R.id.smar_layout_delayed);
        comp=findViewById(R.id.smar_layout_completed);
        taskImage=findViewById(R.id.smar_imageview_moduleimage);
        button=findViewById(R.id.smar_button_statusupdatedone);
        taskName=findViewById(R.id.smar_textview_modulename);
        targetDate=findViewById(R.id.smar_textview_targetdate);
        numOfPhotos=findViewById(R.id.smar_textview_numofphotos);
        numOfComments=findViewById(R.id.smar_textview_numofcomments);
        photos=findViewById(R.id.smar_textview_photos);
        comments=findViewById(R.id.smar_textview_comments);
        taskName.setText(moduleName);
        targetDate.setText(date);
        notStarted=findViewById(R.id.smar_imageview_notstarted);
        inProgress=findViewById(R.id.smar_imageview_inprogress);
        delayed=findViewById(R.id.smar_imageview_delayed);
        completed=findViewById(R.id.smar_imageview_completed);
        photosLayout=findViewById(R.id.smar_layout_photos);
        commentsLayout=findViewById(R.id.smar_layout_comments);

        reference= FirebaseDatabase.getInstance().getReference("users").child(uId).child("projects").child(pId).child("tasks").child(taskId);

        if(cProgress==R.drawable.ic_panorama_fish_eye_black_24dp){
            not.setAlpha(1f);
        }else if (cProgress==R.drawable.ic_ellipse_45){
            in.setAlpha(1f);
        }else if(cProgress==R.drawable.ic_ellipse_77){
            del.setAlpha(1f);
        }else if(cProgress==R.drawable.ic_checked){
            comp.setAlpha(1f);
        }

        Picasso.get().load(image).into(taskImage);

        not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=R.drawable.ic_panorama_fish_eye_black_24dp;

                not.setAlpha(1f);
                in.setAlpha(0.5f);
                del.setAlpha(0.5f);
                comp.setAlpha(0.5f);

            }
        });

        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=R.drawable.ic_ellipse_45;

                not.setAlpha(0.5f);
                in.setAlpha(1f);
                del.setAlpha(0.5f);
                comp.setAlpha(0.5f);
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=R.drawable.ic_ellipse_77;

                not.setAlpha(0.5f);
                in.setAlpha(0.5f);
                del.setAlpha(1f);
                comp.setAlpha(0.5f);
            }
        });
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=R.drawable.ic_checked;

                not.setAlpha(0.5f);
                in.setAlpha(0.5f);
                del.setAlpha(0.5f);
                comp.setAlpha(1f);
            }
        });


        reference.child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long i= dataSnapshot.getChildrenCount();
                numOfPhotos.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long i= dataSnapshot.getChildrenCount();
                numOfComments.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("progress",a);
                reference.updateChildren(map);

                TaskStatusUpdate.super.onBackPressed();
            }
        });




        photosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment=new PhotoDisplay();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(R.anim.r2l_slide_in, R.anim.r2l_slide_out, R.anim.l2r_slide_in, R.anim.l2r_slide_out);
                Bundle bundle=new Bundle();
                bundle.putString("projectId",pId);
                bundle.putString("taskId",taskId);
                bundle.putString("login","admin");
                bundle.putString("userId",uId);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,fragment,"photoDisplay");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                button.setVisibility(View.GONE);
            }
        });

        commentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new AdminMessageActivity();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(R.anim.r2l_slide_in, R.anim.r2l_slide_out, R.anim.l2r_slide_in, R.anim.l2r_slide_out);
                Bundle bundle=new Bundle();
                bundle.putString("projectId",pId);
                bundle.putString("taskId",taskId);
                bundle.putString("login","admin");
                bundle.putString("userId",uId);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,fragment,"AdminMessageActivity");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                button.setVisibility(View.GONE);
            }
        });


    }





}
