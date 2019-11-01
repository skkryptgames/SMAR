package com.example.smar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private ArrayList<Client> titles;
    Context context;




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
    public void onBindViewHolder(@NonNull final ClientAdapter.ClientViewHolder holder, int position) {

        final Client client=titles.get(position);
        holder.design.setText(titles.get(position).getTitle());
        holder.targetDate.setText(titles.get(position).getDate());
        holder.panorama.setImageResource(client.isSelected() ? R.drawable.ic_checked : R.drawable.ic_panorama_fish_eye_black_24dp);
        holder.ruler.setImageResource(titles.get(position).getImages());


        holder.panorama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.setSelected(!client.isSelected());
                holder.panorama.setImageResource(client.isSelected() ? R.drawable.ic_checked : R.drawable.ic_panorama_fish_eye_black_24dp);

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment=new TaskStatusFragment();
                FragmentTransaction fragmentTransaction=((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();

                Bundle bundle=new Bundle();
                bundle.putString("moduleName",holder.design.getText().toString());
                bundle.putString("targetDate",holder.targetDate.getText().toString());
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

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
