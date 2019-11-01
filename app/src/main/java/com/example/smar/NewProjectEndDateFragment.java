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

public class NewProjectEndDateFragment extends Fragment {

    private CalendarView calendarView;
    private Button button;
    Bundle bundle1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AdminPage)getActivity()).toolbarTitle.setText("Project End Date");
        bundle1=getArguments();
        return inflater.inflate(R.layout.project_end_date_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarView=view.findViewById(R.id.smar_calenderview_calender);
        button=view.findViewById(R.id.smar_button_enddatenext);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new NewProjectSelectModulesFragment();
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(R.anim.r2l_slide_in, R.anim.r2l_slide_out, R.anim.l2r_slide_in, R.anim.l2r_slide_out);
                Bundle bundle=new Bundle();
                bundle.putString("projectTitle",bundle1.getString("projectTitle"));
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }
}
