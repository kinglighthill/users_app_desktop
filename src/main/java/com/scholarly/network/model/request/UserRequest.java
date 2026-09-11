package com.scholarly.network.model.request;

import com.google.gson.annotations.SerializedName;
import com.scholarly.network.model.DeviceInfo;
import com.scholarly.network.model.ReferrerInfo;

public class UserRequest {
    @SerializedName("full_name")
    private String fullName;
    private String email;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String password;
    private String country;
    @SerializedName("fcm_token")
    private String fcmToken;
    @SerializedName("app_slug")
    private String appSlug;
    @SerializedName("email_verified")
    private boolean emailVerified;
    @SerializedName("profile_pic_url")
    private String profilePicUrl;
    @SerializedName("device_info")
    private DeviceInfo deviceInfo;
    @SerializedName("referrer_info")
    private ReferrerInfo referrerInfo;

    public UserRequest() {

    }

    public UserRequest(String fullName, String email, String phoneNumber, String password, String country, String fcmToken, String appSlug, boolean emailVerified, String profilePicUrl, DeviceInfo deviceInfo, ReferrerInfo referrerInfo) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.country = country;
        this.fcmToken = fcmToken;
        this.appSlug = appSlug;
        this.emailVerified = emailVerified;
        this.profilePicUrl = profilePicUrl;
        this.deviceInfo = deviceInfo;
        this.referrerInfo = referrerInfo;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public String getAppSlug() {
        return appSlug;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public ReferrerInfo getReferrerInfo() {
        return referrerInfo;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void setAppSlug(String appSlug) {
        this.appSlug = appSlug;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public void setReferrerInfo(ReferrerInfo referrerInfo) {
        this.referrerInfo = referrerInfo;
    }
}
