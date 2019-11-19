package com.example.smar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArchievedProjectListAdapter extends RecyclerView.Adapter<ArchievedProjectListAdapter.MyViewHolder> {
    private ArrayList<ProjectList> mProjectListData;
    private Context mContext;
    boolean projectisStarted=true;
    boolean projectisDelayed=true;
    boolean projectisnotStarted=true;

    public ArchievedProjectListAdapter(Context mContext, ArrayList<ProjectList> mProjectListData) {
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
                Fragment fragment=new ArchievedProjectTasksPage();
                FragmentTransaction fragmentTransaction=((ArchievedProjects)mContext).getSupportFragmentManager().beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("title", holder.projectName.getText().toString());
                bundle.putString("projectId",mProjectListData.get(position).getpId());
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

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
