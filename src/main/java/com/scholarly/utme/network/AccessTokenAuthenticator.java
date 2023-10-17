package com.scholarly.utme.network;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.response.BaseResponse;
import com.scholarly.utme.network.model.RefreshRequest;
import com.scholarly.utme.ui.utils.Alerts;
import com.scholarly.utme.util.Constants;
import com.scholarly.utme.util.PreferencesManager;
import io.reactivex.rxjava3.annotations.NonNull;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.*;

import java.io.IOException;

import static com.scholarly.utme.network.NetworkService.JSON_BODY_TYPE;
import static com.scholarly.utme.util.Constants.*;

public class AccessTokenAuthenticator implements Authenticator {
    private static final String TAG = "AccessTokenAuthenticator: ";
    private static final OkHttpClient httpClient = NetworkService.getHttpClient();
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        final String accessToken = PreferencesManager.get(Constants.PREF_KEY_ACCESS_TOKEN, "");
        if (isRequestWithAccessToken(response) || accessToken == null || accessToken.isEmpty()) {
            return null;
        }

        synchronized (this) {
            final String newAccessToken = PreferencesManager.get(Constants.PREF_KEY_ACCESS_TOKEN, "");
            // Access Token is refreshed in another thread
            if (!accessToken.equals(newAccessToken)) {
                return newRequestWithAccessToken(response.request(), newAccessToken);
            }

            // Need to refresh an Access Token
            refreshAccessToken(accessToken);
            final String updatedAccessToken = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN, "");
            return newRequestWithAccessToken(response.request(), updatedAccessToken);
        }
    }

    private boolean isRequestWithAccessToken(@NonNull Response response) {
        String header = response.request().header("Authorization");
        return header != null && header.startsWith("Bearer");
    }

    @NonNull
    private Request newRequestWithAccessToken(@NonNull Request request, @NonNull String accessToken) {
        return request.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
    }

    private void refreshAccessToken(String accessToken) {
        String END_POINT = "/login/refresh";

        System.out.println(TAG + "Old Access Token -> " + accessToken);
        System.out.println(TAG + "Old Refresh Token -> " + PreferencesManager.get(PREF_KEY_REFRESH_TOKEN, ""));
        RefreshRequest refreshRequest = new RefreshRequest(PreferencesManager.get(PREF_KEY_REFRESH_TOKEN, ""));

        Gson gson = new Gson();
        String json = gson.toJson(refreshRequest);

        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .addHeader("Authorization", "Bearer " + accessToken)
                .put(requestBody)
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println(TAG + "RefreshAccessToken: Got response code -> " + response.code());
                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    BaseResponse refreshResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                    if (refreshResponse.getStatus().equalsIgnoreCase("success")) {
                        // TODO: Encrypt and Save token with Java Keystore
                        PreferencesManager.put(PREF_KEY_ACCESS_TOKEN, refreshResponse.getData().getAccessToken());
                        PreferencesManager.put(PREF_KEY_REFRESH_TOKEN, refreshResponse.getData().getRefreshToken());
                        System.out.println(TAG + "New Access Token -> " + PreferencesManager.get(PREF_KEY_ACCESS_TOKEN, ""));
                        System.out.println(TAG + "New Refresh Token -> " + PreferencesManager.get(PREF_KEY_REFRESH_TOKEN, ""));

                    } else if (refreshResponse.getStatus().equalsIgnoreCase("error")) {
                        Platform.runLater(() -> {
                            Alert alertDialog = Alerts.info(getClass(), "Error", refreshResponse.getMessage(), "");
                            alertDialog.show();
                        });
                    }

                } catch (Exception e) {
                    System.out.println("Cannot parse response body to data class because -> " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    Alert alertDialog = Alerts.info(getClass(), "Error", "Could not connect because " + e.getMessage(), "");
                    alertDialog.show();
                });
                System.out.println("Request failed with exception -> " + e.getMessage());
            }
        });
    }
}
