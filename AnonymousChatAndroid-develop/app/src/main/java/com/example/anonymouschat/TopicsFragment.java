package com.example.anonymouschat;

import com.example.anonymouschat.adapters.ActiveChatAdapter;
import com.example.anonymouschat.models.Topic;
import com.example.anonymouschat.services.*;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TopicsFragment extends Fragment {
    static String chosenTopic = "none";
    private TopicService _topicService;
    private List<Topic> _existingTopics;
    private LinearLayout _linearLayout;
    private View _view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_topics, container, false);
        _linearLayout = _view.findViewById(R.id.topic_linear_layout);

        _topicService = new TopicService(getActivity());
        _getListOfTopics();

        return _view;
    }

    public void openProfilesList() {
        Fragment selectedFragment = new ProfileTopicsFragment();
        Topic selectedTopic = _findTopic();
        if (selectedTopic != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("TOPIC", _findTopic());
            selectedFragment.setArguments(bundle);
            this.getFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
        }
        else {
            Toast.makeText(getContext(), "Invalid topic selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void _getListOfTopics() {
        _existingTopics = new ArrayList<>();
        _topicService.getAllTopics((isSuccessful, jsonResponse) -> {
            if (isSuccessful) {
                _existingTopics.clear();
                _existingTopics.addAll(JsonService.parseTopicsJsonString(jsonResponse));
                _populateLinearLayout();
            }
        });
    }

    private void _populateLinearLayout() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < _existingTopics.size(); i++) {
                    Button temp = new Button(_view.getContext());
                    temp.setText(_existingTopics.get(i).name);
                    _linearLayout.addView(temp);
                    temp.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Button b = (Button) v;
                            chosenTopic = b.getText().toString();
                            openProfilesList();
                        }
                    });
                }
            }
        });
    }

    private Topic _findTopic() {
        for (int i = 0; i < _existingTopics.size(); i++) {
            Topic currentTopic = _existingTopics.get(i);
            if (currentTopic.name == chosenTopic) {
                return currentTopic;
            }
        }

        return null;
    }
}
