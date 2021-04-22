package com.example.anonymouschat.services;

import android.content.Context;

import com.example.anonymouschat.R;
import com.example.anonymouschat.models.GetResponseHandler;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class ApiService {
    private final Context _context;
    private final String _baseUrl;
    private final OkHttpClient _client;

    public ApiService(Context context) {
        _context = context;
        _baseUrl = String.format(
                "%s%s",
                context.getString(R.string.apiProtocol),
                context.getString(R.string.apiBaseUrl)
        );
        _client = new OkHttpClient();
    }

    public void get(String path, final GetResponseHandler responseHandler) {
        final Request request = new Request.Builder()
                .url(_baseUrl + path)
                .method("GET", null)
                .build();

        _client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                responseHandler.handle(response.isSuccessful(), response.body().string());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void put(String path, RequestBody body, final GetResponseHandler responseHandler) {
        final Request request = new Request.Builder()
                .url(_baseUrl + path)
                .method("PUT", body)
                .build();

        _client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                responseHandler.handle(response.isSuccessful(), response.body().string());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void post(String path, String jsonString, final GetResponseHandler responseHandler) {
        final RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json"));

        final Request request = new Request.Builder()
                .url(_baseUrl + path)
                .post(body)
                .build();

        _client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                responseHandler.handle(response.isSuccessful(), response.body().string());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void delete(String path, final GetResponseHandler responseHandler) {
        final Request request = new Request.Builder()
                .url(_baseUrl + path)
                .delete()
                .build();

        _client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                responseHandler.handle(response.isSuccessful(), response.body().string());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }
}
