package com.example.smar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateNewProjectFragment extends Fragment {

    private Button button;
    EditText pName,cName,pNumber;
    FirebaseAuth mAuth;
    String uid,key;
    int count=0;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button=view.findViewById(R.id.smar_button_newprojectnext);
        pName=view.findViewById(R.id.smar_edittext_projectname);
        cName=view.findViewById(R.id.smar_edittext_clientname);
        pNumber=view.findViewById(R.id.smar_edittext_phonenumber);
        mAuth=FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=0;



                 uid=mAuth.getCurrentUser().getUid();
                final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(uid).child("projects");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            if (dataSnapshot1.child("projectName").getValue(String.class).equals(pName.getText().toString())) {

                                Toast.makeText(getContext(), "Entered project name already exists", Toast.LENGTH_SHORT).show();
                                count=count+1;}

                            } if(count==0){
                                String key = reference.push().getKey();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("projectName", pName.getText().toString());
                                map.put("clientName", cName.getText().toString());
                                map.put("clientNumber", pNumber.getText().toString());
                                map.put("projectId", key);
                                reference.child(pName.getText().toString()).updateChildren(map);

                                Fragment fragment=new NewProjectStartDateFragment();
                                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                                //fragmentTransaction.setCustomAnimations(R.anim.r2l_slide_in, R.anim.r2l_slide_out, R.anim.l2r_slide_in, R.anim.l2r_slide_out);

                                Bundle bundle=new Bundle();
                                bundle.putString("projectTitle",pName.getText().toString());
                                fragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.fragment_container,fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();



                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.create_new_project_layout,container,false);
        ((AdminPage)getActivity()).toolbarTitle.setText("Project SetUp");
        ((AdminPage)getActivity()).button.setVisibility(View.GONE);
        return view;
    }


}
