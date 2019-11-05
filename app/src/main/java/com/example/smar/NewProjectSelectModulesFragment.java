package com.example.smar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class NewProjectSelectModulesFragment extends Fragment {
    private ArrayList<ModulesPojo> modulesList=new ArrayList<>();
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button button;
    CalendarDialogPopup calendarDialogPopup;
    String pTitle,uid;
    Bundle bundle1;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button=view.findViewById(R.id.smar_button_modulesdone);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"new project has been created and added to the list",Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(getContext(),AdminPage.class);
                startActivity(intent);
                ((AdminPage)getActivity()).button.setVisibility(View.VISIBLE);
                ((AdminPage)getActivity()).mProjectListData.clear();
                ((AdminPage)getActivity()).addData(pTitle,"This Week","Nov 01 2019");
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i <= fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack();}


                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(uid).child("projects").child(bundle1.getString("projectTitle"));
                    DatabaseReference reference1=reference.child("tasks");
                    for(ModulesPojo model:modulesList){
                        if(model.isSelected()){
                            String key=reference1.push().getKey();
                            HashMap<String,Object> map=new HashMap<>();
                            map.put("taskId",key);
                            map.put("taskName",model.getName());
                            map.put("taskImage",model.getImage());
                            map.put("startDate",model.getStartDate());
                            map.put("numOfDays",model.getNumOfDays());
                            reference1.child(model.getName()).updateChildren(map);
                        }
                    }

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.select_modules_recyclerview,container,false);
        RecyclerView recyclerView=view.findViewById(R.id.smar_recyclerview_modules);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewAdapter(modulesList,getContext()));
        bundle1=getArguments();
        pTitle=bundle1.getString("projectTitle");
        ((AdminPage)getActivity()).toolbarTitle.setText(pTitle);


        populateList();
        return view;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView moduleName,startDate,numOfDays;
        private ImageView moduleImage,radioButtonImage;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater,ViewGroup container){

            super(inflater.inflate(R.layout.project_modules_layout,container,false));

            moduleImage=itemView.findViewById(R.id.smar_imageview_moduleimage);
            moduleName=itemView.findViewById(R.id.smar_textview_modulename);
            radioButtonImage=itemView.findViewById(R.id.smar_radiobutton);
            startDate=itemView.findViewById(R.id.smar_textview_startdate);
            numOfDays=itemView.findViewById(R.id.smar_edittext_numofdays);

        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

        private ArrayList<ModulesPojo> detailsList;
        Context context;
        public RecyclerViewAdapter(ArrayList<ModulesPojo>detailsList, Context context){
            this.detailsList=detailsList;
            this.context=context;
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new RecyclerViewHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
            final ModulesPojo modulesPojo = detailsList.get(position);
            holder.moduleName.setText(detailsList.get(position).getName());
            holder.moduleImage.setImageResource(detailsList.get(position).getImage());
            holder.radioButtonImage.setForeground(modulesPojo.isSelected() ? getResources().getDrawable(R.drawable.ic_ellipse_45) : getResources().getDrawable(R.drawable.ic_panorama_fish_eye_black_24dp));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modulesPojo.setSelected(!modulesPojo.isSelected());
                    holder.radioButtonImage.setForeground(modulesPojo.isSelected() ? getResources().getDrawable(R.drawable.ic_ellipse_45) : getResources().getDrawable(R.drawable.ic_panorama_fish_eye_black_24dp));

                }
            });

            holder.startDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calendarDialogPopup=new CalendarDialogPopup();
                    calendarDialogPopup.showNow(getFragmentManager(),"example");


                    calendarDialogPopup.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                            String month = monthFinder(i1);
                            holder.startDate.setText(month + " " + i2 + " " + i);
                            modulesList.get(position).setStartDate(month + " " + i2 + " " + i);
                            calendarDialogPopup.dismiss();

                        }
                    });

                }


            });
            holder.numOfDays.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    modulesList.get(position).setNumOfDays(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });




        }

        @Override
        public int getItemCount() {
            return detailsList.size();
        }
    }

    public void populateList(){
        ModulesPojo one=new ModulesPojo(R.drawable.ic_021_house_plan,"Arm Chair","MMM DD YYYY","x");
        ModulesPojo two=new ModulesPojo(R.drawable.ic_018_paint,"Curtains","MMM DD YYYY","x");
        ModulesPojo three=new ModulesPojo(R.drawable.ic_020_floor,"Floor","MMM DD YYYY","x");
        ModulesPojo four=new ModulesPojo(R.drawable.ic_023_tools,"Home Cinema","MMM DD YYYY","x");
        ModulesPojo five=new ModulesPojo(R.drawable.ic_033_ceiling,"Stairs","MMM DD YYYY","x");
        modulesList.add(one);
        modulesList.add(two);
        modulesList.add(three);
        modulesList.add(four);
        modulesList.add(five);
    }
    private String monthFinder(int a){
        String month;
        String x[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        month=x[a];
        return month;
    }
}
