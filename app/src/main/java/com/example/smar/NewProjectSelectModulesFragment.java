package com.example.smar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class NewProjectSelectModulesFragment extends Fragment {
    private ArrayList<ModulesPojo> modulesList=new ArrayList<>();
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button button;
    CalendarDialogPopup calendarDialogPopup;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button=view.findViewById(R.id.smar_button_modulesdone);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"new project has been created and added to the list",Toast.LENGTH_SHORT).show();
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
            numOfDays=itemView.findViewById(R.id.smar_textview_numofdays);

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
        public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
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
                            calendarDialogPopup.dismiss();

                        }
                    });

                }


            });




        }

        @Override
        public int getItemCount() {
            return detailsList.size();
        }
    }

    public void populateList(){
        ModulesPojo one=new ModulesPojo(R.drawable.ic_021_house_plan,"Arm Chair");
        ModulesPojo two=new ModulesPojo(R.drawable.ic_018_paint,"Curtains");
        ModulesPojo three=new ModulesPojo(R.drawable.ic_020_floor,"Floor");
        ModulesPojo four=new ModulesPojo(R.drawable.ic_023_tools,"Home Cinema");
        ModulesPojo five=new ModulesPojo(R.drawable.ic_033_ceiling,"Stairs");
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
