package com.example.smar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.BatchUpdateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ArchievedProjectTasksPage extends Fragment {
    String title,pId;
    ArchievedProjectsTasksAdapter clientAdapter;
    ArrayList<Client> tasksData=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_client_page,container,false);
        Bundle bundle=getArguments();
        title=bundle.getString("title");
        pId=bundle.getString("projectId");
        ((ArchievedProjects)getActivity()).toolbarTitle.setText(title);
        RecyclerView recyclerView=view.findViewById(R.id.clientRecyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        clientAdapter=new ArchievedProjectsTasksAdapter(getContext(),tasksData);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(clientAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("archievedprojects").child(pId).child("tasks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tasksData.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String tName = dataSnapshot1.child("taskName").getValue(String.class);
                    String date = dataSnapshot1.child("startDate").getValue(String.class);
                    int days = dataSnapshot1.child("numOfDays").getValue(Integer.class);
                    String image = dataSnapshot1.child("taskImage").getValue(String.class);
                    String taskId = dataSnapshot1.child("taskId").getValue(String.class);
                    int progress = dataSnapshot1.child("progress").getValue(Integer.class);
                    String endDate = dateFinder(date, days);
                    Client data = new Client(endDate, progress, tName, image, taskId);
                    tasksData.add(data);
                    clientAdapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




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

}
