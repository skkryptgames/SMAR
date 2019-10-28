package com.example.smar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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
        private ImageView moduleImage;
        private RadioButton radioButton;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater,ViewGroup container){

            super(inflater.inflate(R.layout.project_modules_layout,container,false));

            moduleImage=itemView.findViewById(R.id.smar_imageview_moduleimage);
            moduleName=itemView.findViewById(R.id.smar_textview_modulename);
            radioButton=itemView.findViewById(R.id.smar_radiobutton);
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
           holder.radioButton.setChecked(modulesPojo.isSelected());
           holder.startDate.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Calendar cal = Calendar.getInstance();
                   int year = cal.get(Calendar.YEAR);
                   int month = cal.get(Calendar.MONTH);
                   int day = cal.get(Calendar.DATE);

                   DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener, year, month, day);
                   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                   dialog.show();

                   mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                       @Override
                       public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                           String a=monthFinder(month);
                           String date = a + " " + dayOfMonth + " " +year;
                           holder.startDate.setText(date);
                       }
                   };
               }
           });
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   holder.radioButton.setChecked(modulesPojo.isSelected());
                   modulesPojo.setSelected(!modulesPojo.isSelected());
               }
           });

        }

        @Override
        public int getItemCount() {
            return detailsList.size();
        }
    }

    public void populateList(){
        ModulesPojo one=new ModulesPojo(R.drawable.harryw,"Design");
        ModulesPojo two=new ModulesPojo(R.drawable.harryw,"Final Budget");
        ModulesPojo three=new ModulesPojo(R.drawable.harryw,"Material Selection");
        ModulesPojo four=new ModulesPojo(R.drawable.harryw,"Civil Works");
        modulesList.add(one);
        modulesList.add(two);
        modulesList.add(three);
        modulesList.add(four);
    }
    private String monthFinder(int a){
        String month;
        String x[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        month=x[a];
        return month;
    }
}
