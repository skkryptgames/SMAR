package com.example.smar;

import android.content.Context;
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


public class ArchievedProjectsTasksAdapter extends RecyclerView.Adapter<ArchievedProjectsTasksAdapter.ClientViewHolder> {

    private ArrayList<Client> titles;
    Context context;
    String pId=getProjectId();




    public ArchievedProjectsTasksAdapter(Context context , ArrayList<Client> titles) {
        this.context=context;
        this.titles=titles;

    }
    @NonNull
    @Override
    public ArchievedProjectsTasksAdapter.ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_detail_page,
                parent, false);
        return new ArchievedProjectsTasksAdapter.ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArchievedProjectsTasksAdapter.ClientViewHolder holder, final int position) {

        final Client client=titles.get(position);
        holder.design.setText(titles.get(position).getTitle());
        holder.targetDate.setText(titles.get(position).getDate());
        holder.panorama.setImageResource(titles.get(position).getTick());
        // holder.panorama.setImageResource(client.isSelected() ? R.drawable.ic_checked : R.drawable.ic_panorama_fish_eye_black_24dp);
        Picasso.get().load(titles.get(position).getImages()).into(holder.ruler);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new PhotoDisplay();
                FragmentTransaction fragmentTransaction=((ArchievedProjects)context).getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(R.anim.r2l_slide_in, R.anim.r2l_slide_out, R.anim.l2r_slide_in, R.anim.l2r_slide_out);
                Bundle bundle=new Bundle();
                bundle.putString("taskId",titles.get(position).getTaskId());
                bundle.putString("projectId",((ClientPage)context).cProjectId);
                bundle.putString("login","client");
                bundle.putString("adminUid",((ClientPage)context).adminUid);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,fragment,"photoDisplay");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });*/

      /*  holder.panorama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.setSelected(!client.isSelected());
                holder.panorama.setImageResource(client.isSelected() ? R.drawable.ic_checked : R.drawable.ic_panorama_fish_eye_black_24dp);

            }
        });*/



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
