package com.example.anonymouschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.anonymouschat.models.User;
import com.example.anonymouschat.services.CurrentUserService;
import com.example.anonymouschat.services.JsonService;
import com.example.anonymouschat.services.UserService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private UserService _userService;

    private CallbackManager mCallbackManager;

    private LoginButton _loginButton;
    private ProgressBar _progressBar;

    private static final String TAG = "Login Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _userService = new UserService(this);

        _progressBar = findViewById(R.id.progressBar);
        _loginButton = findViewById(R.id.login_button);
        _loginButton.setReadPermissions("email", "public_profile");

        mCallbackManager = CallbackManager.Factory.create();

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _showProgressBar();
            }
        });

        _loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess(): " + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel()");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: " + error);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        _updateUI(currentUser);
    }

    private void _enableLoginButton() {
        _progressBar.setVisibility(View.INVISIBLE);
        _loginButton.setVisibility(View.VISIBLE);
    }

    private void _showProgressBar() {
        _loginButton.setVisibility(View.INVISIBLE);
        _progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookToken(AccessToken accessToken) {
        Log.d("TAG", "handleFacebookToken: " + accessToken);

        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Sign in with Facebook: success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            _updateUI(user);
                        }
                        else {
                            Log.d(TAG, "Sign in with Facebook: failed", task.getException());
                            _updateUI(null);
                        }
                    }
                });
    }

    private void _updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        _userService.login(idToken, ((isSuccessful, jsonResponse) -> {
                            if (isSuccessful) {
                                User currentUser = JsonService.parseFirstUserInArrayJsonString(jsonResponse);
                                CurrentUserService.setCurrentUser(currentUser);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            else {
                                _notifyLoginError();
                            }
                        }));
                    }
                    else {
                        _notifyLoginError();
                    }
                }
            });
        }
        else {
            _enableLoginButton();
        }
    }

    private void _notifyLoginError() {
        _enableLoginButton();
    }
}