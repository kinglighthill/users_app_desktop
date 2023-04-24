package com.scholarly.utme.network.model;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("access_token")
    private String accessToken;
    private String refreshToken;
    @SerializedName("user_data")
    private User userData;
    @SerializedName("activation_state")
    private ActivationState activationState;

    public Data() {

    }

    public Data(String accessToken, String refreshToken, User userData, ActivationState activationState) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userData = userData;
        this.activationState = activationState;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public User getUserData() {
        return userData;
    }

    public ActivationState getActivationState() {
        return activationState;
    }
}
