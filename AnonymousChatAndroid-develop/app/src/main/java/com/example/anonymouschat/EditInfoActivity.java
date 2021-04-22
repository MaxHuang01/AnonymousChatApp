package com.example.anonymouschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anonymouschat.models.Topic;
import com.example.anonymouschat.services.JsonService;
import com.example.anonymouschat.services.TopicService;
import com.example.anonymouschat.services.UserService;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditInfoActivity extends AppCompatActivity {
    private UserService _UserService;
    private TopicService _TopicService;

    private List<Topic> _existingTopics = new ArrayList<Topic>();

    private String ID;
    private TextView sOrient;
    private TextView uName;
    private TextView age;
    private TextView uGender;
    private List<String> selectedTopics = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        _UserService = new UserService(this);
        _TopicService = new TopicService(this);

        sOrient = (TextView) findViewById(R.id.editSexualOrientation);
        uName = (TextView) findViewById(R.id.editUserName);
        age = (TextView) findViewById(R.id.editAge);
        uGender = (TextView) findViewById(R.id.editGender);

        Intent intent = getIntent();
        ID = intent.getStringExtra("id");
        sOrient.setText(intent.getStringExtra("sOrient"));
        uName.setText(intent.getStringExtra("uName"));
        age.setText(intent.getStringExtra("age"));
        uGender.setText(intent.getStringExtra("uGender"));
        selectedTopics = Arrays.asList(intent.getStringArrayExtra("selectedTopics"));

        _TopicService.getAllTopics((isSuccessful, jsonResponse) -> {
            if (isSuccessful) {
                _existingTopics.addAll(JsonService.parseTopicsJsonString(jsonResponse));
                populateTopics();
            }
        });

        Button completeButton = findViewById(R.id.CompleteButton);
        completeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("sexualOrientation", sOrient.getText().toString());
                    jsonBody.put("username", uName.getText().toString());
                    jsonBody.put("age", Integer.parseInt(age.getText().toString()));
                    jsonBody.put("gender", uGender.getText().toString());

                    List<String> currentSelectedTopics = new ArrayList<String>();
                    for (Topic topic : _existingTopics) {
                        CheckBox topicCb = findViewById(topic.hashCode());

                        if (topicCb.isChecked()) {
                            currentSelectedTopics.add(topic.id);
                        }
                    }
                    jsonBody.put("selectedTopics", new JSONArray(currentSelectedTopics));
                } catch(Exception e) {}

                RequestBody requestBody = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));

                _UserService.updateUser(ID, requestBody, (isSuccessful, jsonResponse) -> {
                    if (isSuccessful) {
                        Intent intent = new Intent();
                        intent.putExtra("uName", uName.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });

        Button editBlockedUsersButton = findViewById(R.id.BlockedUsersButton);
        editBlockedUsersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), BlockedUsersActivity.class);

                startActivity(intent);
            }
        });
    }

    private void populateTopics() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout view = findViewById(R.id.edit_selected_topics_layout);

                for (Topic topic : _existingTopics) {
                    CheckBox cb = new CheckBox(getApplicationContext());
                    cb.setText(topic.name);
                    cb.setId(topic.hashCode());

                    if (selectedTopics.contains(topic.name)) {
                        cb.setChecked(true);
                    } else {
                        cb.setChecked(false);
                    }

                    view.addView(cb);
                }
            }
        });
    }
}