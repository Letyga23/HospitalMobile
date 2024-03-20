package com.example.hospitalmobile;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ApiManager {
    private static final String API_URL = "http://192.168.0.103:5181/Patient/";
    private static final Map<HttpRequestType, String> RequestMethods = new HashMap<>();

    static {
        RequestMethods.put(HttpRequestType.GET, "GET");
        RequestMethods.put(HttpRequestType.POST, "POST");
        RequestMethods.put(HttpRequestType.PUT, "PUT");
        RequestMethods.put(HttpRequestType.DELETE, "DELETE");
    }

    public static CompletableFuture<JSONArray> getData(String stringURL) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String jsonData = getResponse(new URL(API_URL + stringURL));

                if (jsonData == null)
                    return null;

                return new JSONArray(jsonData);

            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public static CompletableFuture<Integer> sendRequest(HttpRequestType requestType, String stringURL, JSONObject jsonBody) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL(API_URL + stringURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);

                connection.setRequestMethod(RequestMethods.get(requestType));
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                if (jsonBody != null) {
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(jsonBody.toString().getBytes());
                    outputStream.flush();
                    outputStream.close();
                }

                return connection.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
                return HttpURLConnection.HTTP_INTERNAL_ERROR;
            }
        });
    }

    private static String getResponse(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();

        } catch (SocketTimeoutException e) {
            System.out.println("Время ожидания соединения истекло");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
