package com.david.worldtourist.report.data.remote;


import com.david.worldtourist.common.data.remote.NetworkClient;
import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.report.data.boundary.ReportDataSource;
import com.david.worldtourist.report.domain.usecase.SendReport;
import com.david.worldtourist.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HostingerDataSource implements ReportDataSource {

    public static HostingerDataSource INSTANCE = null;

    private NetworkClient networkClient;

    private HostingerDataSource(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    public static HostingerDataSource getInstance(NetworkClient networkClient) {
        if(INSTANCE == null) {
            INSTANCE = new HostingerDataSource(networkClient);
        }
        return INSTANCE;
    }


    @Override
    public void sendReport(SendReport.RequestValues requestValues,
                           UseCase.Callback<SendReport.ResponseValues> callback) {

        String response = Constants.EMPTY_STRING;

        try {

            response = networkClient.makeServiceCall(HostingerPersistence.SEND_REPORT_URL +
                    HostingerPersistence.USER_ID_KEY +
                    URLEncoder.encode(requestValues.getReport().getReporterUser().getId(), "UTF-8") +
                    HostingerPersistence.USER_NAME_KEY +
                    URLEncoder.encode(requestValues.getReport().getReporterUser().getName(), "UTF-8") +
                    HostingerPersistence.USER_MAIL_KEY +
                    URLEncoder.encode(requestValues.getReport().getReporterUser().getMail(), "UTF-8") +
                    HostingerPersistence.REPORTED_ITEM_ID_KEY +
                    URLEncoder.encode(requestValues.getReport().getReportedItem().getId(), "UTF-8") +
                    HostingerPersistence.REPORTED_ITEM_NAME_KEY +
                    URLEncoder.encode(requestValues.getReport().getReportedItem().getName(), "UTF-8") +
                    HostingerPersistence.REPORTED_MESSAGE_ID_KEY +
                    URLEncoder.encode(requestValues.getReport().getReportedMessage().getMessageId(),"UTF-8") +
                    HostingerPersistence.REPORTED_USER_ID_KEY +
                    URLEncoder.encode(requestValues.getReport().getReportedMessage().getUser().getId(), "UTF-8") +
                    HostingerPersistence.REPORTED_USER_NAME_KEY +
                    URLEncoder.encode(requestValues.getReport().getReportedMessage().getUser().getName(), "UTF-8") +
                    HostingerPersistence.REPORTED_USER_MAIL_KEY +
                    URLEncoder.encode(requestValues.getReport().getReportedMessage().getUser().getMail(), "UTF-8") +
                    HostingerPersistence.REPORTED_PROBLEM_KEY +
                    URLEncoder.encode(requestValues.getReport().getReportedProblem().toString(), "UTF-8") +
                    HostingerPersistence.EXTRA_INFORMATION_KEY +
                    URLEncoder.encode(requestValues.getReport().getExtraInformation(), "UTF-8") +
                    HostingerPersistence.DATE_KEY +
                    URLEncoder.encode(requestValues.getReport().getDate().toString(), "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(response.equals("OK\n")) {
            callback.onSuccess(new SendReport.ResponseValues());

        } else {
            callback.onError(response);
        }
    }
}