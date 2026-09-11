package com.scholarly.network.model;

import com.google.gson.annotations.SerializedName;
import com.scholarly.data.model.listItems.AppItem;

import java.util.List;

public class Data {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("refreshToken")
    private String authRefreshToken;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String gender;
    @SerializedName("user_data")
    private UserData userData;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("project_id")
    private String projectId;
    @SerializedName("activation_state")
    private ActivationState activationState;
    private List<AppItem> apps;

    public Data() {
    }

    public Data(String accessToken, String refreshToken, String authRefreshToken, String fullName, String phoneNumber, String gender, UserData userData, String userId, String projectId, ActivationState activationState, List<AppItem> apps) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.authRefreshToken = authRefreshToken;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.userData = userData;
        this.userId = userId;
        this.projectId = projectId;
        this.activationState = activationState;
        this.apps = apps;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        if (refreshToken == null && authRefreshToken != null) {
            return authRefreshToken;
        }
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

    public UserData getUserData() {
        return userData;
    }

    public String getUserId() {
        return userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public ActivationState getActivationState() {
        return activationState;
    }

    public List<AppItem> getApps() {
        return apps;
    }
}
