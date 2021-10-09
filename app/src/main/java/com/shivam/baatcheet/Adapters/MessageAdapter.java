package com.shivam.baatcheet.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.shivam.baatcheet.Models.MessageModel;
import com.shivam.baatcheet.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter {
    ArrayList<MessageModel> msgModel;
    Context ctx;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public MessageAdapter(ArrayList<MessageModel> msgModel, Context ctx) {
        this.msgModel = msgModel;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_sender, parent, false);
            return new SenderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_receiver, parent, false);
            return new ReceiverViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (msgModel.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else
         {
             return RECEIVER_VIEW_TYPE;
         }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = new MessageModel();
        if (holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderText.setText(messageModel.getMsg());
        }
        else {
            ((ReceiverViewHolder)holder).receiverText.setText(messageModel.getMsg());
        }
    }

    @Override
    public int getItemCount() {
        return msgModel.size();
    }

    public  class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverText, receiverTime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverText = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
        }
    }
    public class SenderViewHolder extends  RecyclerView.ViewHolder{
        TextView senderText, senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderText = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }
}

