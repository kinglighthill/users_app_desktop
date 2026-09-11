package com.scholarly.network.model;

import com.google.gson.annotations.SerializedName;

public class ActivationState {
    @SerializedName("activation_active")
    private boolean activationActive;
    private String message;

    public ActivationState(boolean activationActive, String message) {
        this.activationActive = activationActive;
        this.message = message;
    }

    public boolean isActivationActive() {
        return activationActive;
    }

    public String getMessage() {
        return message;
    }
}
