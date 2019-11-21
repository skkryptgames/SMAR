package com.example.smar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class PhotoDisplay extends Fragment {


    static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private static final int RC_SIGN_IN = 123;

    static final int RC_IMAGE_GALLERY = 2;
    private StorageReference mStorageRef;
    FirebaseUser fbUser;
    DatabaseReference database;
    RecyclerView photos;
    Button button;
    RecyclerView.LayoutManager mLayoutManager;
    PhotoAdapter mAdapter;
    ArrayList<AddPhotos> pictures = new ArrayList<>();
    String pId,taskId,projectName,userId;
    ActionBar toolbar;
    TextView toolbarTitle;
    ImageView toolbarImage;
    Bundle bundle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_image_display,container,false);
        mStorageRef = FirebaseStorage.getInstance().getReference();
         bundle=getArguments();

        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        pId=bundle.getString("projectId");
        taskId=bundle.getString("taskId");


        button = (Button) view.findViewById(R.id.button);

        if(bundle.getString("login").equals("client")){
            button.setVisibility(View.GONE);
            userId=bundle.getString("adminUid");
            ((ClientTasks)getActivity()).signOut.setImageResource(R.drawable.ic_141_chat_1);
            ((ClientTasks)getActivity()).signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment=new AdminMessageActivity();
                    FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                    //fragmentTransaction.setCustomAnimations(R.anim.r2l_slide_in, R.anim.r2l_slide_out, R.anim.l2r_slide_in, R.anim.l2r_slide_out);
                    Bundle bundle=new Bundle();
                    bundle.putString("taskId",taskId);
                    bundle.putString("projectId",pId);
                    bundle.putString("adminUid",userId);
                    bundle.putString("login","client");
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_container,fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });


        }
        if(bundle.getString("login").equals("admin")){
            button.setVisibility(View.VISIBLE);
            userId=bundle.getString("userId");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(view);
            }
        });


        fbUser = FirebaseAuth.getInstance().getCurrentUser();



        database = FirebaseDatabase.getInstance().getReference("users").child(userId).child("projects").child(pId).child("tasks").child(taskId);
        // Setup the RecyclerView
        photos =view.findViewById(R.id.photos);
        mLayoutManager = new LinearLayoutManager(getContext());
        photos.setHasFixedSize(true);
        photos.setLayoutManager(mLayoutManager);
        mAdapter = new PhotoAdapter(getContext(), pictures);
        photos.setAdapter(mAdapter);

        Query imagesQuery = database.child("images").orderByKey().limitToFirst(100);
        pictures.clear();
        imagesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                // A new image has been added, add it to the displayed list
              final  AddPhotos addPhotos = dataSnapshot.getValue(AddPhotos.class);
                // get the image DBUser
                database.child("users/" + addPhotos.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DBUser dbUser = dataSnapshot.getValue(DBUser.class);
                        addPhotos.dbUser = dbUser;
                        //images.add(image);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                pictures.add(addPhotos);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       return view;
    }

    public void uploadImage(View view) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent,"select_multi_images" ),RC_IMAGE_GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent,"select_multi_images" ),RC_IMAGE_GALLERY);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_IMAGE_GALLERY && resultCode == RESULT_OK) {


                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        //Uri uri = data.getData();
                        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        final StorageReference imagesRef = FirebaseStorage.getInstance().getReference().child("images"+System.currentTimeMillis());
                        StorageReference userRef = imagesRef.child(fbUser.getUid());
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String filename = fbUser.getUid() + "_" + timeStamp;
                        StorageReference fileRef = userRef.child(filename);

                        UploadTask uploadTask = fileRef.putFile(uri);
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();


                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(getContext(), "Upload failed!\n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                //PhotoDisplay a = (PhotoDisplay) getFragmentManager().findFragmentByTag("photoDisplay");
                                //String tId = a.taskId;
                                final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(fbUser.getUid()).child("projects").child(pId).child("tasks").child(taskId);


                                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadUrl = uri.toString();
                                        String key = database.child("images").push().getKey();
                                        AddPhotos addPhotos = new AddPhotos(key, fbUser.getUid(), downloadUrl);
                                        database.child("images").child(key).setValue(addPhotos);
                                        Log.i("Upload Finished", downloadUrl);
                                    }
                                });


                            }
                        });
                    }
                }
            }
        }


    }
