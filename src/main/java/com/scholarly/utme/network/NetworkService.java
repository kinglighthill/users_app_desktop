package com.scholarly.utme.network;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.SignupResponse;
import com.scholarly.utme.network.model.User;
import okhttp3.*;

import java.io.IOException;

public class NetworkService {
    private static String BASE_URL = "https://staging.utme.scholarly.africa/api/v1/";

    private static final MediaType JSON_BODY_TYPE
            = MediaType.parse("application/json; charset=utf-8");

    private static SignupResponse signupResponse;

    public static SignupResponse createNewUser(User newUser) {
        String END_POINT = "signup";

        OkHttpClient client = NetworkModule.getHttpClient();

        Gson gson = new Gson();
        String json = gson.toJson(newUser);

        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println("Got response with code -> " + response.code());
                if (response.isSuccessful()) {
                    try (ResponseBody responseBody = response.body()) {
                        assert responseBody != null;
                        signupResponse = gson.fromJson(responseBody.string(), SignupResponse.class);

                    } catch (Exception e) {
                        System.out.println("Cannot parse response body to class because -> " + e.getMessage());
                    }
                }

                signupResponse = new SignupResponse("error", null, response.message());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Request failed with exception -> " + e.getMessage());
            }
        });

        return signupResponse;
    }


}
