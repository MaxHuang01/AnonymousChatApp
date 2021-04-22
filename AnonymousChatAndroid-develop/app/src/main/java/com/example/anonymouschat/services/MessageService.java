package com.example.anonymouschat.services;

import android.content.Context;

import com.example.anonymouschat.models.GetResponseHandler;

public class MessageService extends ApiService {
    public MessageService(Context context) {
        super(context);
    }

    public void sendMessage(GetResponseHandler responseHandler) {
        // Calls /users for now just to test a response, but replace with correct endpoint once one is created
        super.get("/users/", responseHandler);
    }

    public void getMessagesForConversation(String conversationId, GetResponseHandler responseHandler) {
        super.get(String.format("/messages/%s", conversationId), responseHandler);
    }
}
