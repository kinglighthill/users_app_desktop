package com.scholarly.utme.network.model;

public class Data {
    private boolean isActivationActive;
    private String token;

    public Data(boolean isActivationActive, String token) {
        this.isActivationActive = isActivationActive;
        this.token = token;
    }

    public boolean isActivationActive() {
        return isActivationActive;
    }

    public String getToken() {
        return token;
    }
}
