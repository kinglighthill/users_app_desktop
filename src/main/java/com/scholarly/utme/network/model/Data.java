package com.scholarly.utme.network.model;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("user_data")
    private SignupUser signupUserData;
    @SerializedName("activation_state")
    private ActivationState activationState;

    public Data() {

    }

    public Data(String accessToken, SignupUser signupUserData, ActivationState activationState) {
        this.accessToken = accessToken;
        this.signupUserData = signupUserData;
        this.activationState = activationState;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public SignupUser getUserData() {
        return signupUserData;
    }

    public ActivationState getActivationState() {
        return activationState;
    }
}
