package com.example.anonymouschat;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anonymouschat.adapters.ActiveChatAdapter;
import com.example.anonymouschat.models.Conversation;
import com.example.anonymouschat.models.User;
import com.example.anonymouschat.services.ConversationService;
import com.example.anonymouschat.services.CurrentUserService;
import com.example.anonymouschat.services.JsonService;
import com.example.anonymouschat.services.UserService;
import com.example.anonymouschat.viewHolders.ActiveChatClickListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ActiveChatClickListener {
    private static final String TAG = "HomeFragment";
    
    private View _view;
    private ConversationService _conversationService;
    private List<Conversation> _existingChats;

    private ActiveChatAdapter _adapter;
    private RecyclerView _recycler;

    private User _currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_home, container, false);

        _existingChats = new ArrayList<>();
        _initializeRecycler();

        _conversationService = new ConversationService(getActivity());
        _currentUser = CurrentUserService.getCurrentUser();

        _getExistingChats();
        return _view;
    }



    private void _getExistingChats() {
        _conversationService.getConversationsForUser(_currentUser.id, (isSuccessful, jsonResponse) -> {
            if (isSuccessful) {
                _existingChats.clear();
                _existingChats.addAll(JsonService.parseConversationsJsonString(jsonResponse));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void _initializeRecycler() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _adapter = new ActiveChatAdapter(_existingChats, HomeFragment.this);

                _recycler = _view.findViewById(R.id.activeChatsRecycler);
                _recycler.setAdapter(_adapter);
                _recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
    }

    @Override
    public void onActiveChatClick(int position) {
        Conversation selectedConversation = _existingChats.get(position);
        Intent intent = new Intent(getActivity(), ChatWindowActivity.class)
                .putExtra("CONVERSATION", selectedConversation);
        startActivity(intent);
    }
}
