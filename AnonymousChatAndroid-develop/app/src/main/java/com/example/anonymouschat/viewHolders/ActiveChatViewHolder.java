package com.example.anonymouschat.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anonymouschat.R;

public class ActiveChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ActiveChatClickListener _clickListener;
    public TextView alias;
    public TextView topic;

    public ActiveChatViewHolder(@NonNull View itemView, ActiveChatClickListener clickListener) {
        super(itemView);

        alias = itemView.findViewById(R.id.activeChatAlias);
        topic = itemView.findViewById(R.id.activeChatTopic);

        _clickListener = clickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        _clickListener.onActiveChatClick(getAdapterPosition());
    }
}
