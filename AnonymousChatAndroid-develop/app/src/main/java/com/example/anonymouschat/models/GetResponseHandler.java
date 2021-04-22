package com.example.anonymouschat.models;

public interface GetResponseHandler {
    void handle(boolean isSuccessful, String jsonResponse);
}
