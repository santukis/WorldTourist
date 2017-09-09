package com.david.worldtourist.common.data.remote;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.inject.Inject;

public class NetworkClient {

    @Inject
    public NetworkClient(){
    }

    public String makeServiceCall(String reqUrl) {
        String response = "";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            response = convertStreamToString(inputStream);

        } catch (MalformedURLException e) {
            Log.e("URL", "MalformedURLException: " + e.getMessage());

        } catch (ProtocolException e) {
            Log.e("URL", "ProtocolException: " + e.getMessage());

        } catch (IOException e) {
            Log.e("URL", "IOException: " + e.getMessage());

        } catch (Exception e) {
            Log.e("URL", "Exception: " + e.getMessage());
        }

        return response;
    }

    private String convertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }
}
