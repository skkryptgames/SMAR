package com.example.smar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class AdminMessageActivity extends Fragment {

    EditText edittext_chat;
    Button button_chat_send;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference ref;
    private RecyclerView recyler_chat;
    private ChatAdapter mAdapter;
    TextView message_user;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<ChatMessage> messages = new ArrayList<>();
    String pId;
    String taskId;
    String userType,userId;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_admin_message, container, false);

        recyler_chat = view.findViewById(R.id.recycler_chat);

        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyler_chat.setLayoutManager(mLinearLayoutManager);

        recyler_chat.setHasFixedSize(true);


        mAdapter = new ChatAdapter(getContext(), messages);
        recyler_chat.setAdapter(mAdapter);


       return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        pId = bundle.getString("projectId");
        taskId = bundle.getString("taskId");

        if(bundle.getString("login").equals("client")){

            userId=bundle.getString("adminUid");
            userType="client";



        }
        if(bundle.getString("login").equals("admin")){

            userId=bundle.getString("userId");
            userType="admin";
        }





        edittext_chat = view.findViewById(R.id.edittext_chat);
        button_chat_send = view.findViewById(R.id.button_chat_send);

        ref = FirebaseDatabase.getInstance().getReference();

        mFirebaseAuth = FirebaseAuth.getInstance();



        readMessages();



        button_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(edittext_chat.getText().toString())){
                sendMessage(edittext_chat.getText().toString(),mFirebaseAuth.getCurrentUser().getPhoneNumber());
                //recyler_chat.setAdapter(mAdapter);
                edittext_chat.setText("");}

            }

            private void sendMessage(String message, String sender) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("projects").child(pId).child("tasks").child(taskId).child("comments");


                String key = reference.push().getKey();
                HashMap<String,Object> a=new HashMap<>();
                a.put("sender",sender);
                a.put("message",message);
                a.put("userType",userType);
                reference.child(key).setValue(a);
            }

        });
    }

    private void readMessages() {

        ref = FirebaseDatabase.getInstance().getReference("users").child(userId).child("projects").child(pId).child("tasks").child(taskId).child("comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String sender=snapshot.child("sender").getValue(String.class);
                    String message=snapshot.child("message").getValue(String.class);
                    String userId=snapshot.child("userType").getValue(String.class);
                    ChatMessage chatMessage=new ChatMessage(message,sender,userId);
                    messages.add(chatMessage);
                    mAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }








}

