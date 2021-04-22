package com.example.anonymouschat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.anonymouschat.models.Topic;
import com.example.anonymouschat.models.User;
import com.example.anonymouschat.services.CurrentUserService;
import com.example.anonymouschat.services.JsonService;
import com.example.anonymouschat.services.UserService;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private User _currentUser;

    private TextView sOrient;
    private TextView uName;
    private TextView age;
    private TextView uGender;
    private TextView printTopics;

    private List<Topic> selectedTopics = new ArrayList<Topic>();

    private ImageView userImageView;
    private static final String TAG = "ProfileFragment";

    private String ID;
    private String topicString;
    private String blockString;
    private UserService _UserService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _currentUser = CurrentUserService.getCurrentUser();
        _UserService = new UserService(getContext());

        Button logoutButton;

        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });

        Button editInfoButton = view.findViewById(R.id.EditInfoButton);
        editInfoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(getActivity(), EditInfoActivity.class);
                intent.putExtra("id", ID);
                intent.putExtra("sOrient",sOrient.getText());
                intent.putExtra("uName", uName.getText());
                intent.putExtra("age", age.getText());
                intent.putExtra("uGender", uGender.getText());

                String[] selectedTopicsArray = new String[selectedTopics.size()];
                for (int i = 0; i < selectedTopics.size(); i++) {
                    selectedTopicsArray[i] = selectedTopics.get(i).name;
                }

                intent.putExtra("selectedTopics", selectedTopicsArray);

                startActivityForResult(intent, 5);
            }
        });

        Button removeProfileButton = view.findViewById(R.id.RemoveProfileButton);
        removeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Remove Profile");
                alertDialogBuilder.setMessage("Are you sure you want to remove your profile?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(), CurrentUserService.getCurrentUser().userName, Toast.LENGTH_SHORT).show();
                        _UserService.deleteUser(CurrentUserService.getCurrentUser().id, (isSuccessful, jsonResponse) -> {
                            if (isSuccessful) {
                                logout(v);
                            }
                            else {
                                Toast.makeText(getContext(), "Couldn't remove profile.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Shouldn't do anything here, just go back to ProfileFragment
                    }
                });

                alertDialogBuilder.show();
            }
        });


    /** Assign Fragment Text boxes to variables **/
        sOrient = (TextView) view.findViewById(R.id.SexualOrientationText);
        uName = (TextView) view.findViewById(R.id.UserNameText);
        age = (TextView) view.findViewById(R.id.ageText);
        uGender = (TextView) view.findViewById(R.id.genderText);
        printTopics = (TextView) view.findViewById(R.id.userTopicText);

        _getUserProfile(_currentUser.userName);

    /** Generate background color and image **/
        RandomAliasGenerator random = new RandomAliasGenerator();
        userImageView = (ImageView) view.findViewById(R.id.userImageView);
        userImageView.setBackgroundColor(Color.parseColor(random.getRandomColor()));

        return view;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            if(resultCode == RESULT_OK)
            {
                _getUserProfile(data.getStringExtra("uName"));
            }
        }
    }

    private void _getUserProfile(String userName) {
        _UserService.getUser(userName, ((isSuccessful, jsonResponse) -> {
            if (isSuccessful) {
                CurrentUserService.setCurrentUser(JsonService.parseFirstUserInArrayJsonString(jsonResponse));
                _currentUser = CurrentUserService.getCurrentUser();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _populateProfileValues();
                    }
                });
            }
        }));
    }

    private void _populateProfileValues() {
        ID = _currentUser.id;
        sOrient.setText(_currentUser.sexualOrientation);
        uName.setText(_currentUser.userName);
        if (_currentUser.age == null) {
            age.setText("");
        }
        else {
            age.setText(_currentUser.age.toString());
        }
        uGender.setText(_currentUser.gender);

        /** Print list of User Selected Topics **/
        selectedTopics.clear();
        selectedTopics.addAll(_currentUser.selectedTopics);

        for (int i = 0; i < selectedTopics.size(); i++) {
            if (i == 0) topicString = selectedTopics.get(i).name;
            else topicString =  selectedTopics.get(i).name + ", " + topicString;
        }
        printTopics.setText(topicString);
    }

    private void logout(final View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

        startActivity(new Intent(view.getContext(), Login.class));
    }
}
