package com.shivam.baatcheet.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shivam.baatcheet.Models.UserStatus;
import com.shivam.baatcheet.databinding.ItemStoryBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    Context context;
    ArrayList<UserStatus> userStories;

    public StatusAdapter(Context context, ArrayList<UserStatus> userStories) {
        this.context = context;
        this.userStories = userStories;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.shivam.baatcheet.R.layout.item_story, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return userStories.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {
        ItemStoryBinding binding;
        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStoryBinding.bind(itemView);
        }
    }
}
