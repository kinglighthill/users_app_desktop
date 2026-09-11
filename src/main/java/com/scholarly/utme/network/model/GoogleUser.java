package com.scholarly.utme.network.model;

import com.google.gson.annotations.SerializedName;

public class GoogleUser {
    private String country;
    @SerializedName("fcm_token")
    private String fcmToken;
    @SerializedName("app_slug")
    private String appSlug;
    @SerializedName("auth_code")
    private String authCode;
    @SerializedName("redirect_uri")
    private String redirectUri;
    @SerializedName("device_info")
    private DeviceInfo deviceInfo;
    @SerializedName("referrer_info")
    private ReferrerInfo referrerInfo;

    public GoogleUser(String country, String fcmToken, String appSlug, String authCode, String redirectUri, DeviceInfo deviceInfo, ReferrerInfo referrerInfo) {
        this.country = country;
        this.fcmToken = fcmToken;
        this.appSlug = appSlug;
        this.authCode = authCode;
        this.redirectUri = redirectUri;
        this.deviceInfo = deviceInfo;
        this.referrerInfo = referrerInfo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getAppSlug() {
        return appSlug;
    }

    public void setAppSlug(String appSlug) {
        this.appSlug = appSlug;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public ReferrerInfo getReferrerInfo() {
        return referrerInfo;
    }

    public void setReferrerInfo(ReferrerInfo referrerInfo) {
        this.referrerInfo = referrerInfo;
    }
}
