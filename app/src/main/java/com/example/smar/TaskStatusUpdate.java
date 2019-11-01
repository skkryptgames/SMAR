package com.example.smar;

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

public class TaskStatusUpdate extends Fragment {
    Button button;
    TextView taskName,targetDate,photos,comments;
    ImageView taskImage;
    String moduleName,date,title;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.task_status_update_layout,container,false);

        //((ClientPage)getActivity()).clientRecyclerView.set;
        title=((ClientPage)getActivity()).title;

        Bundle bundle=getArguments();
        moduleName=bundle.getString("moduleName");
        date=bundle.getString("targetDate");

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getContext(),ClientPage.class);
                intent.putExtra("title",title);
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
