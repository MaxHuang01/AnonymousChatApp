package com.example.anonymouschat.services;

import android.content.Context;

import com.example.anonymouschat.models.GetResponseHandler;

import okhttp3.RequestBody;

public class UserService extends ApiService {
    public UserService(Context context) {
        super(context);
    }

    public void getAllUsers(GetResponseHandler responseHandler) {
        super.get("/users", responseHandler);
    }

    public void getUsersByTopic(String topicId, GetResponseHandler responseHandler) {
        super.get(String.format("/users/%s", topicId), responseHandler);
    }

    public void getUser(String userId, GetResponseHandler responseHandler) {
        super.get(String.format("/user/%s", userId), responseHandler);
    }

    public void updateUser(String userId, RequestBody body, GetResponseHandler responseHandler) {
        super.put(String.format("/user/%s", userId), body, responseHandler);
    }

    public void login(String idToken, GetResponseHandler responseHandler) {
        super.post("/login", JsonService.getLoginRequestJsonString(idToken), responseHandler);
    }

    public void deleteUser(String userId, GetResponseHandler responseHandler) {
        super.delete(String.format("/user/%s", userId), responseHandler);
    }
}
