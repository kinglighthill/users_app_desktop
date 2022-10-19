package com.scholarly.utme.network.model;

import com.google.gson.annotations.SerializedName;

public class LoginUser {
    private String email;
    private String password;
    @SerializedName("app_id")
    private String appId = "";
    @SerializedName("device_info")
    private DeviceInfo deviceInfo;

    public LoginUser() {

    }

    public LoginUser(String email, String password, String appId, DeviceInfo deviceInfo) {
        this.email = email;
        this.password = password;
        this.appId = appId;
        this.deviceInfo = deviceInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
