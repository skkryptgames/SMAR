package com.example.smar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.MyViewHolder> {
    private ArrayList<ProjectList> mProjectListData;
    private Context mContext;
    boolean projectisStarted=true;
    boolean projectisDelayed=true;
    boolean projectisnotStarted=true;

    public ProjectListAdapter(Context mContext, ArrayList<ProjectList> mProjectListData) {
        this.mProjectListData=mProjectListData;
        this.mContext=mContext;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_detail_page,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.imageView.setImageResource(mProjectListData.get(position).getProgress());
        holder.projectName.setText(mProjectListData.get(position).getProjectName());
        holder.work.setText(mProjectListData.get(position).getWork());
        holder.date.setText(mProjectListData.get(position).getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClientPage.class);
                intent.putExtra("title", holder.projectName.getText().toString());
                intent.putExtra("projectId",mProjectListData.get(position).getpId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProjectListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, work,day, projectName;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);


            date = itemView.findViewById(R.id.date);
            work = itemView.findViewById(R.id.work);
            day = itemView.findViewById(R.id.day);
            projectName = itemView.findViewById(R.id.projectName);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
