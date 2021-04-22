package com.example.anonymouschat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import com.example.anonymouschat.models.Topic;
import com.example.anonymouschat.models.User;
import com.example.anonymouschat.services.CurrentUserService;
import com.example.anonymouschat.services.JsonService;
import com.example.anonymouschat.services.TopicService;
import com.example.anonymouschat.services.UserService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BlockedUsersActivity extends AppCompatActivity {
    UserService _UserService;
    User currentUser;

    String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);

        _UserService = new UserService(this);
        currentUser = CurrentUserService.getCurrentUser();

        populateBlockedUsers();
    }

    private void populateBlockedUsers() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout view = findViewById(R.id.edit_blocked_users_layout);
                view.removeAllViews();

                for (User blockedUser : currentUser.blockedUsers) {
                    TextView tv = new TextView(getApplicationContext());
                    tv.setText(blockedUser.userName);

                    Button deleteBtn = new Button(getApplicationContext());
                    deleteBtn.setText("X");
                    deleteBtn.setTag(blockedUser);
                    deleteBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view)
                        {
                            User blockedUser = (User) view.getTag();
                            currentUser.blockedUsers.remove(blockedUser);
                            populateBlockedUsers();

                            JSONObject jsonBody = new JSONObject();
                            try {
                                List<String> blockedUsers = new ArrayList<String>();
                                for (User user : currentUser.blockedUsers) {
                                    blockedUsers.add(user.id);
                                }
                                jsonBody.put("blockedUsers", new JSONArray(blockedUsers));
                            } catch(Exception e) {}

                            RequestBody requestBody = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));

                            _UserService.updateUser(currentUser.id, requestBody, (isSuccessful, jsonResponse) -> {
                            });
                        }
                    });

                    LinearLayout ll = new LinearLayout(getApplicationContext());
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.addView(tv);
                    ll.addView(deleteBtn);

                    view.addView(ll);
                }
            }
        });
    }
}