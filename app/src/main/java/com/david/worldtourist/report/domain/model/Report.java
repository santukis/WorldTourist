package com.david.worldtourist.report.domain.model;


import android.support.annotation.NonNull;

import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.message.domain.model.Message;

import java.io.Serializable;
import java.sql.Date;


public class Report implements Serializable {

    private User reporterUser = User.EMPTY_USER;
    private Item reportedItem = Item.EMPTY_ITEM;
    private Message reportedMessage = Message.MESSAGE_EMPTY;
    private ReportFilter reportedProblem = ReportFilter.NONE;
    private String extraInformation = "";
    private Date date;

    public static Report EMPTY_REPORT = new Report();

    private Report() {

    }


    public Report(@NonNull User user, @NonNull Item item, @NonNull Message message) {
        reporterUser = user;
        reportedItem = item;
        reportedMessage = message;
        date = new Date(System.currentTimeMillis());
    }

    public User getReporterUser() {
        return reporterUser;
    }

    public void setReporterUser(User user) {
        this.reporterUser = user;
    }

    public Message getReportedMessage() {
        return reportedMessage;
    }

    public void setReportedMessage(Message reportedMessage) {
        this.reportedMessage = reportedMessage;
    }

    public Item getReportedItem() {
        return reportedItem;
    }

    public void setReportedItem(Item reportedItem) {
        this.reportedItem = reportedItem;
    }

    public ReportFilter getReportedProblem() {
        return reportedProblem;
    }

    public void setReportedProblem(ReportFilter mReportProblem) {
        this.reportedProblem = mReportProblem;
    }

    public String getExtraInformation() {
        return extraInformation;
    }

    public void setExtraInformation(String mExtraInformation) {
        this.extraInformation = mExtraInformation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
