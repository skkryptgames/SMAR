package com.example.smar;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.internal.ParcelableSparseIntArray;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.mViewHolder>{


    private ArrayList<AddPhotos> pics;
    private PhotoDisplay mActivity;



  public PhotoAdapter(PhotoDisplay mActivity, ArrayList<AddPhotos> pics){
      this.mActivity=mActivity;
      this.pics=pics;

  }

    @NonNull
    @Override
    public PhotoAdapter.mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_display_model, parent, false);

        mViewHolder vh = new mViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.mViewHolder holder, int position) {
        AddPhotos addPhotos = (AddPhotos) pics.get(position);
        Picasso.get().load(addPhotos.downloadUrl).into(holder.displayImage);

  }

    @Override
    public int getItemCount() {
        return pics.size();
    }



    public static class mViewHolder extends RecyclerView.ViewHolder {

       ImageView displayImage;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);

            displayImage = itemView.findViewById(R.id.imageDisplay);
        }
    }

    public void addPhoto(AddPhotos addPhotos) {
        pics.add(0, addPhotos);
        notifyDataSetChanged();
    }



}
