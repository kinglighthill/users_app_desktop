package com.scholarly.utme.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

public class NetworkModule {
    private static final String TAG = "NetworkModule: ";

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
