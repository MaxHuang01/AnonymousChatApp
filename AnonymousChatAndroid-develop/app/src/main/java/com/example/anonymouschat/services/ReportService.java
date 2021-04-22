package com.example.anonymouschat.services;

import android.content.Context;

import com.example.anonymouschat.models.GetResponseHandler;

public class ReportService extends ApiService {
    public ReportService(Context context) {
        super(context);
    }

    public void reportUser(String reporteeUser, String reportedUser, String reportMessage, GetResponseHandler responseHandler) {
        super.post("/report", JsonService.getReportJsonString(reporteeUser, reportedUser, reportMessage), responseHandler);
    }
}
