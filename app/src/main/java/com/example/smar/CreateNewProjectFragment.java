package com.example.smar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class CreateNewProjectFragment extends Fragment {

    private Button button;
    EditText pName,cName,PNumber;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button=view.findViewById(R.id.smar_button_newprojectnext);
        pName=view.findViewById(R.id.smar_edittext_projectname);
        cName=view.findViewById(R.id.smar_edittext_clientname);
        PNumber=view.findViewById(R.id.smar_edittext_phonenumber);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
