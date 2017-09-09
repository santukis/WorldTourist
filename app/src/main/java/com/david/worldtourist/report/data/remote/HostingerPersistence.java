package com.david.worldtourist.report.data.remote;


public class HostingerPersistence {

    private static final String URL_ROOT = "http://dsantamaria.esy.es/worldtourist/";
    private static final String REPORT_ABUSE_KEY = "reports/report_abuse.php?";
    static final String USER_ID_KEY = "uId=";
    static final String USER_NAME_KEY = "&uName=";
    static final String USER_MAIL_KEY = "&uMail=";
    static final String REPORTED_ITEM_ID_KEY = "&riId=";
    static final String REPORTED_ITEM_NAME_KEY = "&riName=";
    static final String REPORTED_MESSAGE_ID_KEY = "&rmId=";
    static final String REPORTED_USER_ID_KEY = "&ruId=";
    static final String REPORTED_USER_NAME_KEY = "&ruName=";
    static final String REPORTED_USER_MAIL_KEY = "&ruMail=";
    static final String REPORTED_PROBLEM_KEY = "&rProblem=";
    static final String EXTRA_INFORMATION_KEY = "&eInformation=";
    static final String DATE_KEY = "&date=";

    static final String SEND_REPORT_URL = URL_ROOT + REPORT_ABUSE_KEY;
}
