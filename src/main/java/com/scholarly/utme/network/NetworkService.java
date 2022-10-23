package com.scholarly.utme.network;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class NetworkService {
    private static final String TAG = "NetworkModule: ";
    public static final MediaType JSON_BODY_TYPE
            = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient httpClient;

    private static final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    public static OkHttpClient getHttpClient() {
        try {
            if (httpClient == null) {
                httpClient = new OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .build();
            }
        } catch (Exception e) {
            System.out.println(TAG + "Cannot create HTTP client because -> " + e.getMessage());
        }

        return httpClient;
    }
}
