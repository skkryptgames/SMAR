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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.smar.AdminTasksPage.getProjectId;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private ArrayList<Client> titles;
    Context context;
    String pId=getProjectId();




    public ClientAdapter(Context context , ArrayList<Client> titles) {
        this.context=context;
        this.titles=titles;

    }
    @NonNull
    @Override
    public ClientAdapter.ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_detail_page,
                parent, false);
        return new ClientAdapter.ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClientAdapter.ClientViewHolder holder, final int position) {

        final Client client=titles.get(position);
        holder.design.setText(titles.get(position).getTitle());
        holder.targetDate.setText(titles.get(position).getDate());
        holder.panorama.setImageResource(titles.get(position).getTick());
        // holder.panorama.setImageResource(client.isSelected() ? R.drawable.ic_checked : R.drawable.ic_panorama_fish_eye_black_24dp);
        Picasso.get().load(titles.get(position).getImages()).into(holder.ruler);

      /*  holder.panorama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.setSelected(!client.isSelected());
                holder.panorama.setImageResource(client.isSelected() ? R.drawable.ic_checked : R.drawable.ic_panorama_fish_eye_black_24dp);

            }
        });*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,TaskStatusUpdate.class);


                intent.putExtra("taskId",titles.get(position).getTaskId());
                intent.putExtra("projectId",pId);
                intent.putExtra("moduleName",holder.design.getText().toString());
                intent.putExtra("targetDate",holder.targetDate.getText().toString());
                intent.putExtra("position",position);
                intent.putExtra("image",titles.get(position).getImages());
                intent.putExtra("progress",titles.get(position).getTick());
                intent.putExtra("userId", AdminTasksPage.uId);
                intent.putExtra("title",((AdminTasksPage)context).title);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    class ClientViewHolder extends RecyclerView.ViewHolder {

        ImageView ruler,panorama;
        TextView design,targetDate;



        public ClientViewHolder(View itemView) {
            super(itemView);



            design = itemView.findViewById(R.id.design);
            targetDate = itemView.findViewById(R.id.targetDate);
            panorama = itemView.findViewById(R.id.panorama);
            ruler = itemView.findViewById(R.id.ruler);



        }
    }

}
