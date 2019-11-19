package com.example.smar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 0;
    private ArrayList<ChatMessage> mProjectListData;
    private Context mContext;

    private FirebaseUser fUser;

    public ChatAdapter(Context mContext, ArrayList<ChatMessage> mProjectListData) {
        this.mProjectListData = mProjectListData;
        this.mContext = mContext;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,
                    parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,
                    parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.message_user.setText(mProjectListData.get(position).getMessageText());
    }


    @Override
    public int getItemCount() {
        return mProjectListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView message_user;

        private MyViewHolder(View itemView) {
            super(itemView);

            message_user = (TextView) itemView.findViewById(R.id.sender);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        return MSG_TYPE_LEFT;
    }
}
