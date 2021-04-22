package com.example.anonymouschat.services;

import com.example.anonymouschat.models.User;

public class CurrentUserService {
    private static CurrentUserService _instance;
    private User _currentUser;

    private CurrentUserService() { }

    private static CurrentUserService _getInstance() {
        if (_instance == null) {
            _instance = new CurrentUserService();
        }

        return _instance;
    }

    public static User getCurrentUser() {
        CurrentUserService instance = _getInstance();
        return instance._currentUser;
    }

    public static void setCurrentUser(User user) {
        CurrentUserService instance = _getInstance();
        instance._currentUser = user;
    }
}
