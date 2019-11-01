package com.example.smar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ClientPage extends AppCompatActivity {

    RecyclerView clientRecyclerView;
    ArrayList<Client> clientData = new ArrayList<>();
    ClientAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_page);

        String a = getIntent().getStringExtra("title");


        clientRecyclerView = findViewById(R.id.clientRecyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(ClientPage.this,
                LinearLayoutManager.VERTICAL, false);
        clientRecyclerView.setLayoutManager(mLinearLayoutManager);


        clientRecyclerView.addItemDecoration(new DividerItemDecoration(ClientPage.this,
                DividerItemDecoration.VERTICAL));

        Client client = new Client("Target Date Dec11 2019",  R.drawable.ic_checked, "Design", R.drawable.ic_023_tools);
        Client client1 = new Client("Target Date Dec11 2019", R.drawable.ic_panorama_fish_eye_black_24dp, "Financial Budget", R.drawable.ic_021_house_plan);
        Client client2 = new Client("Target Date Dec11 2019", R.drawable.ic_ellipse_45 , "Material Selection", R.drawable.ic_018_paint);
        Client client3 = new Client("Target Date Dec7 2019", R.drawable.ic_panorama_fish_eye_black_24dp, "Civil Work", R.drawable.ic_020_floor);
        Client client4= new Client("Target Date Dec1 2019",  R.drawable.ic_panorama_fish_eye_black_24dp, "Site Prep", R.drawable.ic_022_house_plan_1);
        Client client5 = new Client("Targent Date Dec3 2019", R.drawable.ic_ellipse_77, "Flase Ceiling Channels", R.drawable.ic_033_ceiling);
        Client client6 = new Client("Target Date Dec7 2019",R.drawable.ic_panorama_fish_eye_black_24dp , "Electrical Wiring",R.drawable.ic_035_chandelier );
        clientData.add(client);
        clientData.add(client1);
        clientData.add(client2);
        clientData.add(client3);
        clientData.add(client4);
        clientData.add(client5);
        clientData.add(client6);




        adapter = new ClientAdapter(ClientPage.this, clientData);
        clientRecyclerView.setAdapter(adapter);
    }

}
