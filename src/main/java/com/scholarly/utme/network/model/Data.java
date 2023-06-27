package com.scholarly.utme.network.model;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("access_token")
    private String accessToken;
    private String refreshToken;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String gender;
    @SerializedName("user_data")
    private User userData;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("activation_state")
    private ActivationState activationState;

    public Data() {
    }

    public Data(String accessToken, String refreshToken, String fullName, String phoneNumber, String gender, User userData, String userId, ActivationState activationState) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.userData = userData;
        this.userId = userId;
        this.activationState = activationState;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public User getUserData() {
        return userData;
    }

    public String getUserId() {
        return userId;
    }

    public ActivationState getActivationState() {
        return activationState;
    }
}
