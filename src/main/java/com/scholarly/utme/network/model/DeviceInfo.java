package com.scholarly.utme.network.model;

import com.google.gson.annotations.SerializedName;

public class DeviceInfo {
    private String name;
    private String version;
    @SerializedName("api_level")
    private String apiLevel;
    private String platform;
    @SerializedName("form_factor")
    private String formFactor;
    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("app_version_name")
    private String appVersionName;

    public DeviceInfo() {

    }

    public DeviceInfo(String name, String version, String apiLevel, String platform, String formFactor, String deviceId, String appVersionName) {
        this.name = name;
        this.version = version;
        this.apiLevel = apiLevel;
        this.platform = platform;
        this.formFactor = formFactor;
        this.deviceId = deviceId;
        this.appVersionName = appVersionName;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getApiLevel() {
        return apiLevel;
    }

    public String getPlatform() {
        return platform;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getAppVersionName() {
        return appVersionName;
    }
}
