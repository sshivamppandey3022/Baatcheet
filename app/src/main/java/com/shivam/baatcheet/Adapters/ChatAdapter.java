package com.shivam.baatcheet.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivam.baatcheet.Activities.DetailedChatActivity;
import com.shivam.baatcheet.Models.Users;
import com.shivam.baatcheet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    ArrayList<Users> list;
    Context ctx;

    public ChatAdapter(ArrayList<Users> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.chat_design,parent,false);
        return new ChatAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        Users users = list.get(position);
        String senderId = FirebaseAuth.getInstance().getUid();
        String senderRoom = senderId + users.getUserId();
        FirebaseDatabase.getInstance().getReference()
                .child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                    //long time = snapshot.child("lastMsgTime").getValue(long.class);
                    holder.lastMsg.setText(lastMsg);
                }else {
                    holder.lastMsg.setText("Tap to chat");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.chatName.setText(users.getUserName());
        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.ic_avtr).into(holder.chatImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, DetailedChatActivity.class);
                intent.putExtra("userId", users.getUserId());
                intent.putExtra("profilePic", users.getProfilePic());
                intent.putExtra("userName", users.getUserName());
                ctx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chatName, lastMsg, chatTime;
        CircleImageView chatImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatName = itemView.findViewById(R.id.chatName);
            lastMsg = itemView.findViewById(R.id.lastMsg);
            chatImage = itemView.findViewById(R.id.chatImage);
            //chatTime = itemView.findViewById(R.id.chatTime);
        }
    }
}
