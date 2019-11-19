package com.example.smar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_admin_message, container, false);
        recyler_chat = view.findViewById(R.id.recycler_chat);
        recyler_chat.setHasFixedSize(true);
        recyler_chat.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyler_chat.setAdapter(new ChatAdapter(getContext(), messages));

       return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        pId = bundle.getString("projectId");
        taskId = bundle.getString("taskId");


        readMessages();

        edittext_chat = view.findViewById(R.id.edittext_chat);
        button_chat_send = view.findViewById(R.id.button_chat_send);

        ref = FirebaseDatabase.getInstance().getReference();

        mFirebaseAuth = FirebaseAuth.getInstance();

        recyler_chat = view.findViewById(R.id.recycler_chat);

        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyler_chat.setLayoutManager(mLinearLayoutManager);

        recyler_chat.setHasFixedSize(true);


        mAdapter = new ChatAdapter(getContext(), messages);
        recyler_chat.setAdapter(mAdapter);


        recyler_chat.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));




        button_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(mFirebaseAuth.getCurrentUser().getPhoneNumber(),edittext_chat.getText().toString());
                recyler_chat.setAdapter(mAdapter);


            }

            private void sendMessage(String message, String sender) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseAuth.getInstance().getUid()).child("projects").child(pId).child("tasks").child(taskId).child("comments");


                String key = reference.push().getKey();
                ChatMessage message1 = new ChatMessage(sender, message);
                reference.child(key).setValue(message1);
            }

        });
    }

    private void readMessages() {

        messages = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseAuth.getInstance().getUid()).child("projects").child(pId).child("tasks").child(taskId).child("comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
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

