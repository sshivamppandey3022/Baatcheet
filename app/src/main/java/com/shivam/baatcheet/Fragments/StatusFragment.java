package com.shivam.baatcheet.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shivam.baatcheet.Adapters.StatusAdapter;
import com.shivam.baatcheet.Models.Status;
import com.shivam.baatcheet.Models.UserStatus;
import com.shivam.baatcheet.Models.Users;
import com.shivam.baatcheet.databinding.FragmentStatusBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class StatusFragment extends Fragment {
    FragmentStatusBinding binding;
    StatusAdapter statusAdapter;
    ArrayList<UserStatus> statuses = new ArrayList<>();
    ProgressDialog pd;
    FirebaseDatabase database;
    Users users;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(inflater, container, false);
        pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading...");
        pd.setCancelable(false);

        statusAdapter = new StatusAdapter(getContext(), statuses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.statusList.setAdapter(statusAdapter);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.statusList.setLayoutManager(layoutManager);


        binding.addStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 30);
            }
        });
//        database.getReference().child("Users")
//                .child(FirebaseAuth.getInstance().getUid())
//                .child("stories")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        users = snapshot.getValue(Users.class);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//        database.getReference().child("stories")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()){
//                            for(DataSnapshot storySnapshot : snapshot.getChildren()){
//                                UserStatus status = new UserStatus();
//                                status.setName(storySnapshot.child("name").getValue(String.class));
//                                status.setProfilePic(storySnapshot.child("profilePic").getValue(String.class));
//                                status.setLastUpdated(storySnapshot.child("lastUpdated").getValue(Long.class));
//                                statuses.add(status);
//                            }
//                            statusAdapter.notifyDataSetChanged();
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            if (data.getData() != null){
                pd.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                Date date = new Date();
                StorageReference reference = storage.getReference().child("status")
                        .child(date.getTime() + "");
                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserStatus userStatus = new UserStatus();
                                    userStatus.setName(users.getUserName());
                                    userStatus.setProfilePic(users.getProfilePic());
                                    userStatus.setLastUpdated(date.getTime());

                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("name", userStatus.getName());
                                    obj.put("profileImage", userStatus.getProfilePic());
                                    obj.put("lastUpdated", userStatus.getLastUpdated());

                                    String imageUrl = uri.toString();
                                    Status status = new Status(imageUrl, userStatus.getLastUpdated());


                                    database.getReference().child("status")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj);

                                    database.getReference().child("status")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("stories")
                                            .push()
                                            .setValue(status);
                                    pd.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        }
    }
}
