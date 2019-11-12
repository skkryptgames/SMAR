package com.example.smar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class NewProjectStartDateFragment extends Fragment {
    private CalendarView calendarView;
    private Button button;
    Bundle bundle1;
    String startDate,uid;
    Bundle bundle=new Bundle();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((AdminPage)getActivity()).toolbarTitle.setText("Project Start Date");

         bundle1=getArguments();
        return inflater.inflate(R.layout.project_start_date_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarView=view.findViewById(R.id.smar_calenderview_calender);
        button=view.findViewById(R.id.smar_button_startdatenext);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        Calendar calendar=Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        startDate=monthFinder(thisMonth)+" "+thisDay+" "+thisYear;

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                String month = monthFinder(i1);
                if(i2>0&&i2<10)
                    startDate=month+" "+"0"+i2+" "+i;
                else
                    startDate=month+" "+i2+" "+i;

                bundle.putInt("year",i);
                bundle.putInt("month",i1);
                bundle.putInt("day",i2);



            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(uid).child("projects").child(bundle1.getString("projectKey"));
                HashMap<String,Object> map=new HashMap<>();
                map.put("startDate",startDate);
                reference.updateChildren(map);


                Fragment fragment=new NewProjectEndDateFragment();
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(R.anim.r2l_slide_in, R.anim.r2l_slide_out, R.anim.l2r_slide_in, R.anim.l2r_slide_out);

                bundle.putString("projectTitle",bundle1.getString("projectTitle"));
                bundle.putString("projectKey",bundle1.getString("projectKey"));
                bundle.putString("projectStartDate",startDate);

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
