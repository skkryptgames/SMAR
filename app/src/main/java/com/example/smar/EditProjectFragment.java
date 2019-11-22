package com.example.smar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditProjectFragment extends Fragment {
    String uid,pid;
    String projectName,clientName,clientNumber;
    EditText pName,cName,cNumber;
    Button button;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.create_new_project_layout,container,false);

        pName=view.findViewById(R.id.smar_edittext_projectname);
        cName=view.findViewById(R.id.smar_edittext_clientname);
        cNumber=view.findViewById(R.id.smar_edittext_phonenumber);

        Bundle bundle=getArguments();

        pid=bundle.getString("projectId");
        uid=bundle.getString("userId");

        FirebaseDatabase.getInstance().getReference("users").child(uid).child("projects").child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projectName=dataSnapshot.child("projectName").getValue(String.class);
                clientName=dataSnapshot.child("clientName").getValue(String.class);
                clientNumber=dataSnapshot.child("clientNumber").getValue(String.class);
                pName.setText(projectName);
                cName.setText(clientName);
                cNumber.setText(clientNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        button=view.findViewById(R.id.smar_button_newprojectnext);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> a =new HashMap<>();
                a.put("projectName",pName.getText().toString());
                a.put("clientName",cName.getText().toString());
                a.put("clientNumber",cNumber.getText().toString());

                FirebaseDatabase.getInstance().getReference("users").child(uid).child("projects").child(pid).updateChildren(a);

                Fragment fragment=new NewProjectStartDateFragment();
                Bundle bundle=new Bundle();
                bundle.putString("projectTitle",pName.getText().toString());
                bundle.putString("projectKey",pid);
                bundle.putString("status","update");
                bundle.putString("userId",uid);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });


    }
}
