package com.example.anonymouschat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anonymouschat.R;
import com.example.anonymouschat.models.Conversation;
import com.example.anonymouschat.services.CurrentUserService;
import com.example.anonymouschat.viewHolders.ActiveChatClickListener;
import com.example.anonymouschat.viewHolders.ActiveChatViewHolder;

import java.util.List;

public class ActiveChatAdapter extends RecyclerView.Adapter<ActiveChatViewHolder> {
    private final ActiveChatClickListener _clickListener;
    private final List<Conversation> _activeChats;

    public ActiveChatAdapter(List<Conversation> activeChats, ActiveChatClickListener clickListener) {
        _clickListener = clickListener;
        _activeChats = activeChats;
    }

    @NonNull
    @Override
    public ActiveChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.active_chat_row_item, parent, false);
        return new ActiveChatViewHolder(itemView, _clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveChatViewHolder holder, int position) {
        Conversation conversation = _activeChats.get(position);
        holder.alias.setText(conversation.getOtherUserAlias(CurrentUserService.getCurrentUser().id));
        holder.topic.setText(conversation.topic.name);
    }

    @Override
    public int getItemCount() {
        return _activeChats.size();
    }
}
