package com.scholarly.utme.network.model;

import com.google.gson.annotations.SerializedName;

public class ActivationInfo {
    private String pin;
    @SerializedName("device_id")
    private String deviceId;

    public ActivationInfo(String pin, String deviceId) {
        this.pin = pin;
        this.deviceId = deviceId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
