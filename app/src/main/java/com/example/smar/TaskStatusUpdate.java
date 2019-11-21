package com.example.smar;

import android.content.ClipData;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class TaskStatusUpdate extends Fragment {
    Button button;
    TextView taskName,targetDate,photos,comments,numOfPhotos,numOfComments;
    ImageView taskImage,notStarted,inProgress,delayed,completed;
    String moduleName,date,title,taskId,uId,pId,image;
    int position,cProgress,a=R.drawable.ic_panorama_fish_eye_black_24dp;
    DatabaseReference reference;
    RelativeLayout not,in,del,comp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.task_status_update_layout,container,false);

        uId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        title=((ClientPage)getActivity()).title;

        Bundle bundle=getArguments();
        moduleName=bundle.getString("moduleName");
        date=bundle.getString("targetDate");
        position=bundle.getInt("position");
        image=bundle.getString("image");
        taskId=bundle.getString("taskId");
        pId=bundle.getString("projectId");
        cProgress=bundle.getInt("progress");


        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        not=view.findViewById(R.id.smar_layout_notstarted);
        in=view.findViewById(R.id.smar_layout_inprogress);
        del=view.findViewById(R.id.smar_layout_delayed);
        comp=view.findViewById(R.id.smar_layout_completed);
        taskImage=view.findViewById(R.id.smar_imageview_moduleimage);
        button=view.findViewById(R.id.smar_button_statusupdatedone);
        taskName=view.findViewById(R.id.smar_textview_modulename);
        targetDate=view.findViewById(R.id.smar_textview_targetdate);
        numOfPhotos=view.findViewById(R.id.smar_textview_numofphotos);
        numOfComments=view.findViewById(R.id.smar_textview_numofcomments);
        photos=view.findViewById(R.id.smar_textview_photos);
        comments=view.findViewById(R.id.smar_textview_comments);
        taskName.setText(moduleName);
        targetDate.setText(date);
        notStarted=view.findViewById(R.id.smar_imageview_notstarted);
        inProgress=view.findViewById(R.id.smar_imageview_inprogress);
        delayed=view.findViewById(R.id.smar_imageview_delayed);
        completed=view.findViewById(R.id.smar_imageview_completed);
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



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("progress",a);
                reference.updateChildren(map);

                ((ClientPage)getActivity()).onBackPressed();
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });


        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment=new PhotoDisplay();
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(R.anim.r2l_slide_in, R.anim.r2l_slide_out, R.anim.l2r_slide_in, R.anim.l2r_slide_out);
                Bundle bundle=new Bundle();
                bundle.putString("projectId",pId);
                bundle.putString("taskId",taskId);
                bundle.putString("login","admin");
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,fragment,"photoDisplay");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new AdminMessageActivity();
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
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

    @Override
    public void onResume() {

        super.onResume();
        new TaskStatusUpdate();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if(getFragmentManager().getBackStackEntryCount() > 0) {
                        getFragmentManager().popBackStack();
                    }

                    return true;

                }

                return false;
            }
        });
    }
}
