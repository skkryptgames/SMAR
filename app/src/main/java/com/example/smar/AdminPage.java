package com.example.smar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminPage extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ProjectList> mProjectListData = new ArrayList<>();
    ProjectListAdapter adapter;
    Button button;
    Toolbar toolBar;
    ActionBar toolbar;
    TextView toolbarTitle;
    ImageView toolbarImage;




    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);



        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
        getSupportActionBar().getCustomView();

        toolbar=getActionBar();
        toolbarTitle=findViewById(R.id.smar_toolbar_title);
        toolbarImage=findViewById(R.id.smar_toolbar_image);
        toolbarImage.setVisibility(View.GONE);



        /*toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toolBar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);*/


        button= findViewById(R.id.smar_button_addproject);
        button.setVisibility(View.VISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CreateNewProjectFragment()).commit();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(AdminPage.this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);


        recyclerView.addItemDecoration(new DividerItemDecoration(AdminPage.this,
                DividerItemDecoration.VERTICAL));

        ProjectList projectList = new ProjectList("Vihanga 1805", "This Week", "0ct 30 2019");
        ProjectList projectList1 = new ProjectList("Abhra 1504", "This Week", "Nov 7 2019");
        ProjectList projectList2 = new ProjectList("Atria E-1208", "This Week", "Dec 11 2019");
        ProjectList projectList3 = new ProjectList("Lodha A709", "This Week", "Dec 25 2019");

        mProjectListData.add(projectList);
        mProjectListData.add(projectList1);
        mProjectListData.add(projectList2);
        mProjectListData.add(projectList3);

        adapter = new ProjectListAdapter(AdminPage.this, mProjectListData);
        recyclerView.setAdapter(adapter);
    }
}
