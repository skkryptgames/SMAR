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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TaskStatusUpdate extends Fragment {
    Button button;
    TextView taskName,targetDate,photos,comments;
    ImageView taskImage,notStarted,inProgress,delayed,completed;
    String moduleName,date,title,taskId,uId,pId;
    int position,image;
    DatabaseReference reference;

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
        image=bundle.getInt("image");
        taskId=bundle.getString("taskId");
        pId=bundle.getString("projectId");


        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button=view.findViewById(R.id.smar_button_statusupdatedone);
        taskName=view.findViewById(R.id.smar_textview_modulename);
        targetDate=view.findViewById(R.id.smar_textview_targetdate);
        photos=view.findViewById(R.id.smar_textview_numofphotos);
        comments=view.findViewById(R.id.smar_textview_numofcomments);
        taskName.setText(moduleName);
        targetDate.setText(date);
        notStarted=view.findViewById(R.id.smar_imageview_notstarted);
        inProgress=view.findViewById(R.id.smar_imageview_inprogress);
        delayed=view.findViewById(R.id.smar_imageview_delayed);
        completed=view.findViewById(R.id.smar_imageview_completed);
        reference= FirebaseDatabase.getInstance().getReference("users").child(uId).child("projects").child(pId).child("tasks").child(taskId);

        notStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("progress",R.drawable.ic_panorama_fish_eye_black_24dp);
                reference.updateChildren(map);

            }
        });

        inProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("progress",R.drawable.ic_ellipse_45);
                reference.updateChildren(map);
            }
        });

        delayed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("progress",R.drawable.ic_ellipse_77);
                reference.updateChildren(map);
            }
        });
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("progress",R.drawable.ic_checked);
                reference.updateChildren(map);
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getContext(),ClientPage.class);
                intent.putExtra("title",title);
                intent.putExtra("projectId",pId);
                startActivity(intent);



            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
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
