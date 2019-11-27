package com.example.smar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

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
    private ArrayList<AddPhotos> selection_item = new ArrayList<>();
    private int counter=0, totalcheckboxesChecked;
    String pId,taskId,projectName,userId;
    ActionBar toolbar;
    TextView toolbarTitle;
    ImageView toolbarImage;
    Bundle bundle;
    private RelativeLayout relativeLayout;
    private static TextView counterText;
    private ImageButton deleteselected;
    private static Toolbar toolbarDelete;
    ArrayList<String> del = new ArrayList<>();
    private PhotoDisplay mActivity;
    boolean a = false;

    public static Fragment newInstance() {

        return new PhotoDisplay();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_image_display,container,false);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        bundle=getArguments();

        counterText = view.findViewById(R.id.counterText);
        deleteselected = view.findViewById(R.id.deleteselected);

        relativeLayout = view.findViewById(R.id.relativeLayout);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        pId=bundle.getString("projectId");
        taskId=bundle.getString("taskId");


        button = (Button) view.findViewById(R.id.button);

        if(bundle.getString("login").equals("client")){
            button.setVisibility(View.GONE);
            userId=bundle.getString("adminUid");
            ((ClientPage)getActivity()).signOut.setImageResource(R.drawable.ic_141_chat_1);
            ((ClientPage)getActivity()).signOut.setOnClickListener(new View.OnClickListener() {
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

        deleteselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String str: del) {

                    FirebaseDatabase.getInstance().getReference("users").child(userId).child("projects").child(pId).child("tasks").child(taskId).child("images").child(str).removeValue();

                    //mAdapter.notifyDataSetChanged();

                }
                a=false;
                relativeLayout.setVisibility(View.GONE);

            }
        });




        fbUser = FirebaseAuth.getInstance().getCurrentUser();



        database = FirebaseDatabase.getInstance().getReference("users").child(userId).child("projects").child(pId).child("tasks").child(taskId);
        // Setup the RecyclerView
        photos =view.findViewById(R.id.photos);
        mLayoutManager = new LinearLayoutManager(getContext());
        photos.setHasFixedSize(true);
        photos.setLayoutManager(mLayoutManager);
        mAdapter = new PhotoAdapter(this, pictures);
        photos.setAdapter(mAdapter);

        database.child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pictures.clear();
                AddPhotos addPhotos;
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    addPhotos = dataSnapshot1.getValue(AddPhotos.class);
                    mAdapter.addPhoto(addPhotos);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }


    public class mViewHolder extends RecyclerView.ViewHolder {

        ImageView displayImage;
        CheckBox check;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);

            displayImage = itemView.findViewById(R.id.imageDisplay);
            check = itemView.findViewById(R.id.check);

        }
    }


    public class PhotoAdapter extends RecyclerView.Adapter<mViewHolder> {


        private ArrayList<AddPhotos> pics;
        private PhotoDisplay mActivity;
        PhotoFullScreenDialog photoFullScreenDialog;




        public PhotoAdapter(PhotoDisplay mActivity, ArrayList<AddPhotos> pics) {
            this.pics = pics;
            this.mActivity = mActivity;


        }

        @NonNull
        @Override
        public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_display_model, parent, false);
            mViewHolder vh = new mViewHolder(v);
            return vh;

        }

        @Override
        public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
            AddPhotos addPhotos = (AddPhotos) pics.get(position);
            Picasso.get().load(addPhotos.downloadUrl).placeholder(R.drawable.image_background).into(holder.displayImage);
            holder.check.setVisibility(a ? View.VISIBLE : View.GONE);


            /*if (pics.get(position).getA()==0) {
                holder.check.setVisibility(View.GONE);
            } else {
                holder.check.setVisibility(View.VISIBLE);
            }*/





            holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(compoundButton.isChecked())
                    {

                        compoundButton.setChecked(true);
                        del.add(pics.get(position).getKey());
                        counter++;
                        counterText.setText(counter + " " + "Item(s) Selected");


                    }
                    else
                    {
                        compoundButton.setChecked(false);
                        del.remove(pics.get(position).getKey());
                        counter--;
                        counterText.setText(counter + " " + "Item(s) Selected");

                    }

                }


            });


            holder.displayImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    photoFullScreenDialog = new PhotoFullScreenDialog();
                    photoFullScreenDialog.showNow(mActivity.getFragmentManager(), "example");
                    Picasso.get().load(addPhotos.downloadUrl).into(photoFullScreenDialog.imageView);
                }
            });

            holder.displayImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    a=true;

                    notifyDataSetChanged();


                    return false;
                }
            });

        }

        @Override
        public int getItemCount() {
            return pics.size();
        }

        public void addPhoto(AddPhotos addPhotos) {
            pics.add(0, addPhotos);
            notifyDataSetChanged();
        }
    }

    public void uploadImage(View view) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
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
                intent.setAction(Intent.ACTION_GET_CONTENT);
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
                    final StorageReference imagesRef = FirebaseStorage.getInstance().getReference().child("images" + System.currentTimeMillis());
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

                                final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(userId).child("projects").child(pId).child("tasks").child(taskId);


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
                }else if(data.getData()!=null){
                    Uri uri = data.getData();
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

                            final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(userId).child("projects").child(pId).child("tasks").child(taskId);


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
