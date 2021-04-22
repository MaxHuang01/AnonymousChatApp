package com.example.anonymouschat.models;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    public String id;
    public String userName;
    public Integer age;
    public String gender;
    public String sexualOrientation;
    public List<Topic> selectedTopics;
    public List<User> blockedUsers;
    public String alias;

    public User(
            String id,
            String userName,
            Integer age,
            String gender,
            String sexualOrientation,
            List<Topic> selectedTopics,
            List<User> blockedUsers
    ) {
        this.id = id;
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.sexualOrientation = sexualOrientation;
        this.selectedTopics = selectedTopics;
        this.blockedUsers = blockedUsers;
    }

    public String getAlias() {
        if (alias == null) {
            return "";
        }
        else {
            return alias;
        }
    }
}
