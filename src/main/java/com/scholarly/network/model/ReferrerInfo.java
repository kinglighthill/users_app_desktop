package com.scholarly.network.model;

import com.google.gson.annotations.SerializedName;

public class ReferrerInfo {
    @SerializedName("referrer_code")
    private String referrerCode;
    @SerializedName("referrer_url")
    private String referrerUrl;
    @SerializedName("referrer_click_time")
    private String referrerClickTime;
    @SerializedName("app_install_time")
    private String appInstallTime;
    @SerializedName("instant_experience_launched")
    private boolean instantExperienceLaunched;

    public ReferrerInfo() {
        referrerCode = "";
        referrerUrl = "";
        referrerClickTime = "";
        appInstallTime = "";
        instantExperienceLaunched = false;
    }

    public ReferrerInfo(String referrerCode, String referrerUrl, String referrerClickTime, String appInstallTime, boolean instantExperienceLaunched) {
        this.referrerCode = referrerCode;
        this.referrerUrl = referrerUrl;
        this.referrerClickTime = referrerClickTime;
        this.appInstallTime = appInstallTime;
        this.instantExperienceLaunched = instantExperienceLaunched;
    }

    public String getReferrerCode() {
        return referrerCode;
    }

    public String getReferrerUrl() {
        return referrerUrl;
    }

    public String getReferrerClickTime() {
        return referrerClickTime;
    }

    public String getAppInstallTime() {
        return appInstallTime;
    }

    public boolean isInstantExperienceLaunched() {
        return instantExperienceLaunched;
    }

    public void setReferrerCode(String referrerCode) {
        this.referrerCode = referrerCode;
    }

    public void setReferrerUrl(String referrerUrl) {
        this.referrerUrl = referrerUrl;
    }

    public void setReferrerClickTime(String referrerClickTime) {
        this.referrerClickTime = referrerClickTime;
    }

    public void setAppInstallTime(String appInstallTime) {
        this.appInstallTime = appInstallTime;
    }

    public void setInstantExperienceLaunched(boolean instantExperienceLaunched) {
        this.instantExperienceLaunched = instantExperienceLaunched;
    }
}
