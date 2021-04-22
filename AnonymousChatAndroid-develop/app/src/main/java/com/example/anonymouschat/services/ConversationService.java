package com.example.anonymouschat.services;

import android.content.Context;

import com.example.anonymouschat.models.GetResponseHandler;

public class ConversationService extends ApiService {
    public ConversationService(Context context) {
        super(context);
    }

    public void getConversationsForUser(String userId, GetResponseHandler responseHandler) {
        super.get(String.format("/conversations/%s", userId), responseHandler);
    }
}
