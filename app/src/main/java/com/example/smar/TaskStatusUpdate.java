package com.example.smar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TaskStatusUpdate extends Fragment {
    Button button;
    TextView taskName,targetDate,photos,comments;
    ImageView taskImage;
    String moduleName,date;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.task_status_update_layout,container,false);

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
                startActivity(intent);

            }
        });


    }
}
