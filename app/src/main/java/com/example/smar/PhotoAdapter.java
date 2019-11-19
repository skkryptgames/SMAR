package com.example.smar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.internal.ParcelableSparseIntArray;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.mViewHolder>{


    private ArrayList<AddPhotos> pictures;
    private PhotoDisplay mActivity;
    PhotoFullScreenDialog photoFullScreenDialog;



  public PhotoAdapter(PhotoDisplay mActivity, ArrayList<AddPhotos> pictures){
      this.mActivity=mActivity;
      this.pictures=pictures;

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
        AddPhotos addPhotos = (AddPhotos) pictures.get(position);
        Picasso.get().load(addPhotos.downloadUrl).placeholder(R.drawable.image_background).into(holder.displayImage);
        holder.check.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                

                return false;
            }
        });

        holder.displayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoFullScreenDialog=new PhotoFullScreenDialog();
                photoFullScreenDialog.showNow(mActivity.getFragmentManager(),"example");
                Picasso.get().load(addPhotos.downloadUrl).into(photoFullScreenDialog.imageView);
            }
        });

        holder.displayImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AddPhotos addPhotos1=new AddPhotos();
                
                return false;
            }
        });

  }

    @Override
    public int getItemCount() {
        return pictures.size();
    }



    public static class mViewHolder extends RecyclerView.ViewHolder {

       ImageView displayImage;
       CheckBox check;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);

            displayImage = itemView.findViewById(R.id.imageDisplay);
            check = itemView.findViewById(R.id.check);
        }
    }

    public void addPhoto(AddPhotos addPhotos) {
        pictures.add(0, addPhotos);
        notifyDataSetChanged();
    }



}
