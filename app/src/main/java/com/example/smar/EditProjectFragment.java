package com.example.smar;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class EditProjectFragment extends Fragment {
    String uid,pid;
    String projectName,clientName,clientNumber;
    EditText pName,cName,cNumber;
    Button button;
    String number;


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



        cNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {




            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()>0) {
                    char a;
                    a = s.charAt(0);
                    if (a == '+') {
                        cNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                        number = cNumber.getText().toString();


                    } else {
                        cNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        number = cNumber.getText().toString();

                    }
                }

            }
        });

        button=view.findViewById(R.id.smar_button_newprojectnext);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(pName.getText().toString()) && !TextUtils.isEmpty(cName.getText().toString()) && !TextUtils.isEmpty(cNumber.getText().toString())) {
                    try {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                    HashMap<String, Object> a = new HashMap<>();
                    a.put("projectName", pName.getText().toString());
                    a.put("clientName", cName.getText().toString());
                    if(number.length()>10){
                        number=number.substring(3);
                    }
                    a.put("clientNumber", number);

                    FirebaseDatabase.getInstance().getReference("users").child(uid).child("projects").child(pid).updateChildren(a);

                    Fragment fragment = new NewProjectStartDateFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("projectTitle", pName.getText().toString());
                    bundle.putString("projectKey", pid);
                    bundle.putString("status", "update");
                    bundle.putString("userId", uid);
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                }else {
                    Toast.makeText(getContext(),"Please enter valid data",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
