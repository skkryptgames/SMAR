package com.example.smar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class NewProjectEndDateFragment extends Fragment {

    private CalendarView calendarView;
    private Button button;
    Bundle bundle1;
    String uid,endDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bundle1=getArguments();
        if(bundle1.getString("status").equals("create"))
            ((AdminPage)getActivity()).toolbarTitle.setText("Project End Date");
        if(bundle1.getString("status").equals("update"))
            ((AdminTasksPage)getActivity()).toolbarTitle.setText("Project End Date");
        return inflater.inflate(R.layout.project_end_date_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarView=view.findViewById(R.id.smar_calenderview_calender);
        button=view.findViewById(R.id.smar_button_enddatenext);
        uid= bundle1.getString("userId");

        Calendar f =new GregorianCalendar();
        Date date1=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("MMM dd yyyy");
        try {
            date1=sdf.parse(bundle1.getString("projectStartDate"));
            System.out.println(date1);
            f.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        f.add(Calendar.DAY_OF_MONTH,124);
        calendarView.setDate(f.getTimeInMillis());
        endDate=sdf.format(f.getTime());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                if(i<bundle1.getInt("year") || i==bundle1.getInt("year")&&i1< bundle1.getInt("month") || i==bundle1.getInt("year")&&i1==bundle1.getInt("month")&&i2<bundle1.getInt("day")) {
                    Toast.makeText(getContext(), "Project end date cannot be a lower value than project start date", Toast.LENGTH_SHORT).show();
                    button.setEnabled(false);
                }else {
                    button.setEnabled(true);
                    String month = monthFinder(i1);
                    if (i2 > 0 && i2 < 10)
                        endDate = month + " " + "0" + i2 + " " + i;
                    else
                        endDate = month + " " + i2 + " " + i;

                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bundle1.getString("status").equals("update")) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("projects").child(bundle1.getString("projectKey"));
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("endDate", endDate);
                    reference.updateChildren(map);

                    Intent intent=new Intent(getContext(),AdminPage.class);
                    startActivity(intent);
                    // ((AdminPage)getActivity()).mProjectListData.clear();
                    // ((AdminPage)getActivity()).addData(pTitle,"This Week","Nov 01 2019");
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    for (int i = 0; i <= fm.getBackStackEntryCount(); i++) {
                        fm.popBackStack();}
                }

                Fragment fragment=new NewProjectSelectModulesFragment();
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(R.anim.r2l_slide_in, R.anim.r2l_slide_out, R.anim.l2r_slide_in, R.anim.l2r_slide_out);
                Bundle bundle=new Bundle();
                bundle.putString("projectTitle",bundle1.getString("projectTitle"));
                bundle.putString("projectKey",bundle1.getString("projectKey"));
                bundle.putString("projectStartDate",bundle1.getString("projectStartDate"));
                bundle.putString("status",bundle1.getString("status"));
                bundle.putString("userId",bundle1.getString("userId"));
                bundle.putString("clientName",bundle1.getString("clientName"));
                bundle.putString("clientNumber",bundle1.getString("clientNumber"));
                bundle.putString("projectEndDate",endDate);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    private String monthFinder(int a){
        String month;
        String x[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        month=x[a];
        return month;
    }
}
