package com.example.anonymouschat.models;

import java.io.Serializable;

public class Conversation implements Serializable {
    public String id;
    public String startedByUserId;
    public String startedByUserAlias;
    public String otherUserId;
    public String otherUserAlias;
    public Topic topic;

    public Conversation(
            String id,
            String startedByUserId,
            String startedByUserAlias,
            String otherUserId,
            String otherUserAlias,
            Topic topic
    ) {
        this.id = id;
        this.startedByUserId = startedByUserId;
        this.startedByUserAlias = startedByUserAlias;
        this.otherUserId = otherUserId;
        this.otherUserAlias = otherUserAlias;
        this.topic = topic;
    }

    public String getOtherUserAlias(String currentUserId) {
        if (startedByUserId.equals(currentUserId)) {
            return otherUserAlias;
        }
        else {
            return startedByUserAlias;
        }
    }
}
