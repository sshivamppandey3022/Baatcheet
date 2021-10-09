package com.shivam.baatcheet.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivam.baatcheet.Adapters.Message2Adapter;
import com.shivam.baatcheet.Adapters.MessageAdapter;
import com.shivam.baatcheet.Models.Message;
import com.shivam.baatcheet.Models.MessageModel;
import com.shivam.baatcheet.R;
import com.shivam.baatcheet.databinding.ActivityDetailedChatBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DetailedChatActivity extends AppCompatActivity {
    ActivityDetailedChatBinding binding;
    Message2Adapter adapter;
    ArrayList<Message> messages;

    String senderRoom, receiverRoom;

    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        messages = new ArrayList<>();
        adapter = new Message2Adapter(this, messages, senderRoom, receiverRoom);
        binding.chatRecyclerView.setLayoutManager(new  LinearLayoutManager(this));
        binding.chatRecyclerView.setAdapter(adapter);

        getSupportActionBar().hide();
        binding.detailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");
        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_avtr).into(binding.chatProfile);
        String senderId = FirebaseAuth.getInstance().getUid();
        String receiverId = getIntent().getStringExtra("userId");

        senderRoom = senderId + receiverId;
        receiverRoom = receiverId + senderId;
        database = FirebaseDatabase.getInstance();

        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageId(snapshot1.getKey());
                            messages.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageTxt = binding.edMsg.getText().toString();
                Date date = new Date();
                Message message = new Message(messageTxt, senderId, date.getTime());
                binding.edMsg.setText("");
                String randomKey = database.getReference().push().getKey();
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(randomKey)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .child(randomKey)
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                        HashMap<String, Object> lastMsgObj = new HashMap<>();
                        lastMsgObj.put("lastMsg", message.getMessage());
                        lastMsgObj.put("lastMsgTime", date.getTime());

                        database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                        database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);
                    }

                });
            }
        });
    }
}
