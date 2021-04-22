package com.example.anonymouschat.services;

import android.content.Context;

import com.example.anonymouschat.models.GetResponseHandler;

public class TopicService extends ApiService {
    public TopicService(Context context) {
        super(context);
    }

    public void getAllTopics(GetResponseHandler responseHandler) {
        super.get("/topics", responseHandler);
    }

    public void getTopic(String topicId, GetResponseHandler responseHandler) {
        super.get(String.format("/topic/%s", topicId), responseHandler);
    }

}
