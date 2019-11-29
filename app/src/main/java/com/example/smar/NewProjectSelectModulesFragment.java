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
import android.widget.EditText;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class NewProjectSelectModulesFragment extends Fragment {
    private ArrayList<ModulesPojo> modulesList=new ArrayList<>();
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button button;
    CalendarDialogPopup calendarDialogPopup;
    String pTitle,uid;
    Bundle bundle1;
    DatabaseReference reference3;
    RecyclerViewAdapter adapter;
    String projectKey;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button=view.findViewById(R.id.smar_button_modulesdone);
        uid= bundle1.getString("userId");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"new project has been created and added to the list",Toast.LENGTH_SHORT).show();


                Intent intent=new Intent(getContext(),AdminPage.class);
                startActivity(intent);
               // ((AdminPage)getActivity()).mProjectListData.clear();
               // ((AdminPage)getActivity()).addData(pTitle,"This Week","Nov 01 2019");
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i <= fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack();}


                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(uid).child("projects");

                    if(bundle1.getString("status").equals("create")){
                         projectKey=reference.push().getKey();
                        HashMap<String,Object> projectDetails=new HashMap<>();
                        projectDetails.put("clientName",bundle1.getString("clientName"));
                        projectDetails.put("clientNumber",bundle1.getString("clientNumber"));
                        projectDetails.put("endDate",bundle1.getString("projectEndDate"));
                        projectDetails.put("progress",R.drawable.ic_panorama_fish_eye_black_24dp);
                        projectDetails.put("projectId",projectKey);
                        projectDetails.put("projectName",bundle1.getString("projectTitle"));
                        projectDetails.put("startDate",bundle1.getString("projectStartDate"));
                        projectDetails.put("thisWeekTasks","");
                        projectDetails.put("userId",uid);
                        reference.child(projectKey).updateChildren(projectDetails);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("clients");
                        HashMap<String, Object> map1 = new HashMap<>();
                        map1.put("projectId", projectKey);
                        map1.put("adminUid", uid);
                        map1.put("projectName", bundle1.getString("projectTitle"));
                        databaseReference.child(bundle1.getString("clientNumber")).updateChildren(map1);

                        reference3=reference.child(projectKey).child("tasks");
                    for(ModulesPojo model:modulesList){
                        if(model.isSelected()){
                            String key=reference3.push().getKey();
                            HashMap<String,Object> map=new HashMap<>();
                            map.put("taskId",key);
                            map.put("taskName",model.getName());
                            map.put("taskImage",model.getImage());
                            map.put("startDate",model.getStartDate());
                            map.put("numOfDays",model.getNumOfDays());
                            map.put("progress",R.drawable.ic_panorama_fish_eye_black_24dp);
                            reference3.child(key).updateChildren(map);
                        }
                    }}if(bundle1.getString("status").equals("update")){
                        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("users").child(bundle1.getString("userId")).child("projects").child(bundle1.getString("projectKey"));
                        reference1.child("tasks").removeValue();
                    for(ModulesPojo model:modulesList){
                        if(model.isSelected()){
                            String key=reference1.push().getKey();
                            HashMap<String,Object> map=new HashMap<>();
                            map.put("taskId",key);
                            map.put("taskName",model.getName());
                            map.put("taskImage",model.getImage());
                            map.put("startDate",model.getStartDate());
                            map.put("numOfDays",model.getNumOfDays());
                            map.put("progress",R.drawable.ic_panorama_fish_eye_black_24dp);
                            reference1.child("tasks").child(key).updateChildren(map);
                        }
                    }


                }
                    tasksToBeDoneThisWeek();

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.select_modules_recyclerview,container,false);
        RecyclerView recyclerView=view.findViewById(R.id.smar_recyclerview_modules);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new RecyclerViewAdapter(modulesList,getContext());
        recyclerView.setAdapter(adapter);
        bundle1=getArguments();
        pTitle=bundle1.getString("projectTitle");

        if(bundle1.getString("status").equals("create"))
        ((AdminPage)getActivity()).toolbarTitle.setText(pTitle);
        if(bundle1.getString("status").equals("update"))
            ((AdminTasksPage) getActivity()).toolbarTitle.setText(pTitle);




        populateList();
        return view;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView moduleName,startDate;
        private ImageView moduleImage,radioButtonImage;
        private EditText numOfDays;


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
            Picasso.get().load(detailsList.get(position).getImage()).into(holder.moduleImage);
            //holder.moduleImage.setImageResource(detailsList.get(position).getImage());
            holder.radioButtonImage.setForeground(modulesPojo.isSelected() ? getResources().getDrawable(R.drawable.ic_ellipse_45) : getResources().getDrawable(R.drawable.ic_panorama_fish_eye_black_24dp));
            holder.numOfDays.setText(""+detailsList.get(position).getNumOfDays());
            holder.startDate.setText(detailsList.get(position).getStartDate());


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
                            if(i2>0&&i2<10){
                            holder.startDate.setText(month + " " +"0"+i2 + " " + i);
                            modulesList.get(position).setStartDate(month + " " +"0"+i2 + " " + i);
                            calendarDialogPopup.dismiss();}else {
                                holder.startDate.setText(month + " " + i2 + " " + i);
                                modulesList.get(position).setStartDate(month + " " + i2 + " " + i);
                                calendarDialogPopup.dismiss();
                            }

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

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.length()!=0)
                    modulesList.get(position).setNumOfDays( Integer.parseInt( holder.numOfDays.getText().toString() ));
                }
            });





        }

        @Override
        public int getItemCount() {
            return detailsList.size();
        }
    }

    public void populateList(){
        modulesList.clear();

        String pStartDate=bundle1.getString("projectStartDate");
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("tasks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String url=snapshot.child("downloadUrl").getValue(String.class);
                    String name=snapshot.child("name").getValue(String.class);
                    int delay=snapshot.child("startDelay").getValue(Integer.class);
                    int days=snapshot.child("defaultDays").getValue(Integer.class);
                    String date=dateFinder(pStartDate,delay);

                    modulesList.add(new ModulesPojo(url,name,date,days));
                    //Collections.sort(modulesList, (p1, p2) -> p1.getName().compareTo(p2.getName()));
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       /* ModulesPojo one=new ModulesPojo(R.drawable.ic_021_house_plan,"Arm Chair","MMM DD YYYY",0);
        ModulesPojo two=new ModulesPojo(R.drawable.ic_018_paint,"Curtains","MMM DD YYYY",0);
        ModulesPojo three=new ModulesPojo(R.drawable.ic_020_floor,"Floor","MMM DD YYYY",0);
        ModulesPojo four=new ModulesPojo(R.drawable.ic_023_tools,"Home Cinema","MMM DD YYYY",0);
        ModulesPojo five=new ModulesPojo(R.drawable.ic_033_ceiling,"Stairs","MMM DD YYYY",0);
        modulesList.add(one);
        modulesList.add(two);
        modulesList.add(three);
        modulesList.add(four);
        modulesList.add(five);*/
    }
    private String monthFinder(int a){
        String month;
        String x[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        month=x[a];
        return month;
    }

    public void tasksToBeDoneThisWeek(){
        String a="";
        if(bundle1.getString("status").equals("create")) {
            final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("projects").child(projectKey);

            Calendar calendar = Calendar.getInstance();
            reference1.child("tasks").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String b = "";
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String date = dataSnapshot1.child("startDate").getValue(String.class);
                        if (checkDateInThisWeek(date)) {

                            b = b + ", " + dataSnapshot1.child("taskName").getValue(String.class);

                        }
                    }
                    b = b.replaceFirst("(?:, )+", "");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("thisWeekTasks", b);
                    reference1.updateChildren(map);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if(bundle1.getString("status").equals("update")) {
            final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("projects").child(bundle1.getString("projectKey"));

            Calendar calendar = Calendar.getInstance();
            reference1.child("tasks").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String b = "";
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String date = dataSnapshot1.child("startDate").getValue(String.class);
                        if (checkDateInThisWeek(date)) {

                            b = b + ", " + dataSnapshot1.child("taskName").getValue(String.class);

                        }
                    }
                    b = b.replaceFirst("(?:, )+", "");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("thisWeekTasks", b);
                    reference1.updateChildren(map);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public boolean checkDateInThisWeek(String date){
        Date date2= new Date();
        try {
            date2 = new SimpleDateFormat("MMM dd yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SUNDAY);

        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date sunday = c.getTime();

        Date nextMonday= new Date(sunday.getTime()+7*24*60*60*1000);

        boolean isThisWeek = date2.after(sunday) && date2.before(nextMonday);

        return isThisWeek;
    }
    public Boolean allowBackPressed(){

        return true;

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
