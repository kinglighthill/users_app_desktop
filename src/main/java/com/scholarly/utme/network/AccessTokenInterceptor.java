package com.scholarly.utme.network;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.RefreshRequest;
import com.scholarly.utme.util.PreferencesManager;
import io.reactivex.rxjava3.annotations.NonNull;
import okhttp3.*;

import java.io.IOException;
import java.net.HttpURLConnection;

import static com.scholarly.utme.network.NetworkService.JSON_BODY_TYPE;
import static com.scholarly.utme.util.Constants.*;

public class AccessTokenInterceptor implements Interceptor {
    private static final String TAG = "AccessTokenInterceptor: ";

    @Override
    public Response intercept(Chain chain) throws IOException {
        System.out.println(TAG + "Intercept called!");

        Request request = chain.request().newBuilder()
                .build();

        Response response = chain.proceed(request); // Perform request, here original request will be executed

        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            System.out.println(TAG + "Access Token is expired");

//            String refreshedAccessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjhkMDNhZTdmNDczZjJjNmIyNTI3NmMwNjM2MGViOTk4ODdlMjNhYTrekiLCJ0eXAiOiJKV1QifQ.eyJ1dWlkIjoiY0NWT2FZMDllYXJVYVFDRHVCVDciLCJlbWFpbF9hZGRyZXNzIjoicHJpbmNlY2hpbmVjaGVyZW0zNUBnbWFpbC5jb20iLCJjb3VudHJ5IjoibmlnZXJpYSIsImlzX2FjdGl2YXRpb25fYWN0aXZlIjpmYWxzZSwiZGV2aWNlX2lkIjoiQkQxREFEQzQtMjg1RC01OTRBLUJDNEEtNUNDRjNBNTUxOTkxIiwiYXBwX3NsdWciOiJ1dG1lIiwiaXNzIjoiaHR0cHM6Ly9zZWN1cmV0b2tlbi5nb29nbGUuY29tL3NjaG9sYXJseS11dG1lLXN0YWdpbmciLCJhdWQiOiJzY2hvbGFybHktdXRtZS1zdGFnaW5nIiwiYXV0aF90aW1lIjoxNjgyNjY2NjU1LCJ1c2VyX2lkIjoiY0NWT2FZMDllYXJVYVFDRHVCVDciLCJzdWIiOiJjQ1ZPYVkwOWVhclVhUUNEdUJUNyIsImlhdCI6MTY4NzQ0Mjk5MiwiZXhwIjoxNjg3NDQ2NTkyLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7fSwic2lnbl9pbl9wcm92aWRlciI6ImN1c3RvbSJ9fQ.wbZP4N1lKXyVEoOq3osMEfBTutjEX5yxQzRchKLWN6x9CQHXKvotgaun7T8f36eakU2y2EzV8-kgBxeiSd4k1y-4mKANk-_tVZGDt33pyGNvt8-QcUFA3J9ZQEUG2IXj4bAoZb_5P39XOAkbkphNJna8317yqqYtPdhw-LeyQHgnH9fv4xI4fURGf2K-umLbDxZMKpHvwIudTQhfnU9sE_NQXuGMDNR4dN199nPr41HOmceLYSW5NTVUSZbJpu8iPyS_h4-asthn-QW550V5Ruq7RaGTa8jOdUY1jc9-wyzGT_cTC5has5FfkEusEU33r80RuaPhteInxU7X_VTFLw";
//            String refreshedAccessToken = refreshAccessToken();
//            System.out.println(TAG + " New AccessToken -> " + refreshedAccessToken);
//            Request newRequest = chain.request().newBuilder()
//                    .header("Authorization", "Bearer " + refreshedAccessToken)
//                    .build();
//            return chain.proceed(newRequest);
        }

        return response;
    }

    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) //Add Auth token to each request if authorized
            builder.header("Authorization", String.format("Bearer %s", token));
    }

    @NonNull
    private Request newRequestWithAccessToken(@NonNull Request request, @NonNull String accessToken) {
        System.out.println(TAG + "New Request With AccessToken Called!");
        String END_POINT = "/login/refresh";

        System.out.println(TAG + "Old Access Token -> " + accessToken);
        System.out.println(TAG + "Old Refresh Token -> " + PreferencesManager.get(PREF_KEY_REFRESH_TOKEN, ""));
        RefreshRequest refreshRequest = new RefreshRequest(PreferencesManager.get(PREF_KEY_REFRESH_TOKEN, ""));

        Gson gson = new Gson();
        String json = gson.toJson(refreshRequest);
        System.out.println(TAG + "JSON Request Body -> " + json);

        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);
        return request.newBuilder()
//                .url(BASE_URL + END_POINT)
                .header("Authorization", "Bearer " + accessToken)
//                .post(requestBody)
                .build();
    }

}
