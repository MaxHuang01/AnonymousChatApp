package com.example.anonymouschat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.anonymouschat.models.Topic;
import com.example.anonymouschat.models.User;
import com.example.anonymouschat.services.JsonService;
import com.example.anonymouschat.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

public class ProfileTopicsFragment extends Fragment {
    private Topic _selectedTopic;
    private UserService _userService;
    private List<User> _usersInterestedInTopic;
    private LinearLayout _linearLayout;
    private View _view;
    private Integer _textId;
    private List<String> _profileButtons;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_profiles_topics, container, false);
        _linearLayout = _view.findViewById(R.id.profile_linear_layout);
        _usersInterestedInTopic = new ArrayList<>();
        _profileButtons = new ArrayList<>();
        _userService = new UserService(getActivity());
        _textId = R.id.selectProfileTextView;
        TextView mTextView = _view.findViewById(_textId);

        Bundle b = this.getArguments();
        if (b != null) {
            _selectedTopic = (Topic) b.getSerializable("TOPIC");
            mTextView.setText(String.format("Profiles who want to talk about %s", _selectedTopic.name));
        }
        _getListOfProfiles();

        return _view;
    }

    public void openChatWindowActivity(User selectedUser) {
        Intent intent = new Intent(ProfileTopicsFragment.this.getActivity(), ChatWindowActivity.class)
                .putExtra("USER", selectedUser)
                .putExtra("TOPIC", _selectedTopic);
        startActivity(intent);
    }

    public void _getListOfProfiles() {
        _userService.getUsersByTopic(_selectedTopic.id, ((isSuccessful, jsonResponse) -> {
            if (isSuccessful) {
                _usersInterestedInTopic.clear();
                _usersInterestedInTopic.addAll(JsonService.parseUsersJsonString(jsonResponse));

                _populateLinearLayout();
            }
        }));
    }

    private void _populateLinearLayout() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < _usersInterestedInTopic.size(); i++) {
                    User currUser = _usersInterestedInTopic.get(i);
                    currUser.alias = RandomAliasGenerator.getRandomName();

                    Button temp = new Button(_view.getContext());
                    temp.setId(i);
                    temp.setText(currUser.alias);
                    temp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b = (Button) v;
                            User selectedUser = _usersInterestedInTopic.get(b.getId());
                            if (selectedUser != null) {
                                openChatWindowActivity(selectedUser);
                            }
                        }
                    });

                    _linearLayout.addView(temp);
                }
            }
        });
    }
}
