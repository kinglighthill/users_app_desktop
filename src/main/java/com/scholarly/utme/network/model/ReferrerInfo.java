package com.scholarly.utme.network.model;

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
}
